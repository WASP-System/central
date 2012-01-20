
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

import edu.yu.einstein.wasp.dao.WorkflowtasksourceDao;
import edu.yu.einstein.wasp.model.Workflowtasksource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

