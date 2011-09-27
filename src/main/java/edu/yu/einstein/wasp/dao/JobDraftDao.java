
/**
 *
 * JobDraftDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraft Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobDraftDao extends WaspDao<JobDraft> {

  public JobDraft getJobDraftByJobDraftId (final int jobDraftId);


}

