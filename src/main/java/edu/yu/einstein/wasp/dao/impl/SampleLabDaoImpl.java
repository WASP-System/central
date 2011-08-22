
/**
 *
 * SampleLabImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLab object
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

import edu.yu.einstein.wasp.model.SampleLab;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleLabDaoImpl extends WaspDaoImpl<SampleLab> implements edu.yu.einstein.wasp.dao.SampleLabDao {

  public SampleLabDaoImpl() {
    super();
    this.entityClass = SampleLab.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public SampleLab getSampleLabBySampleLabId (final int sampleLabId) {
    HashMap m = new HashMap();
    m.put("sampleLabId", sampleLabId);
    List<SampleLab> results = (List<SampleLab>) this.findByMap((Map) m);
    if (results.size() == 0) {
      SampleLab rt = new SampleLab();
      return rt;
    }
    return (SampleLab) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId) {
    HashMap m = new HashMap();
    m.put("sampleId", sampleId);
    m.put("labId", labId);
    List<SampleLab> results = (List<SampleLab>) this.findByMap((Map) m);
    if (results.size() == 0) {
      SampleLab rt = new SampleLab();
      return rt;
    }
    return (SampleLab) results.get(0);
  }


}

