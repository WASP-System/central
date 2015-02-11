/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptagham.web.service.impl;

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

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.helptagham.service.impl.AbstractHelptaghamServiceImpl;
import edu.yu.einstein.wasp.helptagham.software.Helptagham;
import edu.yu.einstein.wasp.helptagham.web.service.HelptaghamWebService;
import edu.yu.einstein.wasp.helptagham.webpanels.HelptaghamWebPanels;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Service
@Transactional("entityManager")
public class HelptaghamWebServiceImpl extends AbstractHelptaghamServiceImpl implements HelptaghamWebService {
	
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
	@Autowired
	private SoftwareService softwareService;
	@Autowired
	private GenomeService genomeService;

	@Autowired
	private Helptagham helptagHAM;

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public Set<PanelTab> getHAMDataToDisplay(Job job) throws PanelException {
		Map<FileGroup, Sample> outerCollectionFileGroupHpa2SampleMap = new HashMap<FileGroup, Sample>();
		Map<FileGroup, Sample> outerCollectionFileGroupMsp1SampleMap = new HashMap<FileGroup, Sample>();
		Map<FileGroup, String> outerCollectionFileGroupCommandLineMap = new HashMap<FileGroup, String>();
		Map<FileGroup, List<Sample>> outerCollectionFileGroupLibraryListMap = new HashMap<FileGroup, List<Sample>>();
		Map<FileGroup, List<FileHandle>> outerCollectionFileGroupHcountFilesUsedMap = new HashMap<FileGroup, List<FileHandle>>();
		Map<FileGroup, List<Software>> outerCollectionFileGroupSoftwareUsedMap = new HashMap<FileGroup, List<Software>>();

		Set<String> fileDescriptionSet = new HashSet<String>();
		Map<FileGroup, List<FileGroup>> outerCollectionFileGroupInnerFileGroupListMap = new HashMap<FileGroup, List<FileGroup>>();

		List<FileGroup> hamAnalysisFileGroupList = new ArrayList<FileGroup>();// the outerCollectionfileGroupList
		for (FileGroup fg : getHamAnalysisFileGroups(job))
			hamAnalysisFileGroupList.add(fg);
		class FileGroupDescriptionComparator implements Comparator<FileGroup> {
			@Override
			public int compare(FileGroup arg0, FileGroup arg1) {
				return arg0.getDescription().compareToIgnoreCase(arg1.getDescription());// base name
			}
		}
		Collections.sort(hamAnalysisFileGroupList, new FileGroupDescriptionComparator());// sorted by description (which in this case is base name); this
																						   // sorts the name of an analysis, such as:
																						   // 20140916_MACS2_IP_309stop_flag_TARGET_FLAG_CONTROL_309stop_input

		for (FileGroup outerCollectionfileGroup : hamAnalysisFileGroupList) {
			if (!fileService.isFileGroupCollection(outerCollectionfileGroup)) {// just in case, however we will NOT enter here, since
																			   // fileService.isFileGroupCollection(fileGroup) will be true, as
																			   // getMacs2AnalysisFileGroups(job) checked for this already
				continue;
			}
			Set<Sample> samplePair = getSamplePairDerivedFrom(outerCollectionfileGroup);
			Sample test = getHpa2OrBetaGTSampleFromPair(samplePair);
			if (test != null) {
				outerCollectionFileGroupHpa2SampleMap.put(outerCollectionfileGroup, test);
			}
			Sample sMsp1 = getMsp1SampleFromPair(samplePair);
			if (sMsp1 != null) {
				outerCollectionFileGroupMsp1SampleMap.put(outerCollectionfileGroup, sMsp1);
			}

			List<Sample> librariesUsed = getLibrariesUsedInAnalysis(outerCollectionfileGroup);
			outerCollectionFileGroupLibraryListMap.put(outerCollectionfileGroup, librariesUsed);
			List<FileHandle> hcountFilesUsed = getHcountFilesUsedInAnalysis(outerCollectionfileGroup);
			outerCollectionFileGroupHcountFilesUsedMap.put(outerCollectionfileGroup, hcountFilesUsed);

			// finally, deal with list of fileHandles for this outer collection fileGroup
			List<FileGroup> innerFileGroupList = new ArrayList<FileGroup>(outerCollectionfileGroup.getChildren());
			// so now, the innerFileGroups, and thus the innerFileHandles, are sorted by description
			Collections.sort(innerFileGroupList, new FileGroupDescriptionComparator());
			outerCollectionFileGroupInnerFileGroupListMap.put(outerCollectionfileGroup, innerFileGroupList);

			for (FileGroup innerFileGroup : innerFileGroupList) {
				fileDescriptionSet.add(innerFileGroup.getDescription());
			}
		}
		List<String> fileDescriptionList = new ArrayList<String>(fileDescriptionSet);
		Collections.sort(fileDescriptionList);

		Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();
		// create the sole panelTab to house ALL the panels
		PanelTab panelTab = new PanelTab();
		panelTab.setTabTitle("HAM for HELPtagging");
		panelTab.setNumberOfColumns(1);

		panelTab.addPanel(HelptaghamWebPanels.getPluginSpecificFileDefinitionsPanel(fileDescriptionList));
		panelTab.addPanel(HelptaghamWebPanels.getSamplePairsByAnalysisPanel(hamAnalysisFileGroupList, outerCollectionFileGroupHpa2SampleMap,
																			outerCollectionFileGroupMsp1SampleMap));
		panelTab.addPanel(HelptaghamWebPanels.getLibrariesAndHcountFilesUsedByAnalysisPanel(hamAnalysisFileGroupList, outerCollectionFileGroupLibraryListMap,
																							outerCollectionFileGroupHcountFilesUsedMap));
		panelTab.addPanel(HelptaghamWebPanels.getFilesByAnalysisPanel(pluginRegistry, fileUrlResolver, fileService, hamAnalysisFileGroupList,
																	  outerCollectionFileGroupInnerFileGroupListMap));

		panelTabSet.add(panelTab);
		return panelTabSet;
	}

	private Set<FileGroup> getHamAnalysisFileGroups(Job job) {

		Set<FileGroup> hamAnalysisFileGroupList = new LinkedHashSet<FileGroup>();
		for (Sample s : job.getSample()) {
			for (FileGroup fg : s.getFileGroups()) {
				if (fg.getIsActive() == 0)
					continue;
				if (fileService.isFileGroupCollection(fg)) {
					if (fg.getSoftwareGeneratedBy().getId().intValue() == helptagHAM.getId().intValue()) {
						logger.trace("Seeking files for job id=" + job.getId() + ". Found file group associated with sample id=" + s.getId() + ": '"
									 + fg.getDescription() + "'");
						hamAnalysisFileGroupList.add(fg);
					}
				}
			}

			for (SampleSource ss : s.getSourceSample()) {
				for (FileGroup fg : ss.getFileGroups()) {
					if (fg.getIsActive() == 0)
						continue;
					if (fileService.isFileGroupCollection(fg)) {
						if (fg.getSoftwareGeneratedBy().getId().intValue() == helptagHAM.getId().intValue()) {
							logger.trace("Seeking files for job id=" + job.getId() + ". Found file group associated with sampleSource id=" + ss.getId() + ": '"
										 + fg.getDescription() + "'");
							hamAnalysisFileGroupList.add(fg);
						}
					}
				}
			}
		}
		return hamAnalysisFileGroupList;
	}

	private Set<Sample> getSamplePairDerivedFrom(FileGroup outerCollectionfileGroup) {

		// at end,should return set containing one sample (Hpa2/Beta-GT only, if no Msp1) or two samples (Hpa2/Beta-GT and Msp1)

		Set<Sample> tempDerivedFromSamples = new HashSet<Sample>();
		Set<SampleSource> derivedFromSampleSources = new HashSet<SampleSource>();// at end,should be one (IP) or two (IP and Control)
		Set<FileGroup> derivedFromFileGroupSet = outerCollectionfileGroup.getDerivedFrom();
		for (FileGroup derivedFromFileGroup : derivedFromFileGroupSet) {
			if (derivedFromFileGroup.getSamples().size() > 0) {// bam files appear NOT to use this
				tempDerivedFromSamples.addAll(derivedFromFileGroup.getSamples());// could be libraries or samples; not yet sure
			} else if (derivedFromFileGroup.getSampleSources().size() > 0) {// bam files appear to use this
				derivedFromSampleSources.addAll(derivedFromFileGroup.getSampleSources());
			}
		}
		if (derivedFromSampleSources.size() > 0) {
			for (SampleSource ss : derivedFromSampleSources) {
				Sample library = sampleService.getLibrary(ss);// library
				tempDerivedFromSamples.add(library);
			}
		}
		Set<Sample> derivedFromSamples = new HashSet<Sample>();// will be returned
		for (Sample s : tempDerivedFromSamples) {// move up the chain
			while (s.getParent() != null) {
				s = s.getParent();
			}
			derivedFromSamples.add(s);
		}
		return derivedFromSamples;
	}

	private Sample getHpa2OrBetaGTSampleFromPair(Set<Sample> sampleSet) {
		String enzymeString;
		for (Sample s : sampleSet) {
			try {
				if (s.getSampleType().getIName().equalsIgnoreCase("library")) {
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
				} else {// genomic DNA
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, s.getSampleMeta());
				}
				if (enzymeString.equals("HpaII") || enzymeString.equals("beta-GT-MspI"))
					return s;
			} catch (MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) is not found for Sample id = " + s.getId());
			}
		}
		return null;
	}

	private Sample getMsp1SampleFromPair(Set<Sample> sampleSet) {
		String enzymeString;
		for (Sample s : sampleSet) {
			try {
				if (s.getSampleType().getIName().equalsIgnoreCase("library")) {
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
				} else {// genomic DNA
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, s.getSampleMeta());
				}
				if (enzymeString.equals("MspI"))
					return s;
			} catch (MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) is not found for Sample id = " + s.getId());
			}
		}
		return null;
	}

	private String getCommandLineCalls(FileGroup outerCollectionfileGroup) {
		String commandLineCalls = "";
		for (FileGroupMeta fgm : outerCollectionfileGroup.getFileGroupMeta()) {
			if (fgm.getK().equalsIgnoreCase("helptaghamAnalysis.commandLineCall")) {
				commandLineCalls = fgm.getV();
				break;
			}
		}
		return commandLineCalls;
	}

	private List<Software> getSoftwareUsedInAnalysis(FileGroup outerCollectionfileGroup) {
		Set<Software> tempSoftwareUsedSet = new HashSet<Software>();
		String softwareIdUsedStringList = "";
		for (FileGroupMeta fgm : outerCollectionfileGroup.getFileGroupMeta()) {
			if (fgm.getK().contains("softwareIdUsedListAsString")) {
				softwareIdUsedStringList = fgm.getV();
			}
		}
		try {
			if (!softwareIdUsedStringList.isEmpty()) {
				String[] softwareIdAsStringArray = softwareIdUsedStringList.split(":");
				for (String softwareIdAsString : softwareIdAsStringArray) {
					Software software = softwareService.getById(Integer.parseInt(softwareIdAsString));
					tempSoftwareUsedSet.add(software);
				}
			}
		} catch (Exception e) {
			logger.debug("error obtaining software in macs2");
		}
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

	private List<Sample> getLibrariesUsedInAnalysis(FileGroup outerCollectionfileGroup) {

		// at end,should return set containing one sample (Hpa2/beat-GT only, if no Msp1) or two samples (Hpa2/beta-GT and Msp1)

		Set<Sample> tempDerivedFromLibraries = new HashSet<Sample>();
		Set<SampleSource> derivedFromSampleSources = new HashSet<SampleSource>();
		Set<FileGroup> derivedFromFileGroupSet = outerCollectionfileGroup.getDerivedFrom();
		for (FileGroup derivedFromFileGroup : derivedFromFileGroupSet) {
			if (derivedFromFileGroup.getSamples().size() > 0) {// bam files appear NOT to use this
				tempDerivedFromLibraries.addAll(derivedFromFileGroup.getSamples());// could be libraries or samples; not yet sure
			} else if (derivedFromFileGroup.getSampleSources().size() > 0) {// bam files appear to use this
				derivedFromSampleSources.addAll(derivedFromFileGroup.getSampleSources());
			}
		}

		if (derivedFromSampleSources.size() > 0) {
			for (SampleSource ss : derivedFromSampleSources) {
				Sample library = sampleService.getLibrary(ss);// library
				tempDerivedFromLibraries.add(library);
			}
		}

		List<Sample> derivedFromLibraries = new ArrayList<Sample>(tempDerivedFromLibraries);
		class SampleNameComparator implements Comparator<Sample> {
			@Override
			public int compare(Sample arg0, Sample arg1) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			}
		}
		Collections.sort(derivedFromLibraries, new SampleNameComparator());

		return derivedFromLibraries;
	}

	private List<FileHandle> getHcountFilesUsedInAnalysis(FileGroup outerCollectionfileGroup) {
		List<FileHandle> hcountFiles = new ArrayList<FileHandle>();
		Set<FileGroup> derivedFromFileGroupSet = outerCollectionfileGroup.getDerivedFrom();
		for (FileGroup fg : derivedFromFileGroupSet) {
			for (FileHandle fh : fg.getFileHandles()) {
				hcountFiles.add(fh);
			}
		}

		class FileHandleNameComparator implements Comparator<FileHandle> {
			@Override
			public int compare(FileHandle arg0, FileHandle arg1) {
				return arg0.getFileName().compareToIgnoreCase(arg1.getFileName());
			}
		}
		Collections.sort(hcountFiles, new FileHandleNameComparator());

		return hcountFiles;
	}

}
