
/**
 *
 * WorkflowtaskServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtaskService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WorkflowtaskDao;
import edu.yu.einstein.wasp.model.Workflowtask;
import edu.yu.einstein.wasp.service.WorkflowtaskService;

@Service
public class WorkflowtaskServiceImpl extends WaspServiceImpl<Workflowtask> implements WorkflowtaskService {

	/**
	 * workflowtaskDao;
	 *
	 */
	private WorkflowtaskDao workflowtaskDao;

	/**
	 * setWorkflowtaskDao(WorkflowtaskDao workflowtaskDao)
	 *
	 * @param workflowtaskDao
	 *
	 */
	@Autowired
	public void setWorkflowtaskDao(WorkflowtaskDao workflowtaskDao) {
		this.workflowtaskDao = workflowtaskDao;
		this.setWaspDao(workflowtaskDao);
	}

	/**
	 * getWorkflowtaskDao();
	 *
	 * @return workflowtaskDao
	 *
	 */
	public WorkflowtaskDao getWorkflowtaskDao() {
		return this.workflowtaskDao;
	}


  public Workflowtask getWorkflowtaskByWorkflowtaskId (final int workflowtaskId) {
    return this.getWorkflowtaskDao().getWorkflowtaskByWorkflowtaskId(workflowtaskId);
  }

}

