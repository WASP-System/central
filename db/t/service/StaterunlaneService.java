
/**
 *
 * StaterunlaneService.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunlaneService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StaterunlaneDao;
import edu.yu.einstein.wasp.model.Staterunlane;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StaterunlaneService extends WaspService<Staterunlane> {

  public void setStaterunlaneDao(StaterunlaneDao staterunlaneDao);
  public StaterunlaneDao getStaterunlaneDao();

  public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId);

}

