/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.macstwo.service.impl;

import java.text.DecimalFormat;
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

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
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
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
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
	@Autowired
	private FileType macs2AnalysisFileType;
	@Autowired
	private SoftwareService softwareService;
	@Autowired
	private GenomeService genomeService;
	
	public class SampleNameComparator implements Comparator<Sample> {
	    @Override
	    public int compare(Sample arg0, Sample arg1) {
	        return arg0.getName().compareToIgnoreCase(arg1.getName());
	    }
	}
	
	public class FileTypeComparator implements Comparator<FileType> {
	    @Override
	    public int compare(FileType arg0, FileType arg1) {
	        return arg0.getIName().compareToIgnoreCase(arg1.getIName());
	    }
	}
	
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
			 //First, assemble the data
			 Map<FileGroup, Sample> fileGroupTestSampleMap = new HashMap<FileGroup, Sample>();
			 Map<FileGroup, Sample> fileGroupControlSampleMap = new HashMap<FileGroup, Sample>();
			 Map<FileGroup, String> fileGroupCommandLineMap = new HashMap<FileGroup, String>();
			 Map<FileGroup, List<Sample>> fileGroupLibraryListMap = new HashMap<FileGroup, List<Sample>>();
			 Map<FileGroup, List<FileHandle>> fileGroupBamFilesUsedMap = new HashMap<FileGroup, List<FileHandle>>();
			 Map<FileGroup, List<Software>> fileGroupSoftwareUsedMap = new HashMap<FileGroup, List<Software>>();
			 
			 Map<FileGroup, Double> fileGroupFripMap = new HashMap<FileGroup, Double>();
			 Map<FileGroup, String> fileGroupFripCalculationMap = new HashMap<FileGroup, String>();
			 Map<FileGroup, List<FileHandle>> fileGroupFileHandleListMap = new HashMap<FileGroup, List<FileHandle>>();
			 Map<FileGroup, Build> fileGroupBuildMap = new HashMap<FileGroup, Build>();
			 Map<FileHandle, String> fileHandleResolvedURLMap = new HashMap<FileHandle, String>();
			 Set<FileType> fileTypeSet = new HashSet<FileType>();			 

			 List<FileGroup> macs2AnalysisFileGroupList = getMacs2AnalysisFileGroups(job);
			 class FileGroupDescriptionComparator implements Comparator<FileGroup> {
				 @Override
				 public int compare(FileGroup arg0, FileGroup arg1) {
					 return arg0.getDescription().compareToIgnoreCase(arg1.getDescription());//base name
				 }
			 }
			 Collections.sort(macs2AnalysisFileGroupList, new FileGroupDescriptionComparator());//sorted by description (base name)
			 
			 for(FileGroup fileGroup : macs2AnalysisFileGroupList){
				 
				 Set<Sample> testAndControlSamples = getTestAndControlSamples(fileGroup);	
				 Sample test = getTestSample(testAndControlSamples);//getTestSample(fileGroup);
				 if(test!=null){
					 fileGroupTestSampleMap.put(fileGroup, test);
				 }
				 Sample control = getControlSample(testAndControlSamples);//getControlSample(fileGroup);
				 if(control!=null){
					 fileGroupControlSampleMap.put(fileGroup, control);
				 } 
				 String commandLineCalls = getCommandLineCalls(fileGroup);
				 fileGroupCommandLineMap.put(fileGroup, commandLineCalls);
				 List<Software> softwareUsed = getSoftwareUsedInAnalysis(fileGroup);
				 fileGroupSoftwareUsedMap.put(fileGroup, softwareUsed);
				 
				 List<Sample> librariesUsed = getLibrariesUsedInAnalysis(fileGroup);
				 fileGroupLibraryListMap.put(fileGroup, librariesUsed);
				 List<FileHandle> bamFilesUsed = getBamFilesUsedInAnalysis(fileGroup);
				 fileGroupBamFilesUsedMap.put(fileGroup, bamFilesUsed);				 
				 
				 //deal with frip (metadata of fileGroup)
				 Double frip = getFrip(fileGroup);
				 if(frip!=null){
					 fileGroupFripMap.put(fileGroup, frip);
				 }
				 String fripCalculation = getFripPercentCalculation(fileGroup);
				 if(fripCalculation!=null){
					 fileGroupFripCalculationMap.put(fileGroup, fripCalculation);
				 }
				 
				//7-23-14
				 //genomeBrowser
				 Build build = genomeService.getBuild(test);
				 //logger.debug("--SAMPLE name: " + test.getName());
				 //logger.debug("---BUILD name: " + build.getName());//such as      BUILD name: 74
				 //logger.debug("----GENOME name: " + build.getGenome().getName());//such as       GENOME name: GRCm38
				 //logger.debug("-----ORGANISM name: " + build.getGenome().getOrganism().getName());//such as      ORGANISM name: Mus musculus
				 fileGroupBuildMap.put(fileGroup, build);
				 
				 //deal with list of fileHandles for this fileGroup
				 List<FileHandle> fileHandleList = new ArrayList<FileHandle>(fileGroup.getFileHandles());
				 class FileHandleViaFileTypeComparator implements Comparator<FileHandle> {
					 @Override
					 public int compare(FileHandle arg0, FileHandle arg1) {
						 return arg0.getFileType().getIName().compareToIgnoreCase(arg1.getFileType().getIName());
					 }
				 }				 
				 Collections.sort(fileHandleList, new FileHandleViaFileTypeComparator());
				 fileGroupFileHandleListMap.put(fileGroup, fileHandleList);
				 
				 //get SET of filetypes (and convert later to list, so it can be sorted)
				 //get resolvedURL for each fileHandle
				 for(FileHandle fileHandle : fileHandleList){
					fileTypeSet.add(fileHandle.getFileType());
					String resolvedURL = "";
					try{
						resolvedURL = fileUrlResolver.getURL(fileHandle).toString();
					}catch(Exception e){logger.debug("***************UNABLE TO RESOLVE URL for file: " + fileHandle.getFileName());}						
					fileHandleResolvedURLMap.put(fileHandle, resolvedURL);
				 }
				 
			 }
			 List<FileType> fileTypeList = new ArrayList<FileType>(fileTypeSet);
			 Collections.sort(fileTypeList, new FileTypeComparator());
			 
			//SECOND, present the data within an ordered set of panel tabs (recall that the summary panel has already been taken care of)
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();
			
			PanelTab fileTypeDefinitionsPanelTab = MacstwoWebPanels.getFileTypeDefinitions(fileTypeList);
			if(fileTypeDefinitionsPanelTab!=null){panelTabSet.add(fileTypeDefinitionsPanelTab);}
			
			PanelTab samplePairsPanelTab = MacstwoWebPanels.getSamplePairsByAnalysis(macs2AnalysisFileGroupList, fileGroupTestSampleMap, fileGroupControlSampleMap);
			if(samplePairsPanelTab!=null){panelTabSet.add(samplePairsPanelTab);}			
			
			PanelTab commandPanelTab = MacstwoWebPanels.getCommandsByAnalysis(macs2AnalysisFileGroupList, fileGroupSoftwareUsedMap, fileGroupCommandLineMap);
			if(commandPanelTab!=null){panelTabSet.add(commandPanelTab);}

			PanelTab librariesAndBamFilesPanelTab = MacstwoWebPanels.getLibrariesAndBamFilesUsedByAnalysis(macs2AnalysisFileGroupList, fileGroupLibraryListMap, fileGroupBamFilesUsedMap);
			if(librariesAndBamFilesPanelTab!=null){panelTabSet.add(librariesAndBamFilesPanelTab);}

			PanelTab fripCalculationPanelTab = MacstwoWebPanels.getFripCalculationByAnalysis(macs2AnalysisFileGroupList, fileGroupFripCalculationMap);
			if(fripCalculationPanelTab!=null){panelTabSet.add(fripCalculationPanelTab);}
			
			PanelTab allFilesDisplayedByAnalysisPanelTab = MacstwoWebPanels.getFilesByAnalysis(macs2AnalysisFileGroupList, fileGroupBuildMap, fileGroupFileHandleListMap, fileHandleResolvedURLMap, fileGroupFripMap);
			if(allFilesDisplayedByAnalysisPanelTab!=null){panelTabSet.add(allFilesDisplayedByAnalysisPanelTab);}
			PanelTab allFilesDisplayedByFileTypePanelTab = MacstwoWebPanels.getFilesByFileType(macs2AnalysisFileGroupList, fileGroupBuildMap, fileGroupFileHandleListMap, fileHandleResolvedURLMap, fileTypeList);
			if(allFilesDisplayedByFileTypePanelTab!=null){panelTabSet.add(allFilesDisplayedByFileTypePanelTab);}

			PanelTab modelPNGFilesDisplayedByAnalysisPanelTab = MacstwoWebPanels.getModelPNGFilesByAnalysis(macs2AnalysisFileGroupList, fileGroupFileHandleListMap, fileHandleResolvedURLMap);
			if(modelPNGFilesDisplayedByAnalysisPanelTab!=null){panelTabSet.add(modelPNGFilesDisplayedByAnalysisPanelTab);}
			
			//Experiment:
			PanelTab browserByAnalysisPanelTab = MacstwoWebPanels.getBrowserByAnalysis(macs2AnalysisFileGroupList, fileGroupBuildMap, fileGroupFileHandleListMap, fileHandleResolvedURLMap, fileGroupFripMap);
			if(browserByAnalysisPanelTab!=null){panelTabSet.add(browserByAnalysisPanelTab);}

			
			return panelTabSet;
			
		}catch(Exception e){
			logger.debug("exception in macstwoService.getChipSeqDataToDisplay(job): "+ e.getStackTrace());
			throw new PanelException(e.getMessage());
		}		
	}

	private List<FileGroup> getMacs2AnalysisFileGroups(Job job){
		
		List<FileGroup> macs2AnalysisFileGroupList = new ArrayList<FileGroup>();
		for(Sample sample : job.getSample()){
			for(FileGroup fg : sample.getFileGroups()){
				if(fg.getFileType().getId().intValue()==macs2AnalysisFileType.getId().intValue()){
					macs2AnalysisFileGroupList.add(fg);
				}
			}
		}
		return macs2AnalysisFileGroupList;		
	}
	
	public Double getFrip(FileGroup fileGroup){		
		String mappedReadsAsString = "";
		String mappedReadsInPeaksAsString = "";
		for(FileGroupMeta fgm : fileGroup.getFileGroupMeta()){
			if(fgm.getK().equalsIgnoreCase("macs2Analysis.totalCountMappedReads")){
				mappedReadsAsString = fgm.getV();
			}
			if(fgm.getK().equalsIgnoreCase("macs2Analysis.totalCountMappedReadsInPeaksAsString")){
				mappedReadsInPeaksAsString = fgm.getV();
			}
		}
		try{
			Integer totalCountMappedReads = Integer.valueOf(mappedReadsAsString);
			Integer totalCountMappedReadsInPeaks = Integer.valueOf(mappedReadsInPeaksAsString);
			Double frip = (double) totalCountMappedReadsInPeaks / totalCountMappedReads;
			return frip;			
		}catch(Exception e){logger.debug("unable to retrieve Frip values as fileGroupMeta"); return null;}		
	}
	
	public String getFripPercentCalculation(FileGroup fileGroup){		
		String mappedReadsAsString = "";
		String mappedReadsInPeaksAsString = "";
		for(FileGroupMeta fgm : fileGroup.getFileGroupMeta()){
			if(fgm.getK().equalsIgnoreCase("macs2Analysis.totalCountMappedReads")){
				mappedReadsAsString = fgm.getV();
			}
			if(fgm.getK().equalsIgnoreCase("macs2Analysis.totalCountMappedReadsInPeaksAsString")){
				mappedReadsInPeaksAsString = fgm.getV();
			}
		}
		try{
			Integer mappedReads = Integer.valueOf(mappedReadsAsString);
			Integer mappedReadsInPeaks = Integer.valueOf(mappedReadsInPeaksAsString);
			Double frip = (double) mappedReadsInPeaks / mappedReads;
			Double fripPercent = 100 * frip;
			DecimalFormat myFormat = new DecimalFormat("0.00000");
			String formatedFripPercent = myFormat.format(fripPercent);
			return "100 * " + mappedReadsInPeaks.toString() + " / " + mappedReads + " = " + formatedFripPercent.toString();			
		}catch(Exception e){logger.debug("unable to retrieve Frip calculation values as fileGroupMeta"); return "Calculation Error";}		
	}
	private String getCommandLineCalls(FileGroup fileGroup){
		String commandLineCalls = "";		
		for(FileGroupMeta fgm : fileGroup.getFileGroupMeta()){
			if(fgm.getK().equalsIgnoreCase("macs2Analysis.commandLineCall")){
				commandLineCalls = fgm.getV();
				break;
			}
		}
		return commandLineCalls;
	}
	
	private Sample getTestSample(Set<Sample> sampleSet){		
		for(Sample s : sampleSet){
			for(SampleMeta sm : s.getSampleMeta()){
				if(sm.getK().contains("inputOrIP")){
					if(sm.getV().equalsIgnoreCase("ip")){//IP == test
						return s;
					}
				}
			}
		}
		return null;
	}
	private Sample getControlSample(Set<Sample> sampleSet){		
		for(Sample s : sampleSet){
			for(SampleMeta sm : s.getSampleMeta()){
				if(sm.getK().contains("inputOrIP")){
					if(sm.getV().equalsIgnoreCase("input")){//input == control
						return s;
					}
				}
			}
		}
		return null;
	}
	private Set<Sample> getTestAndControlSamples(FileGroup fileGroup){
		
		//at end,should return set containing one sample (IP only, if no control) or two samples (IP and Control)
		
		Set<Sample> tempDerivedFromSamples = new HashSet<Sample>();
		Set<SampleSource> derivedFromSampleSources = new HashSet<SampleSource>();//at end,should be one (IP) or two (IP and Control)
		Set<FileGroup> derivedFromFileGroupSet = fileGroup.getDerivedFrom();		 
		for(FileGroup derivedFromFileGroup : derivedFromFileGroupSet){
			if(derivedFromFileGroup.getSamples().size()>0){//bam files appear NOT to use this
				tempDerivedFromSamples.addAll(derivedFromFileGroup.getSamples());//could be libraries or samples; not yet sure
			}
			else if(derivedFromFileGroup.getSampleSources().size()>0){//bam files appear to use this
				derivedFromSampleSources.addAll(derivedFromFileGroup.getSampleSources());
			}
		}
		if(derivedFromSampleSources.size()>0){
			for(SampleSource ss : derivedFromSampleSources){
				Sample library = sampleService.getLibrary(ss);//library
				tempDerivedFromSamples.add(library);
			}
		}
		Set<Sample> derivedFromSamples = new HashSet<Sample>();//will be returned
		for(Sample s : tempDerivedFromSamples){//move up the chain
			while(s.getParent()!=null){
				s = s.getParent();
			}
			derivedFromSamples.add(s);
		}
		return derivedFromSamples;
	}
	private List<Sample> getLibrariesUsedInAnalysis(FileGroup fileGroup){
		
		//at end,should return set containing one sample (IP only, if no control) or two samples (IP and Control)
		
		Set<Sample> tempDerivedFromLibraries = new HashSet<Sample>();
		Set<SampleSource> derivedFromSampleSources = new HashSet<SampleSource>();//at end,should be one (IP) or two (IP and Control)
		Set<FileGroup> derivedFromFileGroupSet = fileGroup.getDerivedFrom();		 
		for(FileGroup derivedFromFileGroup : derivedFromFileGroupSet){
			if(derivedFromFileGroup.getSamples().size()>0){//bam files appear NOT to use this
				tempDerivedFromLibraries.addAll(derivedFromFileGroup.getSamples());//could be libraries or samples; not yet sure
			}
			else if(derivedFromFileGroup.getSampleSources().size()>0){//bam files appear to use this
				derivedFromSampleSources.addAll(derivedFromFileGroup.getSampleSources());
			}
		}
		if(derivedFromSampleSources.size()>0){
			for(SampleSource ss : derivedFromSampleSources){
				Sample library = sampleService.getLibrary(ss);//library
				tempDerivedFromLibraries.add(library);
			}
		}
		List<Sample> derivedFromLibraries = new ArrayList<Sample>(tempDerivedFromLibraries);//will be returned
		Collections.sort(derivedFromLibraries, new SampleNameComparator());
		return derivedFromLibraries;
	}
	private List<FileHandle> getBamFilesUsedInAnalysis(FileGroup fileGroup){
		List<FileHandle> bamFiles = new ArrayList<FileHandle>();
		Set<FileGroup> derivedFromFileGroupSet = fileGroup.getDerivedFrom();
		for(FileGroup fg : derivedFromFileGroupSet){
			for(FileHandle fh : fg.getFileHandles()){
				bamFiles.add(fh);
			}
		}
		class FileHandleNameComparator implements Comparator<FileHandle> {
		    @Override
		    public int compare(FileHandle arg0, FileHandle arg1) {
		        return arg0.getFileName().compareToIgnoreCase(arg1.getFileName());
		    }
		}
		Collections.sort(bamFiles, new FileHandleNameComparator());
		return bamFiles;
	}
	private List<Software> getSoftwareUsedInAnalysis(FileGroup fileGroup){
		Set<Software> tempSoftwareUsedSet = new HashSet<Software>();
		String softwareIdUsedStringList = "";		
		for(FileGroupMeta fgm : fileGroup.getFileGroupMeta()){
			if(fgm.getK().contains("softwareIdUsedListAsString")){
				softwareIdUsedStringList = fgm.getV();
			}
		}
		try{
			if(!softwareIdUsedStringList.isEmpty()){
				String[] softwareIdAsStringArray = softwareIdUsedStringList.split(":");
				for(String softwareIdAsString : softwareIdAsStringArray){
					Software software = softwareService.getById(Integer.parseInt(softwareIdAsString));
					tempSoftwareUsedSet.add(software);
				}
			}
		}catch(Exception e){logger.debug("error obtaining software in macs2");}
		List<Software> softwareUsedList = new ArrayList<Software>(tempSoftwareUsedSet);
		class SoftwareNameComparator implements Comparator<Software> {
		    @Override
		    public int compare(Software arg0, Software arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
		Collections.sort(softwareUsedList, new SoftwareNameComparator());
		return softwareUsedList;
	}
	
}


