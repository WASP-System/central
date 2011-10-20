
/**
 *
 * LabPendingMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.LabPendingMetaDao;
import edu.yu.einstein.wasp.model.LabPendingMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface LabPendingMetaService extends WaspMetaService<LabPendingMeta> {

	/**
	 * setLabPendingMetaDao(LabPendingMetaDao labPendingMetaDao)
	 *
	 * @param labPendingMetaDao
	 *
	 */
	public void setLabPendingMetaDao(LabPendingMetaDao labPendingMetaDao);

	/**
	 * getLabPendingMetaDao();
	 *
	 * @return labPendingMetaDao
	 *
	 */
	public LabPendingMetaDao getLabPendingMetaDao();

  public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId);

  public LabPendingMeta getLabPendingMetaByKLabpendingId (final String k, final int labpendingId);


  public void updateByLabpendingId (final String area, final int labpendingId, final List<LabPendingMeta> metaList);

  public void updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList);


}

