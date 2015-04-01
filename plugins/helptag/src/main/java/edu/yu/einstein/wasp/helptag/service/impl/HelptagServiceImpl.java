/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
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
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatusKey;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexType;
import edu.yu.einstein.wasp.plugin.genomemetadata.plugin.GenomeMetadataPlugin;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.software.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;

@Service
@Transactional("entityManager")
public class HelptagServiceImpl extends WaspServiceImpl implements HelptagService {
	
	@Autowired
	private GenomeMetadataService genomeMetadataService;

	@Autowired
	private GenomeService genomeService;

	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl")
	// more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

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

	public enum HelptagIndexType { GENOME, CDNA };
	
	public final String HELPTAG_INDEX_FLOW = "helptag.buildHpa2Index";

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
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * The Helptag implementation of this should detect the presence of a suitable FASTA index reference and generate a Helptag index of HpaII loci in the
	 * default metadata location of the remote host.
	 * 
	 */
	@Override
	public synchronized GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, Build build) {
		return getGenomeIndexStatus(workService, build, HelptagIndexType.GENOME);
	}
	
	public synchronized GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, Build build, HelptagIndexType type) {

		String indexType = null;

		switch (type) {
			case GENOME:
				indexType = "genome";
				break;
			case CDNA:
				indexType = "cdna";
				break;
			default:
				break;
		}

		logger.trace("getGenomeIndexStatus for Helptag " + build.getGenomeBuildNameString() + " called");

		GenomeIndexStatusKey remoteKey = genomeMetadataService.generateIndexKey(build, workService, new GenomeIndexType("helptag"), indexType);

		GenomeIndexStatus status = GenomeIndexStatus.UNKNOWN;

		// Helptag Hpa2 index requires an existing fasta index
		try {
			GenomeIndexStatus fasta = genomeMetadataService.getFastaStatus(workService, build);

			if (fasta.equals(GenomeIndexStatus.BUILDING))
				return GenomeIndexStatus.BUILDING;
			if (!fasta.isAvailable()) { // if not BUILDING or BUILT ...
				String message = "FASTA genome index for build " + build.getGenomeBuildNameString() + " has unknown status " + fasta.toString()
								 + " with message: " + fasta.getMessage();
				logger.warn(message);
				return status;
			}

		} catch (IOException e) {
			String mess = "Unable to determine status of fasta index of " + build.getGenomeBuildNameString() + " threw exception " + e.getLocalizedMessage();
			logger.warn(mess);
			return status;
		}

		status = genomeMetadataService.getStatus(remoteKey);

		try {
			if (status.equals(GenomeIndexStatus.BUILDABLE)) {
				logger.debug("determined that index for " + build.getGenomeBuildNameString() + " version " + remoteKey.getVersion() + " does not exist on "
							 + remoteKey.getHostname() + " for type " + remoteKey.getType().toString() + " launching Helptag build");
				launchBuildHpa2Index(workService, build, type);
				genomeMetadataService.updateStatus(remoteKey, GenomeIndexStatus.BUILDING);
				return GenomeIndexStatus.BUILDING;
			} else if (status.equals(GenomeIndexStatus.BUILDING) || status.equals(GenomeIndexStatus.BUILT)) {
				return status;
			} else if (status.equals(GenomeIndexStatus.UNKNOWN)) {
				logger.warn("build " + build.getGenomeBuildNameString() + " has a Helptag " + remoteKey.getVersion() + " index status of UNKNOWN");
				return status;
			} else if (status.equals(GenomeIndexStatus.UNBUILDABLE)) {
				logger.error("build " + build.getGenomeBuildNameString() + " has a Helptag " + remoteKey.getVersion()
							 + " index status of UNBUILDABLE with message " + status.getMessage());
				return status;
			} else {
				logger.error("build " + build.getGenomeBuildNameString() + " has a Helptag " + remoteKey.getVersion() + " index status truly UNKNOWN: "
							 + status.toString());
				return GenomeIndexStatus.UNKNOWN;
			}
		} catch (WaspMessageBuildingException e) {
			logger.warn("problem sending message to launch build Helptag index " + e.getLocalizedMessage());
			return GenomeIndexStatus.UNKNOWN;
		}

	}

	private synchronized Message<String> launchBuildHpa2Index(GridWorkService workService, Build build, HelptagIndexType type) throws WaspMessageBuildingException {

		logger.info("Going to begin build of Helptag index on " + workService.getTransportConnection().getHostName());

		Map<String, String> jobParameters = new HashMap<String, String>();

		String indexType = null;
		
		switch (type) {
			case GENOME:
				indexType = "genome";
				break;
			case CDNA:
				indexType= "cdna";
				break;
			default:
				break;
		}
		
		jobParameters.put(WaspJobParameters.HOSTNAME, workService.getTransportConnection().getHostName());
		jobParameters.put(GenomeMetadataPlugin.METADATA_PATH_KEY, genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build));
		jobParameters.put(GenomeMetadataPlugin.BUILD_NAME_KEY, build.getGenomeBuildNameString());
		jobParameters.put(GenomeMetadataPlugin.VERSION_KEY, indexType);

		// this is set to allow multiple runs of the same flow. not typical.
		jobParameters.put(WaspJobParameters.TEST_ID, String.valueOf(System.currentTimeMillis()));

		logger.info("Sending launch message with flow " + HELPTAG_INDEX_FLOW + " and build: " + build.getGenomeBuildNameString() + " for " + indexType);

		waspMessageHandlingService.launchBatchJob(HELPTAG_INDEX_FLOW, jobParameters);
		return (Message<String>) MessageBuilder.withPayload("Initiating Helptag index flow on build " + build.getGenomeBuildNameString()).build();

	}

}
