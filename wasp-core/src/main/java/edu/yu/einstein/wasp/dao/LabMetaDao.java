
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

import edu.yu.einstein.wasp.model.LabMeta;


public interface LabMetaDao extends WaspMetaDao<LabMeta> {

  public LabMeta getLabMetaByLabMetaId (final int labMetaId);

  public LabMeta getLabMetaByKLabId (final String k, final int labId);

 


}

