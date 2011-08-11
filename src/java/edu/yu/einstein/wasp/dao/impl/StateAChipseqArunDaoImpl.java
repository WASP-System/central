
/**
 *
 * StateAChipseqArunImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StateAChipseqArun object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.StateAChipseqArun;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StateAChipseqArunDaoImpl extends WaspDaoImpl<StateAChipseqArun> implements edu.yu.einstein.wasp.dao.StateAChipseqArunDao {

  public StateAChipseqArunDaoImpl() {
    super();
    this.entityClass = StateAChipseqArun.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public StateAChipseqArun getStateAChipseqArunByStateArunId (final int stateArunId) {
    HashMap m = new HashMap();
    m.put("stateArunId", stateArunId);
    List<StateAChipseqArun> results = (List<StateAChipseqArun>) this.findByMap((Map) m);
    if (results.size() == 0) {
      StateAChipseqArun rt = new StateAChipseqArun();
      return rt;
    }
    return (StateAChipseqArun) results.get(0);
  }


}

