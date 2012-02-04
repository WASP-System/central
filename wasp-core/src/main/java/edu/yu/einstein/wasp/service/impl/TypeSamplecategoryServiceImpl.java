
/**
 *
 * TypeSamplecategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeSampleCategoryDao;
import edu.yu.einstein.wasp.model.TypeSampleCategory;
import edu.yu.einstein.wasp.service.TypeSampleCategoryService;

@Service
public class TypeSamplecategoryServiceImpl extends WaspServiceImpl<TypeSampleCategory> implements TypeSampleCategoryService {

	/**
	 * typeSampleCategoryDao;
	 *
	 */
	private TypeSampleCategoryDao typeSampleCategoryDao;

	/**
	 * setTypeSamplecategoryDao(TypeSampleCategoryDao typeSampleCategoryDao)
	 *
	 * @param typeSampleCategoryDao
	 *
	 */
	@Override
	@Autowired
	public void setTypeSampleCategoryDao(TypeSampleCategoryDao typeSampleCategoryDao) {
		this.typeSampleCategoryDao = typeSampleCategoryDao;
		this.setWaspDao(typeSampleCategoryDao);
	}

	/**
	 * getTypeSamplecategoryDao();
	 *
	 * @return typeSampleCategoryDao
	 *
	 */
	@Override
	public TypeSampleCategoryDao getTypeSampleCategoryDao() {
		return this.typeSampleCategoryDao;
	}


  @Override
public TypeSampleCategory getTypeSampleCategoryByTypeSampleCategoryId (final Integer typeSamplecategoryId) {
    return this.getTypeSampleCategoryDao().getTypeSampleCategoryByTypeSamplecategoryId(typeSamplecategoryId);
  }

  @Override
public TypeSampleCategory getTypeSampleCategoryByIName (final String iName) {
    return this.getTypeSampleCategoryDao().getTypeSampleCategoryByIName(iName);
  }

  @Override
public TypeSampleCategory getTypeSampleCategoryByName (final String name) {
    return this.getTypeSampleCategoryDao().getTypeSampleCategoryByName(name);
  }


}

