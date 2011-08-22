
/**
 *
 * TypeSampleImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSample object
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

import edu.yu.einstein.wasp.model.TypeSample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeSampleDaoImpl extends WaspDaoImpl<TypeSample> implements edu.yu.einstein.wasp.dao.TypeSampleDao {

  public TypeSampleDaoImpl() {
    super();
    this.entityClass = TypeSample.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public TypeSample getTypeSampleByTypeSampleId (final int typeSampleId) {
    HashMap m = new HashMap();
    m.put("typeSampleId", typeSampleId);
    List<TypeSample> results = (List<TypeSample>) this.findByMap((Map) m);
    if (results.size() == 0) {
      TypeSample rt = new TypeSample();
      return rt;
    }
    return (TypeSample) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public TypeSample getTypeSampleByIName (final String iName) {
    HashMap m = new HashMap();
    m.put("iName", iName);
    List<TypeSample> results = (List<TypeSample>) this.findByMap((Map) m);
    if (results.size() == 0) {
      TypeSample rt = new TypeSample();
      return rt;
    }
    return (TypeSample) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public TypeSample getTypeSampleByName (final String name) {
    HashMap m = new HashMap();
    m.put("name", name);
    List<TypeSample> results = (List<TypeSample>) this.findByMap((Map) m);
    if (results.size() == 0) {
      TypeSample rt = new TypeSample();
      return rt;
    }
    return (TypeSample) results.get(0);
  }


}

