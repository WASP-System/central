
/**
 *
 * WorkflowtasksourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtasksourceService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowtasksourceDao;
import edu.yu.einstein.wasp.model.Workflowtasksource;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowtasksourceService extends WaspService<Workflowtasksource> {

  public void setWorkflowtasksourceDao(WorkflowtasksourceDao workflowtasksourceDao);
  public WorkflowtasksourceDao getWorkflowtasksourceDao();

  public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final int workflowtasksourceId);

}

