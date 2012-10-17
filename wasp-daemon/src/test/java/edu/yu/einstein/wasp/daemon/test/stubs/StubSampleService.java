package edu.yu.einstein.wasp.daemon.test.stubs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleSubtypeException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SampleService;

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
	public String getReceiveSampleStatus(Sample sample) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void sortSamplesBySampleName(List<Sample> samples) {
		// Auto-generated method stub

	}

	@Override
	public String convertReceiveSampleStatusForWeb(String internalStatus) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String convertReceiveSampleStatusForInternalStorage(String webStatus) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getReceiveSampleStatusOptionsForWeb() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateSampleReceiveStatus(Sample sample, String status) {
		// Auto-generated method stub
		return false;
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
	public List<Sample> getAvailablePlatformUnits() {
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

}
