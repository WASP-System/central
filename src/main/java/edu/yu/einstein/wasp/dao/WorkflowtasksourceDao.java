
/**
 *
 * WorkflowtasksourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtasksource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowtasksourceDao extends WaspDao<Workflowtasksource> {

  public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final int workflowtasksourceId);


}

