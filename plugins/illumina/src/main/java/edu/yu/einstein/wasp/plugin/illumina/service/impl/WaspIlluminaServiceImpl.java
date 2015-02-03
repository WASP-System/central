package edu.yu.einstein.wasp.plugin.illumina.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SgeWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.illumina.plugin.IlluminaResourceCategory;
import edu.yu.einstein.wasp.plugin.illumina.plugin.WaspIlluminaPlatformPlugin;
import edu.yu.einstein.wasp.plugin.illumina.service.WaspIlluminaService;
import edu.yu.einstein.wasp.plugin.illumina.util.IlluminaRunFolderNameParser;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties.ReadType;
import edu.yu.einstein.wasp.plugin.mps.service.SequencingService;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspMessageHandlingServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.PropertyHelper;

@Service
@Transactional("entityManager")
public class WaspIlluminaServiceImpl extends WaspMessageHandlingServiceImpl implements WaspIlluminaService{
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private RunDao runDao;
	
	@Autowired
	private RunMetaDao runMetaDao;
	
	@Autowired
	private SequencingService sequencingService;
	
	@Autowired
	private AdaptorService adaptorService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIlluminaRunFolderPath(GridWorkService gws, String resourceCategoryIName) throws GridException {
		String dataDir = null;
		if (resourceCategoryIName.equals(IlluminaResourceCategory.HISEQ_2000) || resourceCategoryIName.equals(IlluminaResourceCategory.HISEQ_2500)){
			dataDir = gws.getTransportConnection().getConfiguredSetting("localhost.settings.illumina.data.hiseq.dir");
			if (!PropertyHelper.isSet(dataDir))
				throw new GridException("illumina data folder 'localhost.settings.illumina.data.hiseq.dir' is not defined in properties!");
		}
		else if (resourceCategoryIName.equals(IlluminaResourceCategory.PERSONAL)){
			dataDir = gws.getTransportConnection().getConfiguredSetting("localhost.settings.illumina.data.personalseq.dir");
			if (!PropertyHelper.isSet(dataDir))
				throw new GridException("illumina data folder 'localhost.settings.illumina.data.personalseq.dir' is not defined in properties!");
		}
		return dataDir;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getIlluminaRunFolders(String resourceCategoryIName) throws GridException{
		Set<String> folderNames = new LinkedHashSet<String>();
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		
		c.setProcessMode(ProcessMode.SINGLE);
		GridWorkService workService = hostResolver.getGridWorkService(c);
		GridTransportConnection transportConnection = workService.getTransportConnection();
		String dataDir = getIlluminaRunFolderPath(workService, resourceCategoryIName);
		c.setWorkingDirectory(dataDir);
		WorkUnit w = new WorkUnit(c);
		w.addCommand("ls -1t");
		try {
			GridResult r = transportConnection.sendExecToRemote(w);
			BufferedReader br = new BufferedReader(new InputStreamReader(r.getStdOutStream()));
			boolean keepReading = true;
			while (keepReading){
				String line = br.readLine();
				if (line == null)
					keepReading = false;
				else{
					if (IlluminaRunFolderNameParser.isProperlyFormed(line)){
						logger.debug("Adding Illumina Run Folder " + line + " to working set");
						folderNames.add(line);
						IlluminaRunFolderNameParser parser = new IlluminaRunFolderNameParser(line);
						logger.debug(parser.toString());
					}
				}
			}
			br.close();
		} catch (Exception e) {
			throw new GridException("Caught " + e.getClass().getSimpleName() + " when trying to read Illumina run folder : " + e.getLocalizedMessage());
		}
		return folderNames;
	}

	@Override
	public void setIlluminaRunXml(GridResult result, Run run, String runXML) throws GridException, IOException, MetadataException {
		
		GridWorkService gws = hostResolver.getGridWorkService(result);
		
		if (!result.getResultsDirectory().contains(run.getName())) {
			String mess = "result from " + result.getResultsDirectory() + " does not appear to be related to " + run.getName();
			logger.error(mess);
			throw new MetadataException(mess);
		}
		
		String runInfoS = StringUtils.chomp(gws.getUnregisteredFileContents(result, runXML, SgeWorkService.NO_FILE_SIZE_LIMIT));		
		
		setRunMeta(run, RUN_INFO, runInfoS);
		
	}
	
	@Override
	public void setIlluminaRunXml(GridResult result, Run run) throws GridException, IOException, MetadataException {
		setIlluminaRunXml(result, run, "RunInfo.xml");
	}
	
	
	protected void setRunMeta(Run run, String metaKey, String metaValue) throws MetadataException {
		Assert.assertParameterNotNull(run, "run cannot be null");
		Assert.assertParameterNotNull(metaKey, "metaKey cannot be null");
		Assert.assertParameterNotNull(metaValue, "metaValue cannot be null");
		run = runDao.merge(run);
		RunMeta runMeta = new RunMeta();
		runMeta.setRunId(run.getId());
		runMeta.setK(ILLUMINA_AREA + "." + metaKey);
		runMeta.setV(metaValue);
		logger.debug("setting meta  " + runMeta + " for Run with id=" + run.getId());
		runMetaDao.setMeta(runMeta);
	}
	
	protected String getRunMeta(Run run, String area, String k) {
		Assert.assertParameterNotNull(run, "file group cannot be null");
		String v = null;
		List<RunMeta> runMetaList = runMetaDao.getMeta(run.getId());
		if (runMetaList == null)
			runMetaList = new ArrayList<RunMeta>();
		runMetaList.size();
		try{
			v = (String) MetaHelper.getMetaValue(area, k, runMetaList);
		} catch(MetadataException e) {
			logger.debug("unable to get a meta value with key=" + area + "." + k + " for FileGroup with id=" + run.getId());
		}
		logger.debug("returning meta value " + area + "." + k + "=" + v + " for FileGroup with id=" + run.getId());
		return v;
	}

	@Override
	public Document getIlluminaRunXml(Run run) throws MetadataException {
		InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(getRunMeta(run, ILLUMINA_AREA, RUN_INFO)));
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			return db.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			String mess = "unable to create documentbuilder, throwing metadataexception, reason: " + e.getLocalizedMessage();
			logger.warn(mess);
			throw new MetadataException(mess, e);
		}
		
	}

	@Override
	public int getNumberOfReadSegments(Run run) throws MetadataException {
		Document dom = getIlluminaRunXml(run);
		int n = 0;
		NodeList nodes = dom.getElementsByTagName("Read");
		for (int i = 0; i < nodes.getLength(); i++) {
		      Element element = (Element) nodes.item(i);
		      if (element.getAttribute("IsIndexedRead").equals("N"))
		    	  n++;
		}
		return n;
	}

	@Override
	public int getNumberOfIndexedReads(Run run) throws MetadataException {
		Document dom = getIlluminaRunXml(run);
		int n = 0;
		NodeList nodes = dom.getElementsByTagName("Read");
		for (int i = 0; i < nodes.getLength(); i++) {
		      Element element = (Element) nodes.item(i);
		      if (element.getAttribute("IsIndexedRead").equals("Y"))
		    	  n++;
		}
		return n;
	}

	@Override
	public int getTotalNumberOfReads(Run run) throws MetadataException {
		Document dom = getIlluminaRunXml(run);
		int n = 0;
		NodeList nodes = dom.getElementsByTagName("Read");
		n = nodes.getLength();
		return n;
	}

	@Override
	public List<Integer> getLengthOfReadSegments(Run run) throws MetadataException {
		Document dom = getIlluminaRunXml(run);
		List<Integer> res = new ArrayList<Integer>();
		NodeList nodes = dom.getElementsByTagName("Read");
		for (int i = 0; i < nodes.getLength(); i++) {
		      Element element = (Element) nodes.item(i);
		      if (element.getAttribute("IsIndexedRead").equals("N"))
		    	  res.add(Integer.parseInt(element.getAttribute("NumCycles")));
		}
		return res;
	}

	@Override
	public List<Integer> getLengthOfIndexedReads(Run run) throws MetadataException {
		Document dom = getIlluminaRunXml(run);
		List<Integer> res = new ArrayList<Integer>();
		NodeList nodes = dom.getElementsByTagName("Read");
		for (int i = 0; i < nodes.getLength(); i++) {
		      Element element = (Element) nodes.item(i);
		      if (element.getAttribute("IsIndexedRead").equals("Y"))
		    	  res.add(Integer.parseInt(element.getAttribute("NumCycles")));
		}
		return res;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean assayAllowsPairedEndData(SampleSource cellLibrary) throws MetadataException {
		boolean retval = false;
		Job job = sampleService.getJobOfLibraryOnCell(cellLibrary);
		List<JobResourcecategory> jrc = job.getJobResourcecategory();
		ResourceCategory illumina = jrc.get(0).getResourceCategory();
		if (jrc.size() != 1) {
			logger.warn("expected job to have JobResourceCategory size of 1, found " + jrc.size() + " attempting to proceed using " + illumina.getName());
		}
		retval = sequencingService.isReadTypeConfiguredToBeAvailable(job.getWorkflow(), illumina, ReadType.PAIRED);
		logger.trace("workflow: " + job.getWorkflow().getName() + " resourcecategory: " + illumina.getName() + " paired=" + retval);
		return retval;
	}

	@Override
	public IndexingStrategy getIndexingStrategy(SampleSource cellLibrary) throws WaspException {
		Sample library = sampleService.getLibrary(cellLibrary);
		return adaptorService.getIndexingStrategy(adaptorService.getAdaptor(library).getAdaptorset());
	}
	
	
	/**
	 * {@inheritDoc}
	 * @param run
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public void startTrimOnlyWorkflow(Run run) throws WaspMessageBuildingException{
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.RUN_ID, run.getId().toString());
		launchBatchJob(WaspIlluminaPlatformPlugin.ILLUMINA_TRIM_ONLY_FLOW_NAME, jobParameters);
	}


}
