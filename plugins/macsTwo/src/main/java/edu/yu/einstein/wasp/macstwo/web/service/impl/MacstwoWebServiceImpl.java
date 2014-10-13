package edu.yu.einstein.wasp.macstwo.web.service.impl;

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
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.macstwo.service.impl.MacstwoServiceImpl;
import edu.yu.einstein.wasp.macstwo.software.Macstwo;
import edu.yu.einstein.wasp.macstwo.web.service.MacstwoWebService;
import edu.yu.einstein.wasp.macstwo.webpanels.MacstwoWebPanels;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileHandleMeta;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
//import edu.yu.einstein.wasp.macstwo.web.service.impl.FileTypeComparator;
//import edu.yu.einstein.wasp.macstwo.web.service.impl.SampleNameComparator;

@Service
@Transactional("entityManager")
public class MacstwoWebServiceImpl extends MacstwoServiceImpl implements MacstwoWebService {
	
	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileService fileService;
	@Autowired
	private FileUrlResolver fileUrlResolver;
	@Autowired
	private RunService runService;
	@Autowired
	protected WaspPluginRegistry pluginRegistry;
	
	/*
	@Autowired
	private FileType macs2AnalysisFileType;
	*/
	@Autowired
	private SoftwareService softwareService;
	@Autowired
	private GenomeService genomeService;
	@Autowired
	private Macstwo macs2;

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
	
	@Override
	public String performAction() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public Set<PanelTab> getMacstwoDataToDisplay(Job job)throws PanelException{
		 
		 try{
			 //First, assemble the data
			 Map<FileGroup, Sample> outerCollectionFileGroupTestSampleMap = new HashMap<FileGroup, Sample>();
			 Map<FileGroup, Sample> outerCollectionFileGroupControlSampleMap = new HashMap<FileGroup, Sample>();
			 Map<FileGroup, String> outerCollectionFileGroupCommandLineMap = new HashMap<FileGroup, String>();
			 Map<FileGroup, List<Sample>> outerCollectionFileGroupLibraryListMap = new HashMap<FileGroup, List<Sample>>();
			 Map<FileGroup, List<FileHandle>> outerCollectionFileGroupBamFilesUsedMap = new HashMap<FileGroup, List<FileHandle>>();
			 Map<FileGroup, List<Software>> outerCollectionFileGroupSoftwareUsedMap = new HashMap<FileGroup, List<Software>>();
			 Map<FileGroup, Double> outerCollectionFileGroupFripMap = new HashMap<FileGroup, Double>();
			 Map<FileGroup, String> outerCollectionFileGroupFripCalculationMap = new HashMap<FileGroup, String>();
			 Set<String> fileDescriptionSet = new HashSet<String>();			 
			 Map<FileGroup, List<FileGroup>> outerCollectionFileGroupInnerFileGroupListMap = new HashMap<FileGroup, List<FileGroup>>();
			 
			 List<FileGroup> macs2AnalysisFileGroupList = getMacs2AnalysisFileGroups(job);//the outerCollectionfileGroupList
			 class FileGroupDescriptionComparator implements Comparator<FileGroup> {
				 @Override
				 public int compare(FileGroup arg0, FileGroup arg1) {
					 return arg0.getDescription().compareToIgnoreCase(arg1.getDescription());//base name
				 }
			 }
			 Collections.sort(macs2AnalysisFileGroupList, new FileGroupDescriptionComparator());//sorted by description (which in this case is base name); this sorts the name of an analysis, such as: 20140916_MACS2_IP_309stop_flag_TARGET_FLAG_CONTROL_309stop_input
			 
			 for(FileGroup outerCollectionfileGroup : macs2AnalysisFileGroupList){
				 if(!fileService.isFileGroupCollection(outerCollectionfileGroup)){//just in case, however we will NOT enter here, since fileService.isFileGroupCollection(fileGroup) will be true, as  getMacs2AnalysisFileGroups(job) checked for this already
					continue;
				 }
				 Set<Sample> testAndControlSamples = getTestAndControlSamples(outerCollectionfileGroup);	
				 Sample test = getTestSample(testAndControlSamples);
				 if(test!=null){
					 outerCollectionFileGroupTestSampleMap.put(outerCollectionfileGroup, test);
				 }
				 Sample control = getControlSample(testAndControlSamples);
				 if(control!=null){
					 outerCollectionFileGroupControlSampleMap.put(outerCollectionfileGroup, control);
				 } 
				 String commandLineCalls = getCommandLineCalls(outerCollectionfileGroup);
				 outerCollectionFileGroupCommandLineMap.put(outerCollectionfileGroup, commandLineCalls);
				 List<Software> softwareUsed = getSoftwareUsedInAnalysis(outerCollectionfileGroup);
				 outerCollectionFileGroupSoftwareUsedMap.put(outerCollectionfileGroup, softwareUsed);
				 
				 List<Sample> librariesUsed = getLibrariesUsedInAnalysis(outerCollectionfileGroup);
				 outerCollectionFileGroupLibraryListMap.put(outerCollectionfileGroup, librariesUsed);
				 List<FileHandle> bamFilesUsed = getBamFilesUsedInAnalysis(outerCollectionfileGroup);
				 outerCollectionFileGroupBamFilesUsedMap.put(outerCollectionfileGroup, bamFilesUsed);				 
				 
				 //deal with frip (metadata of fileGroup)
				 Double frip = getFrip(outerCollectionfileGroup);
				 if(frip!=null){
					 outerCollectionFileGroupFripMap.put(outerCollectionfileGroup, frip);
				 }
				 String fripCalculation = getFripPercentCalculation(outerCollectionfileGroup);
				 if(fripCalculation!=null){
					 outerCollectionFileGroupFripCalculationMap.put(outerCollectionfileGroup, fripCalculation);
				 }
				 
				 //finally, deal with list of fileHandles for this outer collection fileGroup
				 //List<FileHandle> fileHandleList = new ArrayList<FileHandle>();
				 List<FileGroup> innerFileGroupList = new ArrayList<FileGroup>(outerCollectionfileGroup.getChildren());
				 Collections.sort(innerFileGroupList, new FileGroupDescriptionComparator());//so now, the innerFileGroups, and thus the innerFileHandles, are sorted by description 
				 outerCollectionFileGroupInnerFileGroupListMap.put(outerCollectionfileGroup, innerFileGroupList);
				 
				 for(FileGroup innerFileGroup : innerFileGroupList){
					 fileDescriptionSet.add(innerFileGroup.getDescription());
				 }				 	 
			}			 
			List<String> fileDescriptionList = new ArrayList<String>(fileDescriptionSet);
			Collections.sort(fileDescriptionList);
			 
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();
			//create the sole panelTab to house ALL the panels
			PanelTab panelTab = new PanelTab();
			panelTab.setTabTitle("MACS2");
			panelTab.setNumberOfColumns(1);
							
			Panel pluginSpecificFileDefinitionsPanel = MacstwoWebPanels.getPluginSpecificFileDefinitionsPanel(fileDescriptionList);
			panelTab.addPanel(pluginSpecificFileDefinitionsPanel);
			Panel samplePairsByAnalysisPanel = MacstwoWebPanels.getSamplePairsByAnalysisPanel(macs2AnalysisFileGroupList, outerCollectionFileGroupTestSampleMap, outerCollectionFileGroupControlSampleMap);
			panelTab.addPanel(samplePairsByAnalysisPanel);
			Panel commandsByAnalysisPanel = MacstwoWebPanels.getCommandsByAnalysisPanel(macs2AnalysisFileGroupList, outerCollectionFileGroupSoftwareUsedMap, outerCollectionFileGroupCommandLineMap);
			panelTab.addPanel(commandsByAnalysisPanel);
			Panel librariesAndBamFilesUsedByAnalysisPanel = MacstwoWebPanels.getLibrariesAndBamFilesUsedByAnalysisPanel(macs2AnalysisFileGroupList, outerCollectionFileGroupLibraryListMap, outerCollectionFileGroupBamFilesUsedMap);
			panelTab.addPanel(librariesAndBamFilesUsedByAnalysisPanel);
			Panel fripCalculationByAnalysisPanel = MacstwoWebPanels.getFripCalculationByAnalysisPanel(macs2AnalysisFileGroupList, outerCollectionFileGroupFripCalculationMap);
			panelTab.addPanel(fripCalculationByAnalysisPanel);			
			// 9-30-14
			Panel filesByAnalysisPanel = MacstwoWebPanels.getFilesByAnalysisPanel(pluginRegistry, fileUrlResolver, macs2AnalysisFileGroupList, outerCollectionFileGroupInnerFileGroupListMap, outerCollectionFileGroupFripMap);
			panelTabSet.add(panelTab);
			panelTab.addPanel(filesByAnalysisPanel);
						
			return panelTabSet;
			
		}catch(Exception e){
			logger.debug("exception in macstwoService.getChipSeqDataToDisplay(job): "+ e.getStackTrace());
			throw new PanelException(e.getMessage());
		}		
	}

	private List<FileGroup> getMacs2AnalysisFileGroups(Job job){
		
		List<FileGroup> macs2AnalysisFileGroupList = new ArrayList<FileGroup>();
		for (Sample s : job.getSample()) {
			for (FileGroup fg : s.getFileGroups()) {
				if (fg.getIsActive() == 0)
					continue;
				if(fileService.isFileGroupCollection(fg)){
					if(fg.getSoftwareGeneratedBy().getId().intValue()==macs2.getId().intValue()){
						logger.trace("Seeking files for job id=" + job.getId()
								+ ". Found file group associated with sample id="
								+ s.getId() + ": '" + fg.getDescription() + "'");
						macs2AnalysisFileGroupList.add(fg);
					}
				}
			}

			for (SampleSource ss : s.getSourceSample()) {
				for (FileGroup fg : ss.getFileGroups()) {
					if (fg.getIsActive() == 0)
						continue;
					if(fileService.isFileGroupCollection(fg)){
						if(fg.getSoftwareGeneratedBy().getId().intValue()==macs2.getId().intValue()){
							logger.trace("Seeking files for job id="
									+ job.getId()
									+ ". Found file group associated with sampleSource id="
									+ ss.getId() + ": '" + fg.getDescription() + "'");
							macs2AnalysisFileGroupList.add(fg);
						}
					}
				}
			}
		}
		return macs2AnalysisFileGroupList;		
	}
	
	public Double getFrip(FileGroup outerCollectionfileGroup){		
		String mappedReadsAsString = "";
		String mappedReadsInPeaksAsString = "";
		for(FileGroupMeta fgm : outerCollectionfileGroup.getFileGroupMeta()){
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
	
	public String getFripPercentCalculation(FileGroup outerCollectionfileGroup){		
		String mappedReadsAsString = "";
		String mappedReadsInPeaksAsString = "";
		for(FileGroupMeta fgm : outerCollectionfileGroup.getFileGroupMeta()){
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
	private String getCommandLineCalls(FileGroup outerCollectionfileGroup){
		String commandLineCalls = "";		
		for(FileGroupMeta fgm : outerCollectionfileGroup.getFileGroupMeta()){
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
	private Set<Sample> getTestAndControlSamples(FileGroup outerCollectionfileGroup){
		
		//at end,should return set containing one sample (IP only, if no control) or two samples (IP and Control)
		
		Set<Sample> tempDerivedFromSamples = new HashSet<Sample>();
		Set<SampleSource> derivedFromSampleSources = new HashSet<SampleSource>();//at end,should be one (IP) or two (IP and Control)
		Set<FileGroup> derivedFromFileGroupSet = outerCollectionfileGroup.getDerivedFrom();		 
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
	private List<Sample> getLibrariesUsedInAnalysis(FileGroup outerCollectionfileGroup){
		
		//at end,should return set containing one sample (IP only, if no control) or two samples (IP and Control)
		
		Set<Sample> tempDerivedFromLibraries = new HashSet<Sample>();
		Set<SampleSource> derivedFromSampleSources = new HashSet<SampleSource>();//at end,should be one (IP) or two (IP and Control)
		Set<FileGroup> derivedFromFileGroupSet = outerCollectionfileGroup.getDerivedFrom();		 
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
	private List<FileHandle> getBamFilesUsedInAnalysis(FileGroup outerCollectionfileGroup){
		List<FileHandle> bamFiles = new ArrayList<FileHandle>();
		Set<FileGroup> derivedFromFileGroupSet = outerCollectionfileGroup.getDerivedFrom();
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
	private List<Software> getSoftwareUsedInAnalysis(FileGroup outerCollectionfileGroup){
		Set<Software> tempSoftwareUsedSet = new HashSet<Software>();
		String softwareIdUsedStringList = "";		
		for(FileGroupMeta fgm : outerCollectionfileGroup.getFileGroupMeta()){
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
