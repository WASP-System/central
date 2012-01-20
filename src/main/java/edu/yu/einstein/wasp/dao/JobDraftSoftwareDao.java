
/**
 *
 * JobDraftSoftwareDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftSoftware Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobDraftSoftwareDao extends WaspDao<JobDraftSoftware> {

  public JobDraftSoftware getJobDraftSoftwareByJobDraftSoftwareId (final Integer jobDraftSoftwareId);

  public JobDraftSoftware getJobDraftSoftwareBySoftwareIdJobdraftId (final Integer softwareId, final Integer jobdraftId);


}

