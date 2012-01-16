
/**
 *
 * SubtypeSampleMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SubtypeSampleMetaDao;
import edu.yu.einstein.wasp.model.SubtypeSampleMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SubtypeSampleMetaService extends WaspMetaService<SubtypeSampleMeta> {

	/**
	 * setSubtypeSampleMetaDao(SubtypeSampleMetaDao subtypeSampleMetaDao)
	 *
	 * @param subtypeSampleMetaDao
	 *
	 */
	public void setSubtypeSampleMetaDao(SubtypeSampleMetaDao subtypeSampleMetaDao);

	/**
	 * getSubtypeSampleMetaDao();
	 *
	 * @return subtypeSampleMetaDao
	 *
	 */
	public SubtypeSampleMetaDao getSubtypeSampleMetaDao();

  public SubtypeSampleMeta getSubtypeSampleMetaBySubtypeSampleMetaId (final int subtypeSampleMetaId);

  public SubtypeSampleMeta getSubtypeSampleMetaByKSubtypeSampleId (final String k, final int subtypeSampleId);


  public void updateBySubtypeSampleId (final String area, final int subtypeSampleId, final List<SubtypeSampleMeta> metaList);

  public void updateBySubtypeSampleId (final int subtypeSampleId, final List<SubtypeSampleMeta> metaList);


  List<SubtypeSampleMeta> getSubtypeSamplesMetaBySubtypeSampleId (final int subtypeSampleId);


}

