
/**
 *
 * StaterunlaneDao.java 
 * @author echeng (table2type.pl)
 *  
 * the StaterunlaneDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StaterunlaneDao extends WaspDao<Staterunlane> {

  public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId);

}

