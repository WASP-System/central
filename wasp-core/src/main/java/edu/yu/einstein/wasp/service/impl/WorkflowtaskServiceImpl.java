
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
	@Override
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
	@Override
	public WorkflowtaskDao getWorkflowtaskDao() {
		return this.workflowtaskDao;
	}


  @Override
public Workflowtask getWorkflowtaskByWorkflowtaskId (final Integer workflowtaskId) {
    return this.getWorkflowtaskDao().getWorkflowtaskByWorkflowtaskId(workflowtaskId);
  }

  @Override
public Workflowtask getWorkflowtaskByIName (final String iName) {
    return this.getWorkflowtaskDao().getWorkflowtaskByIName(iName);
  }

}

