package edu.yu.einstein.wasp.chipseq.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.chipseq.service.ChipSeqService;
import edu.yu.einstein.wasp.chipseq.webpanels.ChipSeqWebPanels;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.viewpanel.Content;
import edu.yu.einstein.wasp.viewpanel.DataTabViewing.Status;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

@Service
@Transactional("entityManager")
public class ChipSeqServiceImpl extends WaspServiceImpl implements ChipSeqService {
	
	@Autowired
	private SampleService sampleService;
	@Autowired
	private JobService jobService;
	@Autowired
	ResourceType peakcallerResourceType;
	@Autowired
	private WaspPluginRegistry waspPluginRegistry;
	@Autowired
	private SoftwareDao softwareDao;
	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public Set<PanelTab> getChipSeqDataToDisplay(Integer jobId, Status jobStatus) throws PanelException{
		logger.debug("***************starting chipseqService.getChipSeqDataToDisplay(job)");
		logger.debug("***************a");
		Job job = jobService.getJobByJobId(jobId);
		
		try{
			Strategy strategy = jobService.getStrategy(Strategy.StrategyType.LIBRARY_STRATEGY, job);
			
			WaspJobContext waspJobContext = new WaspJobContext(jobId, jobService);
			SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(peakcallerResourceType);
			if (softwareConfig == null){
				throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + peakcallerResourceType.getIName());
			}
			BatchJobProviding softwarePlugin = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), BatchJobProviding.class);
			//String softwareName = softwarePlugin.getName();//macsTwo; softwarePlugin.getIName() is macstwo
			String softwareName = softwareDao.getSoftwareByIName(softwarePlugin.getIName()).getName();//should get "MACS2 Peakcaller"
			
			
			
			Sample noControlSample = new Sample();
			noControlSample.setId(0);
			noControlSample.setName("None");
			Set<Sample> testSampleSet = new HashSet<Sample>();
			Map<Sample, List<Sample>> testSampleControlSampleListMap = new HashMap<Sample, List<Sample>>();
			logger.debug("***************b");
			for(Sample sample : job.getSample()){logger.debug("***************c");
				for(SampleMeta sm : sample.getSampleMeta()){logger.debug("***************d");
					if(sm.getK().startsWith("chipseqAnalysis.controlId::")){//could me zero, one, many
						Sample controlSample = null;
						Integer controlId = Integer.parseInt(sm.getV());
						if(controlId.intValue()==0){
							controlSample = noControlSample;
						}
						else{
							controlSample = sampleService.getSampleById(controlId);
						}
						if(testSampleControlSampleListMap.get(sample)==null){
							List<Sample> controlSampleList = new ArrayList<Sample>();
							controlSampleList.add(controlSample);
							testSampleControlSampleListMap.put(sample, controlSampleList);
							testSampleSet.add(sample);
						}
						else{
							List<Sample> controlSampleList = testSampleControlSampleListMap.get(sample);
							controlSampleList.add(controlSample);
							//testSampleControlSampleListMap.put(sample, controlSampleList);//I doubt this is needed here
						}
					}
				}
			}
			logger.debug("***************A*****");
			Map<Sample, List<String>> sampleRunInfoMap = new HashMap<Sample, List<String>>();
			Map<String, String> sampleIdControlIdCommandLine = new HashMap<String, String>();
			for(Sample sample : testSampleSet){
				logger.debug("***************A1***");
				for(SampleMeta sm : sample.getSampleMeta()){logger.debug("***************A2***");
					if(sm.getK().startsWith("chipseqAnalysis.testCellLibraryIdList::")){logger.debug("***************A3");
						if(sampleRunInfoMap.containsKey(sample)){logger.debug("***************A4");
							continue;
						}
						String cellLibraryIdListAsString = sm.getV();logger.debug("***************A5");
						List<Integer> cellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIdListAsString);logger.debug("***************A6");
						List<String> runInfo = new ArrayList<String>();
						for(Integer cellLibraryId : cellLibraryIdList){logger.debug("***************A7");
							SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);logger.debug("***************A8");
							Sample library = sampleService.getLibrary(cellLibrary);logger.debug("***************A9");
							Sample cell = sampleService.getCell(cellLibrary);logger.debug("***************A10");
							String lane = "(lane " + sampleService.getCellIndex(cell).toString() + ")";logger.debug("***************A11");
							Sample platformUnit = sampleService.getPlatformUnitForCell(cell);logger.debug("***************A12");
							Run run = sampleService.getCurrentRunForPlatformUnit(platformUnit);logger.debug("***************A13");
							if(run==null || run.getId()==null){//fix other places too (below) if you make a change here
								runInfo.add(library.getName() + " [" + platformUnit.getName() + " " + lane + "]"); logger.debug("***************A14");
							}
							else{
								runInfo.add(library.getName() + " [" + run.getName() + " " + lane + "]");logger.debug("***************A14");
							}
						}
						sampleRunInfoMap.put(sample, runInfo);logger.debug("***************A15");
					}
					if(sm.getK().startsWith("chipseqAnalysis.controlCellLibraryIdList::")){logger.debug("***************A4");
						String[] splitK = sm.getK().split("::");
						String controlIdAsString = splitK[1];
						if(controlIdAsString.equalsIgnoreCase("0")){//there was no control, and corresponding sm.getV() is empty
							continue;
						}
						Integer controlId = Integer.parseInt(splitK[1]);
						Sample controlSample = sampleService.getSampleById(controlId);
						if(sampleRunInfoMap.containsKey(controlSample)){
							continue;
						}
						String cellLibraryIdListAsString = sm.getV();
						List<Integer> cellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIdListAsString);
						List<String> runInfo = new ArrayList<String>();
						for(Integer cellLibraryId : cellLibraryIdList){
							SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
							Sample library = sampleService.getLibrary(cellLibrary);
							Sample cell = sampleService.getCell(cellLibrary);
							String lane = "(lane " + sampleService.getCellIndex(cell).toString() + ")";
							Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
							Run run = sampleService.getCurrentRunForPlatformUnit(platformUnit);
							if(run==null || run.getId()==null){//fix other places (above) too if you make a change here
								runInfo.add(library.getName() + " [" + platformUnit.getName() + " " + lane + "]");logger.debug("***************A14");
							}
							else{
								runInfo.add(library.getName() + " [" + run.getName() + " " + lane + "]");logger.debug("***************A14");
							}
						}
						sampleRunInfoMap.put(controlSample, runInfo);				
					}
					
					if(sm.getK().startsWith("chipseqAnalysis.commandLineCall::")){logger.debug("***************A5");
						String[] splitK = sm.getK().split("::");
						String controlIdAsString = splitK[1];
						sampleIdControlIdCommandLine.put(sample.getId().toString() + "::" + controlIdAsString, sm.getV());
					}				
				}
			}
			logger.debug("***************B");
			//get the fileGroups for each testSample
			Map<String, List<FileGroup>> sampleIdControlIdFileGroupListMap = new HashMap<String, List<FileGroup>>();
			Set<FileType> fileTypeSet = new HashSet<FileType>();
			List<FileType> fileTypeList = new ArrayList<FileType>();
			logger.debug("***************C");
			for(Sample sample : testSampleSet){
				for(FileGroup fg : sample.getFileGroups()){
					for(FileGroupMeta fgm : fg.getFileGroupMeta()){
						if(fgm.getK().equalsIgnoreCase("chipseqAnalysis.controlId")){
							if(sampleIdControlIdFileGroupListMap.containsKey(sample.getId()+"::"+fgm.getV())){
								sampleIdControlIdFileGroupListMap.get(sample.getId().toString()+"::"+fgm.getV()).add(fg);
							}
							else{
								List<FileGroup> fileGroupList = new ArrayList<FileGroup>();
								fileGroupList.add(fg);
								sampleIdControlIdFileGroupListMap.put(sample.getId().toString()+"::"+fgm.getV(), fileGroupList);
							}
							FileHandle fileHandle = new ArrayList<FileHandle>(fg.getFileHandles()).get(0);
							fileTypeSet.add(fg.getFileType());
							break;//from filegroupmeta for loop
						}
					}
				}
			}
			logger.debug("***************D");
			fileTypeList.addAll(fileTypeSet);
			class FileTypeComparator implements Comparator<FileType> {
			    @Override
			    public int compare(FileType arg0, FileType arg1) {
			        return arg0.getIName().compareToIgnoreCase(arg1.getIName());
			    }
			}
			Collections.sort(fileTypeList, new FileTypeComparator());

			logger.debug("***************E");
			class FileGroupComparator implements Comparator<FileGroup> {
			    @Override
			    public int compare(FileGroup arg0, FileGroup arg1) {
			        return arg0.getFileType().getIName().compareToIgnoreCase(arg1.getFileType().getIName());
			    }
			}
			logger.debug("***************F");
			//check AND order the filegroups by filetype.iname
			for(Sample sample : testSampleControlSampleListMap.keySet()){
				List<Sample> controlSampleList = testSampleControlSampleListMap.get(sample);
				for(Sample controlSample : controlSampleList){
					Assert.assertTrue(sampleIdControlIdFileGroupListMap.containsKey(sample.getId().toString()+"::"+controlSample.getId().toString()));
					Collections.sort(sampleIdControlIdFileGroupListMap.get(sample.getId().toString()+"::"+controlSample.getId().toString()), new FileGroupComparator());
				}
			}
			
			logger.debug("***************G");
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();logger.debug("***************1");


			PanelTab panelTab = ChipSeqWebPanels.getSummaryPanelTab(jobStatus, job, strategy, softwareName);
			panelTabSet.add(panelTab);logger.debug("***************11");

			if(jobStatus.name().equals(Status.COMPLETED)){
				//do the other panels
			}
			logger.debug("***************ending chipseqService.getChipSeqDataToDisplay(job)");

			
			return panelTabSet;
			
		}catch(Exception e){logger.debug("***************EXCEPTION IN chipseqService.getChipSeqDataToDisplay(job): "+ e.getStackTrace());throw new PanelException(e.getMessage());}
	}
	
}
