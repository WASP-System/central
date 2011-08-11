
/**
 *
 * StatejobImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Statejob object
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

import edu.yu.einstein.wasp.model.Statejob;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StatejobDaoImpl extends WaspDaoImpl<Statejob> implements edu.yu.einstein.wasp.dao.StatejobDao {

  public StatejobDaoImpl() {
    super();
    this.entityClass = Statejob.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Statejob getStatejobByStatejobId (final int statejobId) {
    HashMap m = new HashMap();
    m.put("statejobId", statejobId);
    List<Statejob> results = (List<Statejob>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Statejob rt = new Statejob();
      return rt;
    }
    return (Statejob) results.get(0);
  }


}

