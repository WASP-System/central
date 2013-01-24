
/**
 *
 * SampleSubtypeMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSubtypeMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.SampleSubtypeMeta;


public interface SampleSubtypeMetaDao extends WaspMetaDao<SampleSubtypeMeta> {

  public SampleSubtypeMeta getSampleSubtypeMetaBySampleSubtypeMetaId (final int sampleSubtypeMetaId);

  public SampleSubtypeMeta getSampleSubtypeMetaByKSampleSubtypeId (final String k, final int sampleSubtypeId);

  List<SampleSubtypeMeta> getSampleSubtypesMetaBySampleSubtypeId (final int sampleSubtypeId);





}

