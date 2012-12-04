package edu.yu.einstein.wasp.daemon.test.stubs;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.ResourceException;
import edu.yu.einstein.wasp.exception.RunException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleSubtypeException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl.LockStatus;
import edu.yu.einstein.wasp.util.SampleWrapper;

@Service
@Transactional
public class StubSampleService implements SampleService {
	
	private SampleDao sampleDao;

	@Override
	@Autowired
	public void setSampleDao(SampleDao sampleDao) {
		this.sampleDao = sampleDao;
	}
	
	@Override
	public SampleDao getSampleDao() {
		return sampleDao;
	}

	@Override
	public Sample getSampleByName(String name) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findAllPlatformUnits() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(
			Integer workflowId) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(
			Integer workflowId, String sampleTypeIName) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(
			Integer workflowId, String[] roles, String sampleTypeIName) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(
			Integer workflowId, String[] roles) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSampleNameUniqueWithinJob(Sample sampleIn,
			SampleType sampleType, Job job) {
		// Auto-generated method stub
		return false;
	}

	@Override
	public void saveSampleWithAssociatedMeta(Sample sample) {
		// Auto-generated method stub

	}

	@Override
	public BatchStatus getReceiveSampleStatus(Sample sample) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void sortSamplesBySampleName(List<Sample> samples) {
		// Auto-generated method stub

	}

	
	

	@Override
	public List<String> getReceiveSampleStatusOptionsForWeb() {
		// Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean submittedSampleHasBeenProcessedByFacility(Sample sample) {
		// Auto-generated method stub
		return false;
	}

	@Override
	public List<Sample> getFacilityGeneratedLibraries(Sample sample) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Adaptor getLibraryAdaptor(Sample library) {
		// Auto-generated method stub
		return null;
	}


	@Override
	public List<Sample> getAvailableAndCompatiblePlatformUnits(Job job) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Sample> getIndexedCellsOnPlatformUnit(
			Sample platformUnit) throws SampleTypeException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void addCellToPlatformUnit(Sample platformUnit, Sample cell,
			Integer Index) throws SampleTypeException, SampleIndexException {
		// Auto-generated method stub

	}

	@Override
	public List<Sample> getLibrariesOnCell(Sample cell)
			throws SampleTypeException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getLibrariesOnCellWithoutControls(Sample cell)
			throws SampleTypeException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getControlLibrariesOnCell(Sample cell)
			throws SampleTypeException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		// Auto-generated method stub

	}

	@Override
	public Sample getPlatformUnitForCell(Sample cell)
			throws SampleTypeException, SampleParentChildException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void addLibraryToCell(Sample cell, Sample library,
			Float libConcInLanePicoM) throws SampleTypeException,
			SampleException, SampleMultiplexException, MetadataException {
		// Auto-generated method stub

	}

	@Override
	public SampleDraft cloneSampleDraft(SampleDraft sampleDraft) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getAvailablePlatformUnits() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public Run getCurrentRunForPlatformUnit(Sample platformUnit) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBiomolecule(Sample sample) {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBiomolecule(SampleDraft sampleDraft) {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLibrary(Sample sample) {
		// stub functionality copied from SampleServiceImpl
		if (sample.getSampleType().getIName().equals("library") || sample.getSampleType().getIName().equals("facilityLibrary"))
			return true;
		return false;
	}

	@Override
	public boolean isLibrary(SampleDraft sampleDraft) {
		// Auto-generated method stub
		return false;
	}


	@Override
	public Integer getNumberOfIndexedCellsOnPlatformUnit(Sample platformUnit)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean barcodeNameExists(String barcodeName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesBySampleTypeIName(
			String sampleTypeIName) throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SampleSubtype getSampleSubtypeById(Integer sampleSubtypeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sampleSubtypeIsSpecificSampleType(
			SampleSubtype sampleSubtype, String sampleTypeIName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sample getSampleById(Integer sampleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sampleIsSpecificSampleType(Sample sample,
			String sampleTypeIName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sampleIsSpecificSampleSubtype(Sample sample,
			String sampleSubtypeIName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sampleIsInDatabase(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sampleIdIsInDatabase(Integer sampleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sampleIdIsValid(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sampleIsPlatformUnit(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sample getPlatformUnit(Integer sampleId)
			throws NumberFormatException, SampleException, SampleTypeException,
			SampleSubtypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SampleSubtype getSampleSubtypeConfirmedForPlatformunit(
			Integer sampleSubtypeId) throws NumberFormatException,
			SampleSubtypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getNumberOfCellsListForThisTypeOfPlatformUnit(
			SampleSubtype sampleSubtype) throws SampleTypeException,
			SampleSubtypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean requestedReductionInCellNumberIsProhibited(
			Sample platformUnitInDatabase, Integer numberOfLanesRequested)
			throws SampleException, SampleTypeException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createUpdatePlatformUnit(Sample platformUnit,
			SampleSubtype sampleSubtype, String barcodeName,
			Integer numberOfLanesRequested, List<SampleMeta> sampleMetaList)
			throws SampleException, SampleTypeException, SampleSubtypeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePlatformUnit(Integer platformUnitId)
			throws NumberFormatException, SampleException, SampleTypeException,
			SampleSubtypeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendOutboundMessage(Message<?> message, boolean isReplyExpected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean isSampleReceived(Sample sample) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertSampleReceivedStatusForWeb(BatchStatus internalStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSampleReceiveStatus(Sample sample, WaspStatus status)
			throws WaspMessageBuildingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLibraryAwaitingPlatformUnitPlacement(Sample library)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlatformUnitAwaitingSequenceRunPlacement(
			Sample platformUnit) throws SampleTypeException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Resource> getAllMassivelyParallelSequencingMachines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Resource> getSequencingMachinesCompatibleWithPU(
			Sample platformUnit) throws SampleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getSequencingMachineByResourceId(Integer resourceId)
			throws ResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Run getSequenceRun(Integer runId) throws RunException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createUpdateSequenceRun(Run runInstance,
			List<RunMeta> runMetaList, Integer platformUnitId,
			Integer resourceId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean platformUnitIsCompatibleWithSequencingMachine(
			Sample platformUnit, Resource sequencingMachineInstance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteSequenceRun(Run run) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean isSampleAwaitingLibraryCreation(Sample sample) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDnaOrRna(SampleDraft sampleDraft) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDnaOrRna(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Sample> getAvailableAndCompatiblePlatformUnits(
			ResourceCategory resourceCategory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlatformUnitLockStatus(Sample platformunit,
			LockStatus lockStatus) throws SampleTypeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LockStatus getPlatformUnitLockStatus(Sample platformunit)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WaspStatus convertSampleReceivedStatusFromWeb(String webStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initiateBatchJobForSample(Job job, Sample sample,
			String batchJobName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Sample> getCellsForLibrary(Sample library)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRequestedSampleCoverage(Sample sample) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Sample> getRunningOrSuccessfullyRunPlatformUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertSampleQCStatusForWeb(BatchStatus internalStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WaspStatus convertSampleQCStatusFromWeb(String webStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateQCStatus(Sample sample, WaspStatus status)
			throws WaspMessageBuildingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initiateBatchJobForLibrary(Job job, Sample library,
			String batchJobName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createFacilityLibraryFromMacro(Job job,
			SampleWrapper managedLibrary, List<SampleMeta> libraryMetaList) {
		// TODO Auto-generated method stub
		
	}

}
