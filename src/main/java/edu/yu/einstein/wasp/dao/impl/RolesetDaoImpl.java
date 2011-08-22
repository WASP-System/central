
/**
 *
 * RolesetImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Roleset object
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

import edu.yu.einstein.wasp.model.Roleset;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RolesetDaoImpl extends WaspDaoImpl<Roleset> implements edu.yu.einstein.wasp.dao.RolesetDao {

  public RolesetDaoImpl() {
    super();
    this.entityClass = Roleset.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Roleset getRolesetByRolesetId (final int rolesetId) {
    HashMap m = new HashMap();
    m.put("rolesetId", rolesetId);
    List<Roleset> results = (List<Roleset>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Roleset rt = new Roleset();
      return rt;
    }
    return (Roleset) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Roleset getRolesetByParentroleIdChildroleId (final int parentroleId, final int childroleId) {
    HashMap m = new HashMap();
    m.put("parentroleId", parentroleId);
    m.put("childroleId", childroleId);
    List<Roleset> results = (List<Roleset>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Roleset rt = new Roleset();
      return rt;
    }
    return (Roleset) results.get(0);
  }


}

