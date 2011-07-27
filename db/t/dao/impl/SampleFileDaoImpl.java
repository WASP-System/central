
/**
 *
 * SampleFileImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFile object
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

import edu.yu.einstein.wasp.model.SampleFile;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleFileDaoImpl extends WaspDaoImpl<SampleFile> implements edu.yu.einstein.wasp.dao.SampleFileDao {

  public SampleFileDaoImpl() {
    super();
    this.entityClass = SampleFile.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public SampleFile getSampleFileBySampleFileId (final int sampleFileId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleFile a WHERE "
       + "a.sampleFileId = :sampleFileId";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleFileId", sampleFileId);

    return query.getResultList();
  }
  });
    List<SampleFile> results = (List<SampleFile>) res;
    if (results.size() == 0) {
      SampleFile rt = new SampleFile();
      return rt;
    }
    return (SampleFile) results.get(0);
  }


}

