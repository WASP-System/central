/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.software.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;

@Service
@Transactional("entityManager")
public class HelptagServiceImpl extends WaspServiceImpl implements HelptagService {
	
	@Autowired
	private JobDraftService jobDraftService;
	
	@Autowired
	private JobService jobService;

	@Autowired
	private FileService fileService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	ResourceType helptagAngleMakerResourceType;

	@Autowired
	private WaspPluginRegistry waspPluginRegistry;

	@Autowired
	private FileType hcountFileType;

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
	public JobDataTabViewing getHAMPlugin(Job job) throws JobContextInitializationException, SoftwareConfigurationException {
		WaspJobContext waspJobContext = new WaspJobContext(job.getId(), jobService);
		SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(helptagAngleMakerResourceType);
		if (softwareConfig == null) {
			throw new SoftwareConfigurationException("No software could be configured for jobId=" + job.getId() + " with resourceType iname="
													 + helptagAngleMakerResourceType.getIName());
		}
		return waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), JobDataTabViewing.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public boolean confirmCellLibrariesAssociatedWithHcountFiles(List<SampleSource> cellLibraryList) {
		for (SampleSource cellLibrary : cellLibraryList) {
			logger.debug("cellLibrary id: " + cellLibrary.getId());
			logger.debug("hcountFileType : " + hcountFileType.getIName());
			Set<FileGroup> fileGroupSetFromCellLibrary = fileService.getFilesForCellLibraryByType(cellLibrary, hcountFileType);
			if (fileGroupSetFromCellLibrary.isEmpty()) {
				logger.debug("no hcount files associated with cellLibrary id: " + cellLibrary.getId());
				return false;
			}
		}
		return true;
	}

	@Override
	public FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType) {
		FileHandle fileHandle = new FileHandle();
		fileHandle.setFileName(fileName);
		fileHandle.setFileType(fileType);
		fileHandle = fileService.addFile(fileHandle);
		return fileHandle;
	}

	@Override
	public FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description) {
		FileGroup fileGroup = new FileGroup();
		fileGroup.setDescription(description);
		fileGroup.setFileType(fileHandle.getFileType());
		fileGroup.setSoftwareGeneratedById(software.getId());
		fileGroup.setIsActive(0);

		Set<FileHandle> fileHandleSet = new HashSet<FileHandle>();
		fileHandleSet.add(fileHandle);
		fileGroup.setFileHandles(fileHandleSet);
		fileGroup = fileService.addFileGroup(fileGroup);
		return fileGroup;
	}
	
	@Override
	public List<SampleDraft> createNewHelpDNASampleDrafts(SampleDraft sampleDraft, List<String> librariesToCreateList){
		List<SampleDraft> newSampleDrafts = new ArrayList<SampleDraft>();
		for(String libraryTypeToCreate : librariesToCreateList){
			SampleDraft clone = sampleService.cloneSampleDraft(sampleDraft);
			List<SampleDraftMeta> sampleDraftMetaList = clone.getSampleDraftMeta();
			clone.setName(clone.getName() + "_" + libraryTypeToCreate);	
			SampleDraft cloneDB = sampleService.getSampleDraftDao().save(clone);
			cloneDB.setSampleDraftMeta(sampleDraftMetaList);
			this.resetLibraryToCreateMetaData(cloneDB, libraryTypeToCreate);
			newSampleDrafts.add(cloneDB);
		}		
		return newSampleDrafts;
	}
	private void resetLibraryToCreateMetaData(SampleDraft clone, String libraryTypeToCreate){
		for(SampleDraftMeta sdm : clone.getSampleDraftMeta()){
			if(sdm.getK().endsWith("typeOfHelpLibraryRequested")){
				sdm.setV(libraryTypeToCreate);
			}
			sdm.setSampleDraftId(clone.getId());//must do this for every meta entry
			sampleService.getSampleDraftMetaDao().save(sdm);//then must do this for every meta entry
		}
	}

	@Override
	public List<SampleDraft> getAllMspISampleDraftsFromJobDraftId(Integer id) {
		JobDraft jobDraft = jobDraftService.getJobDraftById(id);
		List<SampleDraft> sampleDrafts = jobDraft.getSampleDraft();

		List<SampleDraft> mspSampleDrafts = new ArrayList<SampleDraft>();
		String enzymeString;
		for (SampleDraft sd : sampleDrafts) {
			try {
				if (sd.getSampleType().getIName().equalsIgnoreCase("library")) {
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				} else {// genomic DNA; 1-8-15; dubin
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sd.getSampleDraftMeta());
				}
				if (enzymeString.equals("MspI"))
					mspSampleDrafts.add(sd);
			} catch (MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta is not found for Sample Draft id = " + id);
			}
		}

		return mspSampleDrafts;
	}

	@Override
	public List<SampleDraft> getAllHpaIIAndbetaGTMspISampleDraftsFromJobDraftId(Integer id){
		JobDraft jobDraft = jobDraftService.getJobDraftById(id);
		List<SampleDraft> sampleDrafts = jobDraft.getSampleDraft();
		
		List<SampleDraft> hpaAndbetaGTMspSampleDrafts = new ArrayList<SampleDraft>();
		String enzymeString;
		for (SampleDraft sd : sampleDrafts) {
			try{
				if(sd.getSampleType().getIName().equalsIgnoreCase("library")){
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				}
				else{//genomic DNA; 1-8-15; dubin
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sd.getSampleDraftMeta());					
				}
				if (enzymeString.equals("HpaII")||enzymeString.equals("beta-GT-MspI"))
					hpaAndbetaGTMspSampleDrafts.add(sd);
			} catch(MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) is not found for Sample Draft id = " + id);
			}
		}

		return hpaAndbetaGTMspSampleDrafts;
	}

	@Override
	public List<SampleDraft> getAllbetaGTMspISampleDraftsFromJobDraftId(Integer id){
		JobDraft jobDraft = jobDraftService.getJobDraftById(id);
		List<SampleDraft> sampleDrafts = jobDraft.getSampleDraft();
		
		List<SampleDraft> betaGTMspSampleDrafts = new ArrayList<SampleDraft>();
		String enzymeString;
		for (SampleDraft sd : sampleDrafts) {
			try{
				if(sd.getSampleType().getIName().equalsIgnoreCase("library")){
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				}
				else{//genomic DNA; 1-8-15; dubin
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sd.getSampleDraftMeta());					
				}
				if (enzymeString.equals("beta-GT-MspI"))
					betaGTMspSampleDrafts.add(sd);
			} catch(MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) is not found for Sample Draft id = " + id);
			}
		}
		return betaGTMspSampleDrafts;
	}

	@Override
	public String getTypeOfHelpLibraryRequestedForMacromolecule(Sample sample){
		try{
			return (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sample.getSampleMeta());					
		} catch(MetadataException e) {}	
		// not found
		return null;
	}

	@Override
	public String getTypeOfHelpLibrary(Sample sample){
		try{
			return (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sample.getSampleMeta());					
		} catch(MetadataException e) {}	
		// not found
		return null;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public boolean isMspI(Sample s) {
		String enzymeString;
		try {
			if (s.getParentId() != null) {
				// if it has a parent sample, which means it's a facility library, use its parent sample to check for library type
				s = sampleService.getSampleById(s.getParentId());
			}
			if (!s.getSampleType().getIName().equalsIgnoreCase("dna")) {// not dna, so must be library
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
			} else {// genomic DNA
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, s.getSampleMeta());
			}

			if (enzymeString.equals("MspI")) {
				return true;
			}
		} catch (MetadataException e) {
			// not found
			logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) not found for Sample id = " + s.getId());
		}
		return false;
	}

	@Override
	public boolean isHpaII(Sample s){
		String enzymeString;
		try{
			if (s.getParentId() != null) {
				// if it has a parent sample, which means it's a facility library, use its parent sample to check for library type
				s = sampleService.getSampleById(s.getParentId());
			}
			if(!s.getSampleType().getIName().equalsIgnoreCase("dna")){//not dna, so must be library
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
			} else {// genomic DNA
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, s.getSampleMeta());					
			}

			if (enzymeString.equals("HpaII")){
				return true;
			}
		} catch(MetadataException e) {
			// not found
			logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) not found for Sample id = " + s.getId());
		}
		return false;
	}

	@Override
	public boolean isBetaGTMspI(Sample s){
		String enzymeString;
		try{
			if (s.getParentId() != null) {
				// if it has a parent sample, which means it's a facility library, use its parent sample to check for library type
				s = sampleService.getSampleById(s.getParentId());
			}
			if(!s.getSampleType().getIName().equalsIgnoreCase("dna")){//not dna, so must be library
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
			} else {// genomic DNA
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, s.getSampleMeta());					
			}

			if (enzymeString.equals("beta-GT-MspI")){
				return true;
			}
		} catch(MetadataException e) {
			// not found
			logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) not found for Sample id = " + s.getId());
		}
		return false;
	}
	
	@Override
	public List<String> getTypeOfHelpLibrariesRequestedList(List<SampleDraftMeta> sampleDraftMetaList){
		List<String> typeOfHelpLibrariesRequestedList = new ArrayList<String>();
		for(SampleDraftMeta sdm : sampleDraftMetaList){
			if(sdm.getK().endsWith("typeOfHelpLibraryRequested")){
				String [] stringArray = StringUtils.split(sdm.getV(),",");
				for(int i = 0; i < stringArray.length; i++){
					typeOfHelpLibrariesRequestedList.add(stringArray[i].trim());
				}
				break;
			}
		}
		return typeOfHelpLibrariesRequestedList;
	}
	
	

}
