/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.macstwo.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Strategy;
//import edu.yu.einstein.wasp.chipseq.webpanels.ChipSeqWebPanels;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.service.MacstwoService;
import edu.yu.einstein.wasp.macstwo.webpanels.MacstwoWebPanels;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;

import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Service
@Transactional("entityManager")
public class MacstwoServiceImpl extends WaspServiceImpl implements MacstwoService {
	
	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileUrlResolver fileUrlResolver;
	@Autowired
	private RunService runService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public Set<PanelTab> getMacstwoDataToDisplay(Job job)throws PanelException{
		 
		 try{
			 //First, Assemble The Data (second, get the panelTabs)			
			//samplePairs (for the samplePairsTab)
			Map<Sample, List<Sample>> testSampleControlSampleListMap = getTestSampleControlSampleListMap(job);			
			List<Sample> testSampleList = new ArrayList<Sample>();//will basically function as a set, with each sample in this list being unique
			for(Sample sample : testSampleControlSampleListMap.keySet()){
				testSampleList.add(sample);
			}
			
			class SampleNameComparator implements Comparator<Sample> {
			    @Override
			    public int compare(Sample arg0, Sample arg1) {
			        return arg0.getName().compareToIgnoreCase(arg1.getName());
			    }
			}
			Collections.sort(testSampleList, new SampleNameComparator());
			
			for(Sample testSample : testSampleList){//sort each ControlSampleList
				Collections.sort(testSampleControlSampleListMap.get(testSample), new SampleNameComparator());
			}
			
			//commandLine (for the samplePairsTab)
			Map<String, String> sampleIdControlIdCommandLineMap = getSampleIdControlIdCommandLineMap(testSampleList);
			
			//sample library runs (for the runs tab)
			Map<Sample, List<SampleSource>> sampleCellLibraryListMap = getSampleCellLibraryListMap(testSampleList);
			Map<Sample,List<Sample>> sampleLibraryListMap = getSampleLibraryListMap(sampleCellLibraryListMap);
			for(Sample testSample : testSampleList){
				if(sampleLibraryListMap.containsKey(testSample)){//sort each LibraryList
					Collections.sort(sampleLibraryListMap.get(testSample), new SampleNameComparator());
				}
			}
			//Map<Sample, List<String>> libraryRunInfoListMap = new HashMap<Sample, List<String>>();//used for Runs
			Map<Sample, List<String>> libraryRunInfoListMap = getLibraryRunInfoListMap(sampleCellLibraryListMap);
			/*
			Map<Sample,List<Sample>> sampleLibraryListMap = new HashMap<Sample, List<Sample>>();//used for Runs
			Map<Sample, List<String>> libraryRunInfoListMap = new HashMap<Sample, List<String>>();//used for Runs

			for(Sample testSample : testSampleList){
				logger.debug("***************A1***");
				for(SampleMeta sm : testSample.getSampleMeta()){logger.debug("***************A2***");
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
			*/
			
			
			

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
			
			//Second, assemble the data within ordered set of panel tabs
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();logger.debug("***************1");


			/////////////summary already done by chipseq or core: PanelTab summaryPanelTab = ChipSeqWebPanels.getSummaryPanelTab2222(jobStatus, job, strategy, softwareName);
			///////////////panelTabSet.add(summaryPanelTab);logger.debug("***************11");
			//TODO: uncomment if(jobStatus.toString().equals(Status.COMPLETED.toString())){
			//do the other panels //
				PanelTab samplePairsPanelTab = MacstwoWebPanels.getSamplePairs(testSampleList, testSampleControlSampleListMap, sampleIdControlIdCommandLineMap);
				if(samplePairsPanelTab!=null){panelTabSet.add(samplePairsPanelTab);}
				PanelTab sampleLibraryRunsPanelTab = MacstwoWebPanels.getSampleLibraryRuns(testSampleList, sampleLibraryListMap, libraryRunInfoListMap);
				if(sampleLibraryRunsPanelTab!=null){panelTabSet.add(sampleLibraryRunsPanelTab);}
				PanelTab fileTypeDefinitionsPanelTab = MacstwoWebPanels.getFileTypeDefinitions(fileTypeList);
				if(fileTypeDefinitionsPanelTab!=null){panelTabSet.add(fileTypeDefinitionsPanelTab);}
				PanelTab allFilesDisplayedBySampleUsingGroupingGridPanelTab = MacstwoWebPanels.getFilesBySample(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allFilesDisplayedBySampleUsingGroupingGridPanelTab!=null){panelTabSet.add(allFilesDisplayedBySampleUsingGroupingGridPanelTab);}
				PanelTab allFilesDisplayedByFileTypeUsingGroupingGridPanelTab = MacstwoWebPanels.getFilesByFileType(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allFilesDisplayedByFileTypeUsingGroupingGridPanelTab!=null){panelTabSet.add(allFilesDisplayedByFileTypeUsingGroupingGridPanelTab);}
				
				
				PanelTab allModelPNGFilesDisplayedInPanelsTab = MacstwoWebPanels.getModelImages(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
				if(allModelPNGFilesDisplayedInPanelsTab!=null){panelTabSet.add(allModelPNGFilesDisplayedInPanelsTab);}

			
			return panelTabSet;
			
		}catch(Exception e){
			logger.debug("exception in macstwoService.getChipSeqDataToDisplay(job): "+ e.getStackTrace());
			throw new PanelException(e.getMessage());
		}		
	}

	private Map<Sample, List<Sample>> getTestSampleControlSampleListMap(Job job){
		
		Map<Sample, List<Sample>> testSampleControlSampleListMap = new HashMap<Sample, List<Sample>>();
		
		Sample noControlSample = new Sample();
		noControlSample.setId(0);
		noControlSample.setName("None");
		
		for(Sample sample : job.getSample()){
			for(SampleMeta sm : sample.getSampleMeta()){
				if(sm.getK().startsWith("chipseqAnalysis.controlId::")){//there exists at least one chipseq analysis for this sample, but could be more than one
					if(!testSampleControlSampleListMap.containsKey(sample)){
						testSampleControlSampleListMap.put(sample, new ArrayList<Sample>());
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
		return testSampleControlSampleListMap;
	}
	
	private Map<String, String> getSampleIdControlIdCommandLineMap(List<Sample> testSampleList){

		Map<String, String> sampleIdControlIdCommandLineMap = new HashMap<String, String>();		
		for(Sample testSample : testSampleList){			
			for(SampleMeta sm : testSample.getSampleMeta()){
				if(sm.getK().startsWith("chipseqAnalysis.commandLineCall::")){//capture for each distinct chipseq analysis
					String[] splitK = sm.getK().split("::");
					String controlIdAsString = splitK[1];
					sampleIdControlIdCommandLineMap.put(testSample.getId().toString() + "::" + controlIdAsString, sm.getV());
				}
			}
		}
		return sampleIdControlIdCommandLineMap;
	}
	
	private Map<Sample, List<SampleSource>> getSampleCellLibraryListMap(List<Sample> testSampleList)throws SampleTypeException{
		
		Map<Sample, List<SampleSource>> sampleCellLibraryListMap = new HashMap<Sample, List<SampleSource>>();
		for(Sample testSample : testSampleList){
			for(SampleMeta sm : testSample.getSampleMeta()){
				if(sm.getK().startsWith("chipseqAnalysis.testCellLibraryIdList::")){//only need to capture once for each test sample
					if(sampleCellLibraryListMap.containsKey(testSample)){//we already obtained this info; no need to repeat, as the info will be the same
						continue;
					}
					String cellLibraryIdListAsString = sm.getV();
					List<Integer> cellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIdListAsString);
					sampleCellLibraryListMap.put(testSample, new ArrayList<SampleSource>());
					for(Integer cellLibraryId : cellLibraryIdList){
						SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
						sampleCellLibraryListMap.get(testSample).add(cellLibrary);						
					}
				}
			}
		}	
		return sampleCellLibraryListMap;
	}
	
	private Map<Sample,List<Sample>>  getSampleLibraryListMap(Map<Sample, List<SampleSource>> sampleCellLibraryListMap){
		Map<Sample,List<Sample>> sampleLibraryListMap = new HashMap<Sample,List<Sample>>();
		for(Sample sample : sampleCellLibraryListMap.keySet()){
			List<SampleSource> cellLibraryList = sampleCellLibraryListMap.get(sample);
			sampleLibraryListMap.put(sample, new ArrayList<Sample>());
			
			for(SampleSource cellLibrary : cellLibraryList){
				Sample library = sampleService.getLibrary(cellLibrary);				
				if(!sampleLibraryListMap.get(sample).contains(library)){//the LibraryList is acting as a set
					sampleLibraryListMap.get(sample).add(library);
				}
			}
		}
		return sampleLibraryListMap;
	}
	
	private Map<Sample, List<String>> getLibraryRunInfoListMap(Map<Sample, List<SampleSource>> sampleCellLibraryListMap)throws SampleTypeException, SampleParentChildException{
		Map<Sample, List<String>> libraryRunInfoListMap = new HashMap<Sample, List<String>>();
		for(Sample sample : sampleCellLibraryListMap.keySet()){
			List<SampleSource> cellLibraryList = sampleCellLibraryListMap.get(sample);
			for(SampleSource cellLibrary : cellLibraryList){
				Sample library = sampleService.getLibrary(cellLibrary);
				if(!libraryRunInfoListMap.containsKey(library)){
					libraryRunInfoListMap.put(library, new ArrayList<String>());
				}
				Sample cell = sampleService.getCell(cellLibrary);
				String lane = sampleService.getCellIndex(cell).toString(); 
				Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
				List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);//WHY IS THIS A LIST rather than a singleton?
				if(runList.isEmpty()){//fix other places too (below) if you make a change here
					libraryRunInfoListMap.get(library).add("Lane " + lane + ": " + platformUnit.getName()); 
				}
				else{
					libraryRunInfoListMap.get(library).add("Lane " + lane + ": " + runList.get(0).getName()); 
				}				
			}
		}
		return libraryRunInfoListMap;
	}
}
