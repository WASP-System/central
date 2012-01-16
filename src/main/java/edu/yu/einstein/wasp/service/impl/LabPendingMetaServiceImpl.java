
/**
 *
 * LabPendingMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.LabPendingMetaDao;
import edu.yu.einstein.wasp.model.LabPendingMeta;
import edu.yu.einstein.wasp.service.LabPendingMetaService;

@Service
public class LabPendingMetaServiceImpl extends WaspMetaServiceImpl<LabPendingMeta> implements LabPendingMetaService {

	/**
	 * labPendingMetaDao;
	 *
	 */
	private LabPendingMetaDao labPendingMetaDao;

	/**
	 * setLabPendingMetaDao(LabPendingMetaDao labPendingMetaDao)
	 *
	 * @param labPendingMetaDao
	 *
	 */
	@Autowired
	public void setLabPendingMetaDao(LabPendingMetaDao labPendingMetaDao) {
		this.labPendingMetaDao = labPendingMetaDao;
		this.setWaspDao(labPendingMetaDao);
	}

	/**
	 * getLabPendingMetaDao();
	 *
	 * @return labPendingMetaDao
	 *
	 */
	public LabPendingMetaDao getLabPendingMetaDao() {
		return this.labPendingMetaDao;
	}


  public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId) {
    return this.getLabPendingMetaDao().getLabPendingMetaByLabPendingMetaId(labPendingMetaId);
  }

  public LabPendingMeta getLabPendingMetaByKLabpendingId (final String k, final int labpendingId) {
    return this.getLabPendingMetaDao().getLabPendingMetaByKLabpendingId(k, labpendingId);
  }

  public void updateByLabpendingId (final String area, final int labpendingId, final List<LabPendingMeta> metaList) {
    this.getLabPendingMetaDao().updateByLabpendingId(area, labpendingId, metaList); 
  }

  public void updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList) {
    this.getLabPendingMetaDao().updateByLabpendingId(labpendingId, metaList); 
  }


}

