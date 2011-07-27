
/**
 *
 * StaterunDao.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StaterunDao extends WaspDao<Staterun> {

  public Staterun getStaterunByStaterunId (final int staterunId);

}

