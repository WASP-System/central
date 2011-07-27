
/**
 *
 * StatemetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the StatemetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StatemetaDao extends WaspDao<Statemeta> {

  public Statemeta getStatemetaByStatemetaId (final int statemetaId);

  public Statemeta getStatemetaByKStateId (final String k, final int stateId);

  public void updateByStateId (final int stateId, final List<Statemeta> metaList);

}

