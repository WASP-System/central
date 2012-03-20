
/**
 *
 * StateMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the StateMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.StateMeta;


public interface StateMetaDao extends WaspDao<StateMeta> {

  public StateMeta getStateMetaByStateMetaId (final int stateMetaId);

  public StateMeta getStateMetaByKStateId (final String k, final int stateId);

  public void updateByStateId (final int stateId, final List<StateMeta> metaList);




}

