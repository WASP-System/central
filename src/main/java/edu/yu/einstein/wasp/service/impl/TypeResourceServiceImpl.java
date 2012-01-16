
/**
 *
 * TypeResourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.service.TypeResourceService;

@Service
public class TypeResourceServiceImpl extends WaspServiceImpl<TypeResource> implements TypeResourceService {

	/**
	 * typeResourceDao;
	 *
	 */
	private TypeResourceDao typeResourceDao;

	/**
	 * setTypeResourceDao(TypeResourceDao typeResourceDao)
	 *
	 * @param typeResourceDao
	 *
	 */
	@Autowired
	public void setTypeResourceDao(TypeResourceDao typeResourceDao) {
		this.typeResourceDao = typeResourceDao;
		this.setWaspDao(typeResourceDao);
	}

	/**
	 * getTypeResourceDao();
	 *
	 * @return typeResourceDao
	 *
	 */
	public TypeResourceDao getTypeResourceDao() {
		return this.typeResourceDao;
	}


  public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId) {
    return this.getTypeResourceDao().getTypeResourceByTypeResourceId(typeResourceId);
  }

  public TypeResource getTypeResourceByIName (final String iName) {
    return this.getTypeResourceDao().getTypeResourceByIName(iName);
  }

  public TypeResource getTypeResourceByName (final String name) {
    return this.getTypeResourceDao().getTypeResourceByName(name);
  }

}

