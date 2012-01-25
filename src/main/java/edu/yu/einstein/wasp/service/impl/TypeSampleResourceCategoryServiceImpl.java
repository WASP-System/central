
/**
 *
 * TypeSampleResourceCategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleResourceCategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeSampleResourceCategoryDao;
import edu.yu.einstein.wasp.model.TypeSampleResourceCategory;
import edu.yu.einstein.wasp.service.TypeSampleResourceCategoryService;

@Service
public class TypeSampleResourceCategoryServiceImpl extends WaspServiceImpl<TypeSampleResourceCategory> implements TypeSampleResourceCategoryService {

	/**
	 * typeSampleResourceCategoryDao;
	 *
	 */
	private TypeSampleResourceCategoryDao typeSampleResourceCategoryDao;

	/**
	 * setTypeSampleresourcecategoryDao(TypeSampleResourceCategoryDao typeSampleResourceCategoryDao)
	 *
	 * @param typeSampleResourceCategoryDao
	 *
	 */
	@Override
	@Autowired
	public void setTypeSampleResourceCategoryDao(TypeSampleResourceCategoryDao typeSampleResourceCategoryDao) {
		this.typeSampleResourceCategoryDao = typeSampleResourceCategoryDao;
		this.setWaspDao(typeSampleResourceCategoryDao);
	}

	/**
	 * getTypeSampleresourcecategoryDao();
	 *
	 * @return typeSampleResourceCategoryDao
	 *
	 */
	@Override
	public TypeSampleResourceCategoryDao getTypeSampleResourceCategoryDao() {
		return this.typeSampleResourceCategoryDao;
	}


  @Override
  public TypeSampleResourceCategory getTypeSampleResourceCategoryByTypeSampleResourceCategoryId (final Integer typeSampleResourceCategoryId) {
    return this.getTypeSampleResourceCategoryDao().getTypeSampleResourceCategoryByTypeSampleResourceCategoryId(typeSampleResourceCategoryId);
  }
  
  @Override
  public TypeSampleResourceCategory getTypeSampleResourceCategoryByIName (final String iName){
	  return this.getTypeSampleResourceCategoryDao().getTypeSampleResourceCategoryByIName(iName);
  }
  
  @Override
  public TypeSampleResourceCategory getTypeTypeSampleResourceCategoryByName (final String name){
	  return this.getTypeSampleResourceCategoryDao().getTypeSampleResourceCategoryByName(name);
  }
}

