
/**
 *
 * StatesampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the StatesampleService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.model.Statesample;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StatesampleService extends WaspService<Statesample> {

  public void setStatesampleDao(StatesampleDao statesampleDao);
  public StatesampleDao getStatesampleDao();

  public Statesample getStatesampleByStatesampleId (final int statesampleId);

}

