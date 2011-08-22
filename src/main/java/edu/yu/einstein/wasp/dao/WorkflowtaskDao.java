
/**
 *
 * WorkflowtaskDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtaskDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowtaskDao extends WaspDao<Workflowtask> {

  public Workflowtask getWorkflowtaskByWorkflowtaskId (final int workflowtaskId);

}

