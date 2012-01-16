
/**
 *
 * StatejobServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StatejobService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.service.StatejobService;

@Service
public class StatejobServiceImpl extends WaspServiceImpl<Statejob> implements StatejobService {

	/**
	 * statejobDao;
	 *
	 */
	private StatejobDao statejobDao;

	/**
	 * setStatejobDao(StatejobDao statejobDao)
	 *
	 * @param statejobDao
	 *
	 */
	@Override
	@Autowired
	public void setStatejobDao(StatejobDao statejobDao) {
		this.statejobDao = statejobDao;
		this.setWaspDao(statejobDao);
	}

	/**
	 * getStatejobDao();
	 *
	 * @return statejobDao
	 *
	 */
	@Override
	public StatejobDao getStatejobDao() {
		return this.statejobDao;
	}


  @Override
public Statejob getStatejobByStatejobId (final int statejobId) {
    return this.getStatejobDao().getStatejobByStatejobId(statejobId);
  }

}

