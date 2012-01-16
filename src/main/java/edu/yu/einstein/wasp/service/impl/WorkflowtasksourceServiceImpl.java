
/**
 *
 * WorkflowtasksourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtasksourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowtasksourceDao;
import edu.yu.einstein.wasp.model.Workflowtasksource;
import edu.yu.einstein.wasp.service.WorkflowtasksourceService;

@Service
public class WorkflowtasksourceServiceImpl extends WaspServiceImpl<Workflowtasksource> implements WorkflowtasksourceService {

	/**
	 * workflowtasksourceDao;
	 *
	 */
	private WorkflowtasksourceDao workflowtasksourceDao;

	/**
	 * setWorkflowtasksourceDao(WorkflowtasksourceDao workflowtasksourceDao)
	 *
	 * @param workflowtasksourceDao
	 *
	 */
	@Autowired
	public void setWorkflowtasksourceDao(WorkflowtasksourceDao workflowtasksourceDao) {
		this.workflowtasksourceDao = workflowtasksourceDao;
		this.setWaspDao(workflowtasksourceDao);
	}

	/**
	 * getWorkflowtasksourceDao();
	 *
	 * @return workflowtasksourceDao
	 *
	 */
	public WorkflowtasksourceDao getWorkflowtasksourceDao() {
		return this.workflowtasksourceDao;
	}


  public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final int workflowtasksourceId) {
    return this.getWorkflowtasksourceDao().getWorkflowtasksourceByWorkflowtasksourceId(workflowtasksourceId);
  }

}

