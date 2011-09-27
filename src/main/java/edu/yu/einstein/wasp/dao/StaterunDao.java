
/**
 *
 * StaterunDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterun Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StaterunDao extends WaspDao<Staterun> {

  public Staterun getStaterunByStaterunId (final int staterunId);


}

