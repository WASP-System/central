
/**
 *
 * WorkflowtaskService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtaskService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowtaskDao;
import edu.yu.einstein.wasp.model.Workflowtask;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowtaskService extends WaspService<Workflowtask> {

  public void setWorkflowtaskDao(WorkflowtaskDao workflowtaskDao);
  public WorkflowtaskDao getWorkflowtaskDao();

  public Workflowtask getWorkflowtaskByWorkflowtaskId (final int workflowtaskId);

}

