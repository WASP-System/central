
/**
 *
 * StatesampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Statesample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StatesampleDao extends WaspDao<Statesample> {

  public Statesample getStatesampleByStatesampleId (final int statesampleId);


}

