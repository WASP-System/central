
/**
 *
 * StaterunService.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StaterunDao;
import edu.yu.einstein.wasp.model.Staterun;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StaterunService extends WaspService<Staterun> {

  public void setStaterunDao(StaterunDao staterunDao);
  public StaterunDao getStaterunDao();

  public Staterun getStaterunByStaterunId (final int staterunId);

}

