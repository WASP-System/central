package edu.yu.einstein.wasp.util;

import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * An interface that describes a biological molecule
 * @author ASMcLellan
 *
 */
public interface BioMoleculeWrapperI {

	/** 
	 * Retrieve the Sample entity represented
	 * @return
	 */
	public Sample getSampleObject();
	
	/**
	 * Obtains a list of ALL meta data associated with this biological sample, including that from all ancestor
	 * objects (cascading SampleMeta)
	 * @return list of {@link SampleMeta}
	 */
	public List<SampleMeta> getAllSampleMeta();
	
	/**
	 * Retrieves the set of unique meta areas representative of ALL meta data associated with this biological sample, including that from parent
	 * objects (cascading SampleMeta)
	 * @return
	 */
	public Set<String> getAllSampleMetaAreas();
	
	
	/**
	 * Retrieves a list of ALL meta data associated with this biological sample using only the areas comprising the provided {@link SampleSubtype}
	 * @param sampleSubtype
	 * @return List of SampleMeta
	 */
	public List<SampleMeta> getMetaTemplatedToSampleSybtype(SampleSubtype sampleSubtype);
	
	/**
	 * Synchronizes meta data from the provided SampleMeta list with all the sample meta associated with this biological sample.
	 * Existing meta data values are updated and merged, and new meta data is persisted as appropriate. 
	 * Meta data is not persisted if the sample has not itself been persisted
	 * Throws a MetadataException if a supplied SampleMeta object has a sampleId associated with it that is not the same as sampleId 
	 * of the represented Sample entity or any of its ancestors.
	 * The entire list of updated meta is also returned for convenience
	 * @param inputMetaList
	 * @param sampleMetaDao
	 * @return updated SampleMeta list
	 */
	public List<SampleMeta> updateMetaToList(List<SampleMeta> inputMetaList, SampleMetaDao sampleMetaDao);

	
	/**
	 * Adds a parent to the managed sample
	 * @param parentSample
	 * @throws SampleParentChildException
	 */
	public void setParent(Sample parentSample) throws SampleParentChildException;
	
	/**
	 * Save sample object and its metadata to the database. Propagates up the inheritance chain
	 * @param sampleService
	 * @param sampleSourceDao
	 */
	public void saveAll(SampleService sampleService);

	/**
	 * Get wrapped parent sample object (or null)
	 * @return
	 */
	public SampleWrapper getParentWrapper();
	
}
