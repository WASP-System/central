
/**
 *
 * WorkflowtasksourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtasksourceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowtasksourceDao;
import edu.yu.einstein.wasp.model.Workflowtasksource;

@Service
public interface WorkflowtasksourceService extends WaspService<Workflowtasksource> {

	/**
	 * setWorkflowtasksourceDao(WorkflowtasksourceDao workflowtasksourceDao)
	 *
	 * @param workflowtasksourceDao
	 *
	 */
	public void setWorkflowtasksourceDao(WorkflowtasksourceDao workflowtasksourceDao);

	/**
	 * getWorkflowtasksourceDao();
	 *
	 * @return workflowtasksourceDao
	 *
	 */
	public WorkflowtasksourceDao getWorkflowtasksourceDao();

  public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final Integer workflowtasksourceId);


}

