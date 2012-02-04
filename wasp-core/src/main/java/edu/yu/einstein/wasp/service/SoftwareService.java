
/**
 *
 * SoftwareService.java 
 * @author echeng (table2type.pl)
 *  
 * the SoftwareService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.model.Software;

@Service
public interface SoftwareService extends WaspService<Software> {

	/**
	 * setSoftwareDao(SoftwareDao softwareDao)
	 *
	 * @param softwareDao
	 *
	 */
	public void setSoftwareDao(SoftwareDao softwareDao);

	/**
	 * getSoftwareDao();
	 *
	 * @return softwareDao
	 *
	 */
	public SoftwareDao getSoftwareDao();

  public Software getSoftwareBySoftwareId (final Integer softwareId);

  public Software getSoftwareByIName (final String iName);

  public Software getSoftwareByName (final String name);


}

