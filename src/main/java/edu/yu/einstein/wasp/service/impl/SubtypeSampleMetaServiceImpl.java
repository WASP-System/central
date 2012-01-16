
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SubtypeSampleMetaDao;
import edu.yu.einstein.wasp.model.SubtypeSampleMeta;
import edu.yu.einstein.wasp.service.SubtypeSampleMetaService;

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
	@Override
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
	@Override
	public SubtypeSampleMetaDao getSubtypeSampleMetaDao() {
		return this.subtypeSampleMetaDao;
	}


  @Override
public SubtypeSampleMeta getSubtypeSampleMetaBySubtypeSampleMetaId (final int subtypeSampleMetaId) {
    return this.getSubtypeSampleMetaDao().getSubtypeSampleMetaBySubtypeSampleMetaId(subtypeSampleMetaId);
  }

  @Override
public SubtypeSampleMeta getSubtypeSampleMetaByKSubtypeSampleId (final String k, final int subtypeSampleId) {
    return this.getSubtypeSampleMetaDao().getSubtypeSampleMetaByKSubtypeSampleId(k, subtypeSampleId);
  }

  @Override
public void updateBySubtypeSampleId (final String area, final int subtypeSampleId, final List<SubtypeSampleMeta> metaList) {
    this.getSubtypeSampleMetaDao().updateBySubtypeSampleId(area, subtypeSampleId, metaList); 
  }

  @Override
public void updateBySubtypeSampleId (final int subtypeSampleId, final List<SubtypeSampleMeta> metaList) {
    this.getSubtypeSampleMetaDao().updateBySubtypeSampleId(subtypeSampleId, metaList); 
  }

  @Override
public  List<SubtypeSampleMeta> getSubtypeSamplesMetaBySubtypeSampleId (final int subtypeSampleId) {
    return getSubtypeSampleMetaDao().getSubtypeSamplesMetaBySubtypeSampleId(subtypeSampleId);
  }


}

