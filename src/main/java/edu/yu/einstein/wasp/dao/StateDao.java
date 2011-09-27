
/**
 *
 * StateDao.java 
 * @author echeng (table2type.pl)
 *  
 * the State Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StateDao extends WaspDao<State> {

  public State getStateByStateId (final int stateId);


}

