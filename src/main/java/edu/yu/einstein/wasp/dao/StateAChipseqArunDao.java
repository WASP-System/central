
/**
 *
 * StateAChipseqArunDao.java 
 * @author echeng (table2type.pl)
 *  
 * the StateAChipseqArunDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StateAChipseqArunDao extends WaspDao<StateAChipseqArun> {

  public StateAChipseqArun getStateAChipseqArunByStateArunId (final int stateArunId);

}

