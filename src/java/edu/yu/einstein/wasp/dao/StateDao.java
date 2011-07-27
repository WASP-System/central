
/**
 *
 * StateDao.java 
 * @author echeng (table2type.pl)
 *  
 * the StateDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StateDao extends WaspDao<State> {

  public State getStateByStateId (final int stateId);

}

