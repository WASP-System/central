
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

import edu.yu.einstein.wasp.service.TypeSampleCategoryService;
import edu.yu.einstein.wasp.dao.TypeSampleCategoryDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.TypeSampleCategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public TypeSampleCategoryDao getTypeSampleCategoryDao() {
		return this.typeSampleCategoryDao;
	}


  public TypeSampleCategory getTypeSampleCategoryByTypeSamplecategoryId (final Integer typeSamplecategoryId) {
    return this.getTypeSampleCategoryDao().getTypeSampleCategoryByTypeSamplecategoryId(typeSamplecategoryId);
  }

  public TypeSampleCategory getTypeSampleCategoryByIName (final String iName) {
    return this.getTypeSampleCategoryDao().getTypeSampleCategoryByIName(iName);
  }

  public TypeSampleCategory getTypeSampleCategoryByName (final String name) {
    return this.getTypeSampleCategoryDao().getTypeSampleCategoryByName(name);
  }

}

