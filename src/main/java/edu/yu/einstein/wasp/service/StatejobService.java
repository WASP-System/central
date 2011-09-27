
/**
 *
 * StatejobService.java 
 * @author echeng (table2type.pl)
 *  
 * the StatejobService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.model.Statejob;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface StatejobService extends WaspService<Statejob> {

	/**
	 * setStatejobDao(StatejobDao statejobDao)
	 *
	 * @param statejobDao
	 *
	 */
	public void setStatejobDao(StatejobDao statejobDao);

	/**
	 * getStatejobDao();
	 *
	 * @return statejobDao
	 *
	 */
	public StatejobDao getStatejobDao();

  public Statejob getStatejobByStatejobId (final int statejobId);


}

