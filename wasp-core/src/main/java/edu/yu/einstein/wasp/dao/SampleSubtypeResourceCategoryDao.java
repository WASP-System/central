
/**
 *
 * SubypeSampleResourceCategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSubtypeResourceCategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;


public interface SampleSubtypeResourceCategoryDao extends WaspDao<SampleSubtypeResourceCategory> {

	public SampleSubtypeResourceCategory getSampleSubtypeResourceCategoryBySampleSubtypeResourceCategoryId (final Integer sampleSubtypeResourceCategoryId);
	
	public SampleSubtypeResourceCategory getSampleSubtypeResourceCategoryBySampleSubtypeIdResourceCategoryId (final Integer sampleSubtypeId, final Integer resourceCategoryId);
	
}

