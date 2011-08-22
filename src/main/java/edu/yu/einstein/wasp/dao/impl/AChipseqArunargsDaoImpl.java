
/**
 *
 * AChipseqArunargsImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunargs object
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

import edu.yu.einstein.wasp.model.AChipseqArunargs;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AChipseqArunargsDaoImpl extends WaspDaoImpl<AChipseqArunargs> implements edu.yu.einstein.wasp.dao.AChipseqArunargsDao {

  public AChipseqArunargsDaoImpl() {
    super();
    this.entityClass = AChipseqArunargs.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AChipseqArunargs getAChipseqArunargsByArunargsId (final int arunargsId) {
    HashMap m = new HashMap();
    m.put("arunargsId", arunargsId);
    List<AChipseqArunargs> results = (List<AChipseqArunargs>) this.findByMap((Map) m);
    if (results.size() == 0) {
      AChipseqArunargs rt = new AChipseqArunargs();
      return rt;
    }
    return (AChipseqArunargs) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public AChipseqArunargs getAChipseqArunargsByArunIdArgc (final Integer arunId, final int argc) {
    HashMap m = new HashMap();
    m.put("arunId", arunId);
    m.put("argc", argc);
    List<AChipseqArunargs> results = (List<AChipseqArunargs>) this.findByMap((Map) m);
    if (results.size() == 0) {
      AChipseqArunargs rt = new AChipseqArunargs();
      return rt;
    }
    return (AChipseqArunargs) results.get(0);
  }


}

