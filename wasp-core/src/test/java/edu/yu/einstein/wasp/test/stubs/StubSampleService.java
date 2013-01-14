package edu.yu.einstein.wasp.test.stubs;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.integration.Message;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.MetaMessage;
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
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl.LockStatus;
import edu.yu.einstein.wasp.util.SampleWrapper;

@Service
public class StubSampleService implements SampleService {

	public StubSampleService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sendOutboundMessage(Message<?> message, boolean isReplyExpected) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSampleDao(SampleDao sampleDao) {
		// TODO Auto-generated method stub

	}

	@Override
	public SampleDao getSampleDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sample getSampleByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> findAllPlatformUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(
			Integer workflowId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(
			Integer workflowId, String sampleTypeIName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(
			Integer workflowId, String[] roles, String sampleTypeIName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(
			Integer workflowId, String[] roles) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSampleNameUniqueWithinJob(Sample sampleIn,
			SampleType sampleType, Job job) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveSampleWithAssociatedMeta(Sample sample) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExitStatus getReceiveSampleStatus(Sample sample) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExitStatus getSampleQCStatus(Sample sample) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExitStatus getLibraryQCStatus(Sample library) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isSampleReceived(Sample sample) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isSampleAwaitingLibraryCreation(Sample sample) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sortSamplesBySampleName(List<Sample> samples) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sortSamplesBySampleId(List<Sample> samples) {
		// TODO Auto-generated method stub

	}

	@Override
	public String convertSampleReceivedStatusForWeb(ExitStatus internalStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WaspStatus convertSampleReceivedStatusFromWeb(String webStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getReceiveSampleStatusOptionsForWeb() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertSampleQCStatusForWeb(ExitStatus internalStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WaspStatus convertSampleQCStatusFromWeb(String webStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSampleReceiveStatus(Sample sample, WaspStatus status)
			throws WaspMessageBuildingException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSubmittedSampleProcessedByFacility(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Sample> getFacilityGeneratedLibraries(Sample sample) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Adaptor getLibraryAdaptor(Sample library) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getAvailablePlatformUnits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getAvailableAndCompatiblePlatformUnits(
			ResourceCategory resourceCategory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getAvailableAndCompatiblePlatformUnits(Job job) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Sample> getIndexedCellsOnPlatformUnit(
			Sample platformUnit) throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNumberOfIndexedCellsOnPlatformUnit(Sample platformUnit)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCellToPlatformUnit(Sample platformUnit, Sample cell,
			Integer Index) throws SampleTypeException, SampleIndexException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Sample> getLibrariesOnCell(Sample cell)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getLibrariesOnCellWithoutControls(Sample cell)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getControlLibrariesOnCell(Sample cell)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		// TODO Auto-generated method stub

	}

	@Override
	public Sample getPlatformUnitForCell(Sample cell)
			throws SampleTypeException, SampleParentChildException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLibraryToCell(Sample cell, Sample library,
			Float libConcInLanePicoM) throws SampleTypeException,
			SampleException, SampleMultiplexException, MetadataException {
		// TODO Auto-generated method stub

	}

	@Override
	public SampleDraft cloneSampleDraft(SampleDraft sampleDraft) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Run getCurrentRunForPlatformUnit(Sample platformUnit)
			throws SampleTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBiomolecule(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBiomolecule(SampleDraft sampleDraft) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLibrary(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLibrary(SampleDraft sampleDraft) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBarcodeNameExisting(String barcodeName) {
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
	public boolean isSampleSubtypeWithSpecificSampleType(
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
	public boolean isSampleOfSpecificSampleType(Sample sample,
			String sampleTypeIName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSampleOfSpecificSampleSubtype(Sample sample,
			String sampleSubtypeIName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSampleInDatabase(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSampleIdInDatabase(Integer sampleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSampleIdValid(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlatformUnit(Sample sample) {
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
	public boolean isRequestedReductionInCellNumberProhibited(
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
	public boolean isPlatformUnitCompatibleWithSequencingMachine(
			Sample platformUnit, Resource sequencingMachineInstance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteSequenceRun(Run run) throws Exception {
		// TODO Auto-generated method stub

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
	public void initiateBatchJobForSample(Job job, Sample sample,
			String batchJobName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initiateBatchJobForLibrary(Job job, Sample library,
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
	public void updateQCStatus(Sample sample, WaspStatus status)
			throws WaspMessageBuildingException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createFacilityLibraryFromMacro(Job job,
			SampleWrapper managedLibrary, List<SampleMeta> libraryMetaList) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSamplePassQC(Sample sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLibraryPassQC(Sample library) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Sample> getPlatformUnitsNotYetRun() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLibraryFromCellOfPlatformUnit(SampleSource cellLibraryLink)
			throws SampleTypeException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLibraryFromCellOfPlatformUnit(Sample cell, Sample library)
			throws SampleTypeException, SampleParentChildException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSampleQCComment(Integer sampleId, String comment)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<MetaMessage> getSampleQCComments(Integer sampleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Sample> getLibrariesOnSuccessfulRunCellsWithoutControls(Integer runId) {
		return null;
	}

	@Override
	public Set<Sample> getLibrariesOnSuccessfulRunCellsWithoutControls(Run run) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Sample> getLibrariesOnSuccessfulRunCells(Integer runId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Sample> getLibrariesOnSuccessfulRunCells(Run run) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCell(Sample cell) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCellSequencedSuccessfully(Sample cell)	throws SampleTypeException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsCellSequencedSuccessfully(Sample cell, boolean success) throws SampleTypeException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isControlLibrary(Sample library) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateSampleMeta(Sample sample, List<SampleMeta> sampleMetaList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateExistingSampleViaSampleWrapper(SampleWrapper sw,
			List<SampleMeta> sampleMetaList) {
		// TODO Auto-generated method stub
		
	}

}
