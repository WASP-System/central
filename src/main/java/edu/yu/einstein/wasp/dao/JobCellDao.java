
/**
 *
 * JobCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobCellDao extends WaspDao<JobCell> {

  public JobCell getJobCellByJobCellId (final int jobCellId);

  public JobCell getJobCellByJobIdCellindex (final int jobId, final int cellindex);


}

