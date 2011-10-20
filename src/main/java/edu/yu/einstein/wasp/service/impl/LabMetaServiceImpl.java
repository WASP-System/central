
/**
 *
 * LabMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.LabMetaService;
import edu.yu.einstein.wasp.dao.LabMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.LabMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabMetaServiceImpl extends WaspMetaServiceImpl<LabMeta> implements LabMetaService {

	/**
	 * labMetaDao;
	 *
	 */
	private LabMetaDao labMetaDao;

	/**
	 * setLabMetaDao(LabMetaDao labMetaDao)
	 *
	 * @param labMetaDao
	 *
	 */
	@Autowired
	public void setLabMetaDao(LabMetaDao labMetaDao) {
		this.labMetaDao = labMetaDao;
		this.setWaspDao(labMetaDao);
	}

	/**
	 * getLabMetaDao();
	 *
	 * @return labMetaDao
	 *
	 */
	public LabMetaDao getLabMetaDao() {
		return this.labMetaDao;
	}


  public LabMeta getLabMetaByLabMetaId (final int labMetaId) {
    return this.getLabMetaDao().getLabMetaByLabMetaId(labMetaId);
  }

  public LabMeta getLabMetaByKLabId (final String k, final int labId) {
    return this.getLabMetaDao().getLabMetaByKLabId(k, labId);
  }

  public void updateByLabId (final String area, final int labId, final List<LabMeta> metaList) {
    this.getLabMetaDao().updateByLabId(area, labId, metaList); 
  }

  public void updateByLabId (final int labId, final List<LabMeta> metaList) {
    this.getLabMetaDao().updateByLabId(labId, metaList); 
  }


}

