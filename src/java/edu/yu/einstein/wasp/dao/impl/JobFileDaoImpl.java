
/**
 *
 * JobFileImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFile object
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

import edu.yu.einstein.wasp.model.JobFile;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobFileDaoImpl extends WaspDaoImpl<JobFile> implements edu.yu.einstein.wasp.dao.JobFileDao {

  public JobFileDaoImpl() {
    super();
    this.entityClass = JobFile.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public JobFile getJobFileByJobFileId (final int jobFileId) {
    HashMap m = new HashMap();
    m.put("jobFileId", jobFileId);
    List<JobFile> results = (List<JobFile>) this.findByMap((Map) m);
    if (results.size() == 0) {
      JobFile rt = new JobFile();
      return rt;
    }
    return (JobFile) results.get(0);
  }


}

