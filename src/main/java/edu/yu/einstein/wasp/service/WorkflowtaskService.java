
/**
 *
 * WorkflowtaskService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtaskService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.WorkflowtaskDao;
import edu.yu.einstein.wasp.model.Workflowtask;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WorkflowtaskService extends WaspService<Workflowtask> {

	/**
	 * setWorkflowtaskDao(WorkflowtaskDao workflowtaskDao)
	 *
	 * @param workflowtaskDao
	 *
	 */
	public void setWorkflowtaskDao(WorkflowtaskDao workflowtaskDao);

	/**
	 * getWorkflowtaskDao();
	 *
	 * @return workflowtaskDao
	 *
	 */
	public WorkflowtaskDao getWorkflowtaskDao();

  public Workflowtask getWorkflowtaskByWorkflowtaskId (final Integer workflowtaskId);

  public Workflowtask getWorkflowtaskByIName (final String iName);


}

