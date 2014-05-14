
/**
 *
 * SampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Sample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.Sample;


public interface SampleDao extends WaspDao<Sample> {

	public Sample getSampleBySampleId (final int sampleId);
  
	public List<Sample> getSamplesByJobId (final int jobId);

	public Sample getSampleByName(final String name);

	public List<Sample> getPlatformUnits();
	
	public List<Sample> getPlatformUnitsOrderByDescending();

	public List<Sample> getActiveSamples();

	public List<Sample> getActiveLibraries();

	public List<Sample> getActiveBiomolecules();

}

