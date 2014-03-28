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
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
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
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.viewpanel.Content;
import edu.yu.einstein.wasp.viewpanel.DataTabViewing.Status;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;
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
	@Autowired
	private FileUrlResolver fileUrlResolver;
	@Autowired
	private RunService runService;

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public JobDataTabViewing getPeakcallerPlugin(Job job) throws SoftwareConfigurationException{
			WaspJobContext waspJobContext;
			try{
				waspJobContext = new WaspJobContext(job.getId(), jobService);
			}catch(Exception e){ 
				throw new SoftwareConfigurationException(e.getMessage());
			}
			SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(peakcallerResourceType);
			if (softwareConfig == null){
				throw new SoftwareConfigurationException("No software could be configured for jobId=" + job.getId() + " with resourceType iname=" + peakcallerResourceType.getIName());
			}
			return waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), JobDataTabViewing.class);				
	}

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
			//for the SummaryTab (job, jobStatus, strategy, softwareName)
			Strategy strategy = jobService.getStrategy(Strategy.StrategyType.LIBRARY_STRATEGY, job);			
			WaspJobContext waspJobContext = new WaspJobContext(jobId, jobService);
			logger.debug("***************just after a");
			if(peakcallerResourceType==null){
				logger.debug("***************autowired peakcallerResourceType is null");
			}
			else{
				logger.debug("***************autowired peakcallerResourceType: name = " + peakcallerResourceType.getName());
				logger.debug("***************autowired peakcallerResourceType: Iname = " + peakcallerResourceType.getIName());
			}
			SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(peakcallerResourceType);
			if (softwareConfig == null){
				throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + peakcallerResourceType.getIName());
			}
			BatchJobProviding softwarePlugin_batchJobProviding = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), BatchJobProviding.class);
			JobDataTabViewing softwarePlugin_JobDataTabViewing = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), JobDataTabViewing.class);
			//String softwareName = softwarePlugin.getName();//macsTwo; softwarePlugin.getIName() is macstwo
			String softwareName = softwareDao.getSoftwareByIName(softwarePlugin_batchJobProviding.getIName()).getName();//should get "MACS2 Peakcaller"
			
			//samplePairs 			
			Sample noControlSample = new Sample();
			noControlSample.setId(0);
			noControlSample.setName("None");
			
			Map<Sample, List<Sample>> testSampleControlSampleListMap = new HashMap<Sample, List<Sample>>();
			List<Sample> testSampleList = new ArrayList<Sample>();//will basically function as a set, with each sample in this list being unique
			
			logger.debug("***************b");
			for(Sample sample : job.getSample()){logger.debug("***************c");
				for(SampleMeta sm : sample.getSampleMeta()){logger.debug("***************d");
					if(sm.getK().startsWith("chipseqAnalysis.controlId::")){//there exists at least one chipseq analysis for this sample, but could be more than one
						if(!testSampleControlSampleListMap.containsKey(sample)){
							testSampleControlSampleListMap.put(sample, new ArrayList<Sample>());
							testSampleList.add(sample);
						}
						Sample controlSample = null;
						Integer controlId = Integer.parseInt(sm.getV());
						if(controlId.intValue()==0){
							controlSample = noControlSample;
						}
						else{
							controlSample = sampleService.getSampleById(controlId);
						}
						testSampleControlSampleListMap.get(sample).add(controlSample);
					}
				}
			}
			logger.debug("***************e");
			
			class SampleNameComparator implements Comparator<Sample> {
			    @Override
			    public int compare(Sample arg0, Sample arg1) {
			        return arg0.getName().compareToIgnoreCase(arg1.getName());
			    }
			}
			Collections.sort(testSampleList, new SampleNameComparator());
			
			logger.debug("***************f");
			for(Sample testSample : testSampleList){//sort each ControlSampleList
				Collections.sort(testSampleControlSampleListMap.get(testSample), new SampleNameComparator());
			}
			logger.debug("***************g");
			
			logger.debug("***************A*****");
			//sample library runs (for the runs tab) and commandLine (for the pairs tab)
			Map<String, String> sampleIdControlIdCommandLineMap = new HashMap<String, String>();//used for Pairs
			Map<Sample,List<Sample>> sampleLibraryListMap = new HashMap<Sample, List<Sample>>();//used for Runs
			Map<Sample, List<String>> libraryRunInfoListMap = new HashMap<Sample, List<String>>();//used for Runs

			for(Sample testSample : testSampleList){
				logger.debug("***************A1***");
				for(SampleMeta sm : testSample.getSampleMeta()){logger.debug("***************A2***");
					if(sm.getK().startsWith("chipseqAnalysis.commandLineCall::")){//capture for each distinct chipseq analysis
						String[] splitK = sm.getK().split("::");
						String controlIdAsString = splitK[1];
						sampleIdControlIdCommandLineMap.put(testSample.getId().toString() + "::" + controlIdAsString, sm.getV());
					}				
					if(sm.getK().startsWith("chipseqAnalysis.testCellLibraryIdList::")){//only need to capture once for each test sample
						if(sampleLibraryListMap.containsKey(testSample)){//we already obtained this info; no need to repeat, as the info will be the same
							continue;
						}
						String cellLibraryIdListAsString = sm.getV();logger.debug("***************A5");
						List<Integer> cellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIdListAsString);logger.debug("***************A6");
												
						sampleLibraryListMap.put(testSample, new ArrayList<Sample>());
						
						for(Integer cellLibraryId : cellLibraryIdList){logger.debug("***************A7");
							SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);logger.debug("***************A8");
							Sample library = sampleService.getLibrary(cellLibrary);logger.debug("***************A9");
							
							if(!sampleLibraryListMap.get(testSample).contains(library)){//the LibraryList is acting as a set
								sampleLibraryListMap.get(testSample).add(library);
								libraryRunInfoListMap.put(library, new ArrayList<String>());
							}
							Sample cell = sampleService.getCell(cellLibrary);logger.debug("***************A10");
							String lane = sampleService.getCellIndex(cell).toString(); logger.debug("***************A11");
							Sample platformUnit = sampleService.getPlatformUnitForCell(cell);logger.debug("***************A12");
							List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);//WHY IS THIS A LIST rather than a singleton?
							if(runList.isEmpty()){//fix other places too (below) if you make a change here
								libraryRunInfoListMap.get(library).add("Lane " + lane + ": " + platformUnit.getName()); logger.debug("***************A14.5");
							}
							else{
								libraryRunInfoListMap.get(library).add("Lane " + lane + ": " + runList.get(0).getName()); logger.debug("***************A14.5");
							}
						}
						logger.debug("***************A15");
					}
	
				}
			}
			for(Sample testSample : testSampleList){
				if(sampleLibraryListMap.containsKey(testSample)){//sort each LibraryList
					Collections.sort(sampleLibraryListMap.get(testSample), new SampleNameComparator());
				}
			}

			logger.debug("***************B");
			//get the fileGroups for each testSample
			//Map<String, List<FileGroup>> sampleIdControlIdFileGroupListMap = new HashMap<String, List<FileGroup>>();
			Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap = new HashMap<String, FileGroup>();
			Map<String, FileHandle> sampleIdControlIdFileTypeIdFileHandleMap = new HashMap<String, FileHandle>();
			Map<FileHandle, String> fileHandleResolvedURLMap = new HashMap<FileHandle, String>();

			Set<FileType> fileTypeSet = new HashSet<FileType>();
			List<FileType> fileTypeList = new ArrayList<FileType>();
			logger.debug("***************C");
			for(Sample testSample : testSampleList){
				for(FileGroup fg : testSample.getFileGroups()){
					for(FileGroupMeta fgm : fg.getFileGroupMeta()){
						if(fgm.getK().equalsIgnoreCase("chipseqAnalysis.controlId")){
							String controlId = fgm.getV();
							fileTypeSet.add(fg.getFileType());
							String fileTypeId = fg.getFileType().getId().toString();
							String sampleIdControlIdFileTypeIdKey = testSample.getId().toString()+"::"+controlId+"::"+fileTypeId;
							
							//if(!sampleIdControlIdFileGroupListMap.containsKey(testSample.getId()+"::"+controlId)){
							//	sampleIdControlIdFileGroupListMap.put(testSample.getId().toString()+"::"+controlId, new ArrayList<FileGroup>());
							//}							
							//sampleIdControlIdFileGroupListMap.get(testSample.getId().toString()+"::"+controlId).add(fg);
							FileHandle fileHandle = new ArrayList<FileHandle>(fg.getFileHandles()).get(0);
							String resolvedURL = null;
							try{
								resolvedURL = fileUrlResolver.getURL(fileHandle).toString();
							}catch(Exception e){logger.debug("***************UNABLE TO RESOLVE URL for file: " + fileHandle.getFileName());}
							
							logger.debug("***************resolvedURL: " + resolvedURL + " for fileHandle " + fileHandle.getFileName());
							
							fileHandleResolvedURLMap.put(fileHandle, resolvedURL);
							sampleIdControlIdFileTypeIdFileGroupMap.put(sampleIdControlIdFileTypeIdKey, fg);
							sampleIdControlIdFileTypeIdFileHandleMap.put(sampleIdControlIdFileTypeIdKey, fileHandle);
							
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
					//Assert.assertTrue(sampleIdControlIdFileGroupListMap.containsKey(sample.getId().toString()+"::"+controlSample.getId().toString()));
					//Collections.sort(sampleIdControlIdFileGroupListMap.get(sample.getId().toString()+"::"+controlSample.getId().toString()), new FileGroupComparator());
				}
			}
			
			logger.debug("***************G");
			/////////softwarePlugin_JobDataTabViewing.getViewPanelTabs(job);
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();logger.debug("***************1");


			PanelTab summaryPanelTab = ChipSeqWebPanels.getSummaryPanelTab2222(jobStatus, job, strategy, softwareName);
			panelTabSet.add(summaryPanelTab);logger.debug("***************11");
			//TODO: uncomment if(jobStatus.toString().equals(Status.COMPLETED.toString())){
			//do the other panels //
				PanelTab samplePairsPanelTab = ChipSeqWebPanels.getSamplePairsPanelTab2222(testSampleList, testSampleControlSampleListMap, sampleIdControlIdCommandLineMap);
				if(samplePairsPanelTab!=null){panelTabSet.add(samplePairsPanelTab);}
				PanelTab sampleLibraryRunsPanelTab = ChipSeqWebPanels.getSampleLibraryRunsPanelTab2222(testSampleList, sampleLibraryListMap, libraryRunInfoListMap);
				if(sampleLibraryRunsPanelTab!=null){panelTabSet.add(sampleLibraryRunsPanelTab);}
				PanelTab fileTypeDefinitionsPanelTab = ChipSeqWebPanels.getFileTypeDefinitionsPanelTab2222(fileTypeList);
				if(fileTypeDefinitionsPanelTab!=null){panelTabSet.add(fileTypeDefinitionsPanelTab);}
				PanelTab allFilesDisplayedBySampleUsingGroupingGridPanelTab = ChipSeqWebPanels.getAllFilesDisplayedBySampleUsingGroupingGridPanelTab2222(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allFilesDisplayedBySampleUsingGroupingGridPanelTab!=null){panelTabSet.add(allFilesDisplayedBySampleUsingGroupingGridPanelTab);}
				PanelTab allFilesDisplayedByFileTypeUsingGroupingGridPanelTab = ChipSeqWebPanels.getAllFilesDisplayedByFileTypeUsingGroupingGridPanelTab2222(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allFilesDisplayedByFileTypeUsingGroupingGridPanelTab!=null){panelTabSet.add(allFilesDisplayedByFileTypeUsingGroupingGridPanelTab);}
				
				//PanelTab summaryPanelTab123 = ChipSeqWebPanels.getSummaryPanelTab(jobStatus, job, strategy, softwareName);
				//panelTabSet.add(summaryPanelTab123);
				
				PanelTab allModelPNGFilesDisplayedInPanelsTab = ChipSeqWebPanels.getAllModelPNGFilesDisplayedInPanelsTab2222(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allModelPNGFilesDisplayedInPanelsTab!=null){panelTabSet.add(allModelPNGFilesDisplayedInPanelsTab);}

/*			
  			PanelTab summaryPanelTab = ChipSeqWebPanels.getSummaryPanelTab(jobStatus, job, strategy, softwareName);
			panelTabSet.add(summaryPanelTab);logger.debug("***************11");

			PanelTab testPanelTabWithIFrames = ChipSeqWebPanels.getTestPanelTab();
			if(testPanelTabWithIFrames!=null){panelTabSet.add(testPanelTabWithIFrames);}


			//TODO: uncomment if(jobStatus.toString().equals(Status.COMPLETED.toString())){
				logger.debug("***************jobStatus is COMPLETED, so we enter this loop");
				//do the other panels //
				PanelTab samplePairsPanelTab = ChipSeqWebPanels.getSamplePairsPanelTab(testSampleList, testSampleControlSampleListMap, sampleIdControlIdCommandLineMap);
				if(samplePairsPanelTab!=null){panelTabSet.add(samplePairsPanelTab);}
				PanelTab sampleLibraryRunsPanelTab = ChipSeqWebPanels.getSampleLibraryRunsPanelTab(testSampleList, sampleLibraryListMap, libraryRunInfoListMap);
				if(sampleLibraryRunsPanelTab!=null){panelTabSet.add(sampleLibraryRunsPanelTab);}
				
				PanelTab fileTypeDefinitionsPanelTab = ChipSeqWebPanels.getFileTypeDefinitionsPanelTab(fileTypeList);
				if(fileTypeDefinitionsPanelTab!=null){panelTabSet.add(fileTypeDefinitionsPanelTab);}
				
				PanelTab allFilesDisplayedBySamplePanelTab = ChipSeqWebPanels.getAllFilesDisplayedBySamplePanelTab(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allFilesDisplayedBySamplePanelTab!=null){panelTabSet.add(allFilesDisplayedBySamplePanelTab);}
				PanelTab allFilesDisplayedByFileTypePanelTab = ChipSeqWebPanels.getAllFilesDisplayedByFileTypePanelTab(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allFilesDisplayedByFileTypePanelTab!=null){panelTabSet.add(allFilesDisplayedByFileTypePanelTab);}
				
				PanelTab allFilesDisplayedBySampleUsingGroupingGridPanelTab = ChipSeqWebPanels.getAllFilesDisplayedBySampleUsingGroupingGridPanelTab(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allFilesDisplayedBySampleUsingGroupingGridPanelTab!=null){panelTabSet.add(allFilesDisplayedBySampleUsingGroupingGridPanelTab);}
				PanelTab allFilesDisplayedByFileTypeUsingGroupingGridPanelTab = ChipSeqWebPanels.getAllFilesDisplayedByFileTypeUsingGroupingGridPanelTab(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allFilesDisplayedByFileTypeUsingGroupingGridPanelTab!=null){panelTabSet.add(allFilesDisplayedByFileTypeUsingGroupingGridPanelTab);}
*/
				
				

				
				
				/*
				if(!fileTypeList.isEmpty()){
					
					PanelTab fileTypeDefinitionsPanelTab = ChipSeqWebPanels.getFileTypeDefinitionsPanelTab(fileTypeList);
					if(fileTypeDefinitionsPanelTab!=null){panelTabSet.add(fileTypeDefinitionsPanelTab);}
					
					//int counter = 0;
					for(FileType fileType : fileTypeList){
						//if(counter==0){
						PanelTab filePanelTab = ChipSeqWebPanels.getFilePanelTab(testSampleList, testSampleControlSampleListMap, fileType, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
						if(filePanelTab!=null){panelTabSet.add(filePanelTab);}
						//}
						//counter++;
					}
				}
				*/
			//}
			
			logger.debug("***************ending chipseqService.getChipSeqDataToDisplay(job)");

			
			return panelTabSet;
			
		}catch(Exception e){logger.debug("***************EXCEPTION IN chipseqService.getChipSeqDataToDisplay(job): "+ e.getStackTrace());throw new PanelException(e.getMessage());}
	}
	
}
