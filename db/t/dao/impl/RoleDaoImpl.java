
/**
 *
 * RoleImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Role object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Role;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RoleDaoImpl extends WaspDaoImpl<Role> implements edu.yu.einstein.wasp.dao.RoleDao {

  public RoleDaoImpl() {
    super();
    this.entityClass = Role.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Role getRoleByRoleId (final int roleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Role a WHERE "
       + "a.roleId = :roleId";
     Query query = em.createQuery(queryString);
      query.setParameter("roleId", roleId);

    return query.getResultList();
  }
  });
    List<Role> results = (List<Role>) res;
    if (results.size() == 0) {
      Role rt = new Role();
      return rt;
    }
    return (Role) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Role getRoleByRoleName (final String roleName) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Role a WHERE "
       + "a.roleName = :roleName";
     Query query = em.createQuery(queryString);
      query.setParameter("roleName", roleName);

    return query.getResultList();
  }
  });
    List<Role> results = (List<Role>) res;
    if (results.size() == 0) {
      Role rt = new Role();
      return rt;
    }
    return (Role) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Role getRoleByName (final String name) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Role a WHERE "
       + "a.name = :name";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);

    return query.getResultList();
  }
  });
    List<Role> results = (List<Role>) res;
    if (results.size() == 0) {
      Role rt = new Role();
      return rt;
    }
    return (Role) results.get(0);
  }


}

