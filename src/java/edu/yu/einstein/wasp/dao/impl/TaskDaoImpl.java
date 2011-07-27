
/**
 *
 * TaskImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Task object
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

import edu.yu.einstein.wasp.model.Task;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TaskDaoImpl extends WaspDaoImpl<Task> implements edu.yu.einstein.wasp.dao.TaskDao {

  public TaskDaoImpl() {
    super();
    this.entityClass = Task.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Task getTaskByTaskId (final int taskId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Task a WHERE "
       + "a.taskId = :taskId";
     Query query = em.createQuery(queryString);
      query.setParameter("taskId", taskId);

    return query.getResultList();
  }
  });
    List<Task> results = (List<Task>) res;
    if (results.size() == 0) {
      Task rt = new Task();
      return rt;
    }
    return (Task) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Task getTaskByIName (final String iName) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Task a WHERE "
       + "a.iName = :iName";
     Query query = em.createQuery(queryString);
      query.setParameter("iName", iName);

    return query.getResultList();
  }
  });
    List<Task> results = (List<Task>) res;
    if (results.size() == 0) {
      Task rt = new Task();
      return rt;
    }
    return (Task) results.get(0);
  }


}

