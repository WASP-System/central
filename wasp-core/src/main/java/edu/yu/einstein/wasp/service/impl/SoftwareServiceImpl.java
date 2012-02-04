
/**
 *
 * SoftwareServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SoftwareService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.SoftwareService;

@Service
public class SoftwareServiceImpl extends WaspServiceImpl<Software> implements SoftwareService {

	/**
	 * softwareDao;
	 *
	 */
	private SoftwareDao softwareDao;

	/**
	 * setSoftwareDao(SoftwareDao softwareDao)
	 *
	 * @param softwareDao
	 *
	 */
	@Override
	@Autowired
	public void setSoftwareDao(SoftwareDao softwareDao) {
		this.softwareDao = softwareDao;
		this.setWaspDao(softwareDao);
	}

	/**
	 * getSoftwareDao();
	 *
	 * @return softwareDao
	 *
	 */
	@Override
	public SoftwareDao getSoftwareDao() {
		return this.softwareDao;
	}


  @Override
public Software getSoftwareBySoftwareId (final Integer softwareId) {
    return this.getSoftwareDao().getSoftwareBySoftwareId(softwareId);
  }

  @Override
public Software getSoftwareByIName (final String iName) {
    return this.getSoftwareDao().getSoftwareByIName(iName);
  }

  @Override
public Software getSoftwareByName (final String name) {
    return this.getSoftwareDao().getSoftwareByName(name);
  }

}

