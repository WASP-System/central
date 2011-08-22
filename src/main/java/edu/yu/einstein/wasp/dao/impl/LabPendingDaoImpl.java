
/**
 *
 * LabPendingImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPending object
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

import edu.yu.einstein.wasp.model.LabPending;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabPendingDaoImpl extends WaspDaoImpl<LabPending> implements edu.yu.einstein.wasp.dao.LabPendingDao {

  public LabPendingDaoImpl() {
    super();
    this.entityClass = LabPending.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public LabPending getLabPendingByLabPendingId (final int labPendingId) {
    HashMap m = new HashMap();
    m.put("labPendingId", labPendingId);
    List<LabPending> results = (List<LabPending>) this.findByMap((Map) m);
    if (results.size() == 0) {
      LabPending rt = new LabPending();
      return rt;
    }
    return (LabPending) results.get(0);
  }


}

