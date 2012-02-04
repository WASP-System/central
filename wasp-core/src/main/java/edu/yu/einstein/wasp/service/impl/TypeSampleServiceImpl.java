
/**
 *
 * TypeSampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeSampleDao;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.service.TypeSampleService;

@Service
public class TypeSampleServiceImpl extends WaspServiceImpl<TypeSample> implements TypeSampleService {

	/**
	 * typeSampleDao;
	 *
	 */
	private TypeSampleDao typeSampleDao;

	/**
	 * setTypeSampleDao(TypeSampleDao typeSampleDao)
	 *
	 * @param typeSampleDao
	 *
	 */
	@Override
	@Autowired
	public void setTypeSampleDao(TypeSampleDao typeSampleDao) {
		this.typeSampleDao = typeSampleDao;
		this.setWaspDao(typeSampleDao);
	}

	/**
	 * getTypeSampleDao();
	 *
	 * @return typeSampleDao
	 *
	 */
	@Override
	public TypeSampleDao getTypeSampleDao() {
		return this.typeSampleDao;
	}


  @Override
public TypeSample getTypeSampleByTypeSampleId (final int typeSampleId) {
    return this.getTypeSampleDao().getTypeSampleByTypeSampleId(typeSampleId);
  }

  @Override
public TypeSample getTypeSampleByIName (final String iName) {
    return this.getTypeSampleDao().getTypeSampleByIName(iName);
  }

  @Override
public TypeSample getTypeSampleByName (final String name) {
    return this.getTypeSampleDao().getTypeSampleByName(name);
  }

}

