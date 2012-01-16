
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.LabMetaDao;
import edu.yu.einstein.wasp.model.LabMeta;
import edu.yu.einstein.wasp.service.LabMetaService;

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

