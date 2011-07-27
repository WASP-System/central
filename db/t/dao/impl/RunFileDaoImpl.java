
/**
 *
 * RunFileImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunFile object
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

import edu.yu.einstein.wasp.model.RunFile;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RunFileDaoImpl extends WaspDaoImpl<RunFile> implements edu.yu.einstein.wasp.dao.RunFileDao {

  public RunFileDaoImpl() {
    super();
    this.entityClass = RunFile.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public RunFile getRunFileByRunlanefileId (final int runlanefileId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM RunFile a WHERE "
       + "a.runlanefileId = :runlanefileId";
     Query query = em.createQuery(queryString);
      query.setParameter("runlanefileId", runlanefileId);

    return query.getResultList();
  }
  });
    List<RunFile> results = (List<RunFile>) res;
    if (results.size() == 0) {
      RunFile rt = new RunFile();
      return rt;
    }
    return (RunFile) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public RunFile getRunFileByFileId (final int fileId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM RunFile a WHERE "
       + "a.fileId = :fileId";
     Query query = em.createQuery(queryString);
      query.setParameter("fileId", fileId);

    return query.getResultList();
  }
  });
    List<RunFile> results = (List<RunFile>) res;
    if (results.size() == 0) {
      RunFile rt = new RunFile();
      return rt;
    }
    return (RunFile) results.get(0);
  }


}

