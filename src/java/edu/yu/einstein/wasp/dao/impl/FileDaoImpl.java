
/**
 *
 * FileImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the File object
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

import edu.yu.einstein.wasp.model.File;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class FileDaoImpl extends WaspDaoImpl<File> implements edu.yu.einstein.wasp.dao.FileDao {

  public FileDaoImpl() {
    super();
    this.entityClass = File.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public File getFileByFileId (final int fileId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM File a WHERE "
       + "a.fileId = :fileId";
     Query query = em.createQuery(queryString);
      query.setParameter("fileId", fileId);

    return query.getResultList();
  }
  });
    List<File> results = (List<File>) res;
    if (results.size() == 0) {
      File rt = new File();
      return rt;
    }
    return (File) results.get(0);
  }


}

