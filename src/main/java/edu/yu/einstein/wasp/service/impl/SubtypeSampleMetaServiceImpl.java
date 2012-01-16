
/**
 *
 * SubtypeSampleMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.SubtypeSampleMetaService;
import edu.yu.einstein.wasp.dao.SubtypeSampleMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SubtypeSampleMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubtypeSampleMetaServiceImpl extends WaspMetaServiceImpl<SubtypeSampleMeta> implements SubtypeSampleMetaService {

	/**
	 * subtypeSampleMetaDao;
	 *
	 */
	private SubtypeSampleMetaDao subtypeSampleMetaDao;

	/**
	 * setSubtypeSampleMetaDao(SubtypeSampleMetaDao subtypeSampleMetaDao)
	 *
	 * @param subtypeSampleMetaDao
	 *
	 */
	@Autowired
	public void setSubtypeSampleMetaDao(SubtypeSampleMetaDao subtypeSampleMetaDao) {
		this.subtypeSampleMetaDao = subtypeSampleMetaDao;
		this.setWaspDao(subtypeSampleMetaDao);
	}

	/**
	 * getSubtypeSampleMetaDao();
	 *
	 * @return subtypeSampleMetaDao
	 *
	 */
	public SubtypeSampleMetaDao getSubtypeSampleMetaDao() {
		return this.subtypeSampleMetaDao;
	}


  public SubtypeSampleMeta getSubtypeSampleMetaBySubtypeSampleMetaId (final int subtypeSampleMetaId) {
    return this.getSubtypeSampleMetaDao().getSubtypeSampleMetaBySubtypeSampleMetaId(subtypeSampleMetaId);
  }

  public SubtypeSampleMeta getSubtypeSampleMetaByKSubtypeSampleId (final String k, final int subtypeSampleId) {
    return this.getSubtypeSampleMetaDao().getSubtypeSampleMetaByKSubtypeSampleId(k, subtypeSampleId);
  }

  public void updateBySubtypeSampleId (final String area, final int subtypeSampleId, final List<SubtypeSampleMeta> metaList) {
    this.getSubtypeSampleMetaDao().updateBySubtypeSampleId(area, subtypeSampleId, metaList); 
  }

  public void updateBySubtypeSampleId (final int subtypeSampleId, final List<SubtypeSampleMeta> metaList) {
    this.getSubtypeSampleMetaDao().updateBySubtypeSampleId(subtypeSampleId, metaList); 
  }

  public  List<SubtypeSampleMeta> getSubtypeSamplesMetaBySubtypeSampleId (final int subtypeSampleId) {
    return getSubtypeSampleMetaDao().getSubtypeSamplesMetaBySubtypeSampleId(subtypeSampleId);
  }


}

