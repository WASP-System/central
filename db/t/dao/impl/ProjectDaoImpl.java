
/**
 *
 * ProjectImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Project object
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

import edu.yu.einstein.wasp.model.Project;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ProjectDaoImpl extends WaspDaoImpl<Project> implements edu.yu.einstein.wasp.dao.ProjectDao {

  public ProjectDaoImpl() {
    super();
    this.entityClass = Project.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Project getProjectByProjectId (final int projectId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Project a WHERE "
       + "a.projectId = :projectId";
     Query query = em.createQuery(queryString);
      query.setParameter("projectId", projectId);

    return query.getResultList();
  }
  });
    List<Project> results = (List<Project>) res;
    if (results.size() == 0) {
      Project rt = new Project();
      return rt;
    }
    return (Project) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Project getProjectByNameLabId (final String name, final int labId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Project a WHERE "
       + "a.name = :name"
       + "AND "+ "a.labId = :labId";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);
      query.setParameter("labId", labId);

    return query.getResultList();
  }
  });
    List<Project> results = (List<Project>) res;
    if (results.size() == 0) {
      Project rt = new Project();
      return rt;
    }
    return (Project) results.get(0);
  }


}

