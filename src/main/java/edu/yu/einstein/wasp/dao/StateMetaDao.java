
/**
 *
 * StateMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the StateMetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StateMetaDao extends WaspDao<StateMeta> {

  public StateMeta getStateMetaByStateMetaId (final int stateMetaId);

  public StateMeta getStateMetaByKStateId (final String k, final int stateId);

  public void updateByStateId (final int stateId, final List<StateMeta> metaList);

}

