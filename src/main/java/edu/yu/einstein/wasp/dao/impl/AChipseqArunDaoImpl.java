
/**
 *
 * AChipseqArunImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArun object
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

import edu.yu.einstein.wasp.model.AChipseqArun;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AChipseqArunDaoImpl extends WaspDaoImpl<AChipseqArun> implements edu.yu.einstein.wasp.dao.AChipseqArunDao {

  public AChipseqArunDaoImpl() {
    super();
    this.entityClass = AChipseqArun.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AChipseqArun getAChipseqArunByArunId (final int arunId) {
    HashMap m = new HashMap();
    m.put("arunId", arunId);
    List<AChipseqArun> results = (List<AChipseqArun>) this.findByMap((Map) m);
    if (results.size() == 0) {
      AChipseqArun rt = new AChipseqArun();
      return rt;
    }
    return (AChipseqArun) results.get(0);
  }


}

