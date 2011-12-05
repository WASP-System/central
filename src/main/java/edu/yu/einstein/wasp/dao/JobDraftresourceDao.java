
/**
 *
 * JobDraftresourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobDraftresourceDao extends WaspDao<JobDraftresource> {

  public JobDraftresource getJobDraftresourceByJobDraftresourceId (final int jobDraftresourceId);

  public JobDraftresource getJobDraftresourceByResourceIdJobdraftId (final int resourceId, final int jobdraftId);


}

