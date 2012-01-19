
/**
 *
 * TypeSamplecategoryresourcecategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategoryResourceCategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.TypeSampleCategoryResourceCategoryService;
import edu.yu.einstein.wasp.dao.TypeSampleCategoryResourceCategoryDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.TypeSampleCategoryResourceCategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TypeSamplecategoryresourcecategoryServiceImpl extends WaspServiceImpl<TypeSampleCategoryResourceCategory> implements TypeSampleCategoryResourceCategoryService {

	/**
	 * typeSampleCategoryResourceCategoryDao;
	 *
	 */
	private TypeSampleCategoryResourceCategoryDao typeSampleCategoryResourceCategoryDao;

	/**
	 * setTypeSamplecategoryresourcecategoryDao(TypeSampleCategoryResourceCategoryDao typeSampleCategoryResourceCategoryDao)
	 *
	 * @param typeSampleCategoryResourceCategoryDao
	 *
	 */
	@Autowired
	public void setTypeSampleCategoryResourceCategoryDao(TypeSampleCategoryResourceCategoryDao typeSampleCategoryResourceCategoryDao) {
		this.typeSampleCategoryResourceCategoryDao = typeSampleCategoryResourceCategoryDao;
		this.setWaspDao(typeSampleCategoryResourceCategoryDao);
	}

	/**
	 * getTypeSamplecategoryresourcecategoryDao();
	 *
	 * @return typeSampleCategoryResourceCategoryDao
	 *
	 */
	public TypeSampleCategoryResourceCategoryDao getTypeSampleCategoryResourceCategoryDao() {
		return this.typeSampleCategoryResourceCategoryDao;
	}


  public TypeSampleCategoryResourceCategory getTypeSampleCategoryResourceCategoryByTypeSamplecategoryresourcecategoryId (final Integer typeSamplecategoryresourcecategoryId) {
    return this.getTypeSampleCategoryResourceCategoryDao().getTypeSampleCategoryResourceCategoryByTypeSamplecategoryresourcecategoryId(typeSamplecategoryresourcecategoryId);
  }

}

