
/**
 *
 * LabMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.LabMetaDao;
import edu.yu.einstein.wasp.model.LabMeta;

@Service
public interface LabMetaService extends WaspMetaService<LabMeta> {

	/**
	 * setLabMetaDao(LabMetaDao labMetaDao)
	 *
	 * @param labMetaDao
	 *
	 */
	public void setLabMetaDao(LabMetaDao labMetaDao);

	/**
	 * getLabMetaDao();
	 *
	 * @return labMetaDao
	 *
	 */
	public LabMetaDao getLabMetaDao();

  public LabMeta getLabMetaByLabMetaId (final int labMetaId);

  public LabMeta getLabMetaByKLabId (final String k, final int labId);


  public void updateByLabId (final String area, final int labId, final List<LabMeta> metaList);

  public void updateByLabId (final int labId, final List<LabMeta> metaList);


}

