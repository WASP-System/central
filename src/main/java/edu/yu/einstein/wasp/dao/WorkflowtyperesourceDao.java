
/**
 *
 * WorkflowtyperesourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtyperesource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface WorkflowtyperesourceDao extends WaspDao<Workflowtyperesource> {

  public Workflowtyperesource getWorkflowtyperesourceByWorkflowtyperesourceId (final int workflowtyperesourceId);

  public Workflowtyperesource getWorkflowtyperesourceByWorkflowIdTypeResourceId (final int workflowId, final int typeResourceId);


}

