
/**
 *
 * SubtypeSampleMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.SubtypeSampleMeta;


public interface SubtypeSampleMetaDao extends WaspDao<SubtypeSampleMeta> {

  public SubtypeSampleMeta getSubtypeSampleMetaBySubtypeSampleMetaId (final int subtypeSampleMetaId);

  public SubtypeSampleMeta getSubtypeSampleMetaByKSubtypeSampleId (final String k, final int subtypeSampleId);

  List<SubtypeSampleMeta> getSubtypeSamplesMetaBySubtypeSampleId (final int subtypeSampleId);

  public void updateBySubtypeSampleId (final int subtypeSampleId, final List<SubtypeSampleMeta> metaList);




}

