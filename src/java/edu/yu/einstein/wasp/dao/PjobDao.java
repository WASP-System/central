
/**
 *
 * Pjob.java 
 * @author echeng (table2type.pl)
 *  
 * the Pjob object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public interface PjobDao extends WaspDao<Pjob> {

  public Pjob getPjobByPjobId (final int pjobId);

}

