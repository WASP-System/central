
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

import edu.yu.einstein.wasp.dao.LabMetaDao;
import edu.yu.einstein.wasp.model.LabMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface LabMetaService extends WaspService<LabMeta> {

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

  public void updateByLabId (final int labId, final List<LabMeta> metaList);


}

