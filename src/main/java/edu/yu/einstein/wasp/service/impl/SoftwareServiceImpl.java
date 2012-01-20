
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

import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Software;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public SoftwareDao getSoftwareDao() {
		return this.softwareDao;
	}


  public Software getSoftwareBySoftwareId (final Integer softwareId) {
    return this.getSoftwareDao().getSoftwareBySoftwareId(softwareId);
  }

  public Software getSoftwareByIName (final String iName) {
    return this.getSoftwareDao().getSoftwareByIName(iName);
  }

  public Software getSoftwareByName (final String name) {
    return this.getSoftwareDao().getSoftwareByName(name);
  }

}

