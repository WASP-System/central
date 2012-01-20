
/**
 *
 * JobDraftCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobDraftCellDao extends WaspDao<JobDraftCell> {

  public JobDraftCell getJobDraftCellByJobDraftCellId (final Integer jobDraftCellId);

  public JobDraftCell getJobDraftCellByJobdraftIdCellindex (final Integer jobdraftId, final Integer cellindex);


}

