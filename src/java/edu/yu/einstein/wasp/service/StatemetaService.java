
/**
 *
 * StatemetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the StatemetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StatemetaDao;
import edu.yu.einstein.wasp.model.Statemeta;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StatemetaService extends WaspService<Statemeta> {

  public void setStatemetaDao(StatemetaDao statemetaDao);
  public StatemetaDao getStatemetaDao();

  public Statemeta getStatemetaByStatemetaId (final int statemetaId);

  public Statemeta getStatemetaByKStateId (final String k, final int stateId);

  public void updateByStateId (final int stateId, final List<Statemeta> metaList);

}

