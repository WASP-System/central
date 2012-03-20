
/**
 *
 * LabMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.LabMeta;


public interface LabMetaDao extends WaspDao<LabMeta> {

  public LabMeta getLabMetaByLabMetaId (final int labMetaId);

  public LabMeta getLabMetaByKLabId (final String k, final int labId);

  public void updateByLabId (final int labId, final List<LabMeta> metaList);




}

