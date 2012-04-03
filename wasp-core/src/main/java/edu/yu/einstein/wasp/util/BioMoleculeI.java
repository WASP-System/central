package edu.yu.einstein.wasp.util;

import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;

/**
 * An interface that describes a biological molecule
 * @author ASMcLellan
 *
 */
public interface BioMoleculeI {

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
	 * Throws a MetadataException if a supplied SampleMeta object has a sampleId associated with it that is not the same as sampleId 
	 * of the represented Sample entity or any of its ancestors.
	 * @param inputMetaList
	 * @param sampleMetaDao
	 * @throws MetadataException
	 */
	public void updateMetaToListAndSave(List<SampleMeta> inputMetaList, SampleMetaDao sampleMetaDao) throws MetadataException;
	
}
