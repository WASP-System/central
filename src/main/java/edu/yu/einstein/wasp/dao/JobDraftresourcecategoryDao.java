
/**
 *
 * JobDraftresourcecategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresourcecategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobDraftresourcecategoryDao extends WaspDao<JobDraftresourcecategory> {

  public JobDraftresourcecategory getJobDraftresourcecategoryByJobDraftresourcecategoryId (final Integer jobDraftresourcecategoryId);

  public JobDraftresourcecategory getJobDraftresourcecategoryByResourcecategoryIdJobdraftId (final Integer resourcecategoryId, final Integer jobdraftId);


}

