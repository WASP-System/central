
/**
 *
 * SubtypeSampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SubtypeSampleDao;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.service.SubtypeSampleService;

@Service
public class SubtypeSampleServiceImpl extends WaspServiceImpl<SubtypeSample> implements SubtypeSampleService {

	/**
	 * subtypeSampleDao;
	 *
	 */
	private SubtypeSampleDao subtypeSampleDao;

	/**
	 * setSubtypeSampleDao(SubtypeSampleDao subtypeSampleDao)
	 *
	 * @param subtypeSampleDao
	 *
	 */
	@Override
	@Autowired
	public void setSubtypeSampleDao(SubtypeSampleDao subtypeSampleDao) {
		this.subtypeSampleDao = subtypeSampleDao;
		this.setWaspDao(subtypeSampleDao);
	}

	/**
	 * getSubtypeSampleDao();
	 *
	 * @return subtypeSampleDao
	 *
	 */
	@Override
	public SubtypeSampleDao getSubtypeSampleDao() {
		return this.subtypeSampleDao;
	}


  @Override
public SubtypeSample getSubtypeSampleBySubtypeSampleId (final int subtypeSampleId) {
    return this.getSubtypeSampleDao().getSubtypeSampleBySubtypeSampleId(subtypeSampleId);
  }

  @Override
public SubtypeSample getSubtypeSampleByIName (final String iName) {
    return this.getSubtypeSampleDao().getSubtypeSampleByIName(iName);
  }
  
  @Override
  public List<SubtypeSample> getActiveSubtypeSamples(){
	  Map queryMap = new HashMap();
	  queryMap.put("isActive", 1);
	  return this.findByMap(queryMap);
  }

}

