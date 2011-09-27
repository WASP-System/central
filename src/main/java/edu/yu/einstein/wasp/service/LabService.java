
/**
 *
 * LabService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.model.Lab;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface LabService extends WaspService<Lab> {

	/**
	 * setLabDao(LabDao labDao)
	 *
	 * @param labDao
	 *
	 */
	public void setLabDao(LabDao labDao);

	/**
	 * getLabDao();
	 *
	 * @return labDao
	 *
	 */
	public LabDao getLabDao();

  public Lab getLabByLabId (final int labId);

  public Lab getLabByName (final String name);

  public Lab getLabByPrimaryUserId (final int primaryUserId);


}

