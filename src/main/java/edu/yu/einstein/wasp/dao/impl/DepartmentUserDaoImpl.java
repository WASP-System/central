
/**
 *
 * DepartmentUserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUser object
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

import edu.yu.einstein.wasp.model.DepartmentUser;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DepartmentUserDaoImpl extends WaspDaoImpl<DepartmentUser> implements edu.yu.einstein.wasp.dao.DepartmentUserDao {

  public DepartmentUserDaoImpl() {
    super();
    this.entityClass = DepartmentUser.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId) {
    HashMap m = new HashMap();
    m.put("departmentUserId", departmentUserId);
    List<DepartmentUser> results = (List<DepartmentUser>) this.findByMap((Map) m);
    if (results.size() == 0) {
      DepartmentUser rt = new DepartmentUser();
      return rt;
    }
    return (DepartmentUser) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int UserId) {
    HashMap m = new HashMap();
    m.put("departmentId", departmentId);
    m.put("UserId", UserId);
    List<DepartmentUser> results = (List<DepartmentUser>) this.findByMap((Map) m);
    if (results.size() == 0) {
      DepartmentUser rt = new DepartmentUser();
      return rt;
    }
    return (DepartmentUser) results.get(0);
  }


}

