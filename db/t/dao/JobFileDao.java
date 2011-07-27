
/**
 *
 * JobFileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFileDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobFileDao extends WaspDao<JobFile> {

  public JobFile getJobFileByJobFileId (final int jobFileId);

}

