
/**
 *
 * LabPendingMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.LabPendingMeta;


public interface LabPendingMetaDao extends WaspDao<LabPendingMeta> {

  public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId);

  public LabPendingMeta getLabPendingMetaByKLabpendingId (final String k, final int labpendingId);


  public void updateByLabpendingId (final String area, final int labpendingId, final List<LabPendingMeta> metaList);

  public void updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList);




}

