
/**
 *
 * JobTaskRunlane.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskRunlane object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public interface JobTaskRunlaneDao extends WaspDao<JobTaskRunlane> {

  public JobTaskRunlane getJobTaskRunlaneByJobtaskrunlaneId (final int jobtaskrunlaneId);

}

