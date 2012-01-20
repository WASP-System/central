
/**
 *
 * TypeSampleresourcecategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleresourcecategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.TypeSampleresourcecategoryService;
import edu.yu.einstein.wasp.dao.TypeSampleresourcecategoryDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.TypeSampleresourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TypeSampleresourcecategoryServiceImpl extends WaspServiceImpl<TypeSampleresourcecategory> implements TypeSampleresourcecategoryService {

	/**
	 * typeSampleresourcecategoryDao;
	 *
	 */
	private TypeSampleresourcecategoryDao typeSampleresourcecategoryDao;

	/**
	 * setTypeSampleresourcecategoryDao(TypeSampleresourcecategoryDao typeSampleresourcecategoryDao)
	 *
	 * @param typeSampleresourcecategoryDao
	 *
	 */
	@Autowired
	public void setTypeSampleresourcecategoryDao(TypeSampleresourcecategoryDao typeSampleresourcecategoryDao) {
		this.typeSampleresourcecategoryDao = typeSampleresourcecategoryDao;
		this.setWaspDao(typeSampleresourcecategoryDao);
	}

	/**
	 * getTypeSampleresourcecategoryDao();
	 *
	 * @return typeSampleresourcecategoryDao
	 *
	 */
	public TypeSampleresourcecategoryDao getTypeSampleresourcecategoryDao() {
		return this.typeSampleresourcecategoryDao;
	}


  public TypeSampleresourcecategory getTypeSampleresourcecategoryByTypeSampleresourcecategoryId (final Integer typeSampleresourcecategoryId) {
    return this.getTypeSampleresourcecategoryDao().getTypeSampleresourcecategoryByTypeSampleresourcecategoryId(typeSampleresourcecategoryId);
  }

  public TypeSampleresourcecategory getTypeSampleresourcecategoryByIName (final String iName) {
    return this.getTypeSampleresourcecategoryDao().getTypeSampleresourcecategoryByIName(iName);
  }

  public TypeSampleresourcecategory getTypeSampleresourcecategoryByName (final String name) {
    return this.getTypeSampleresourcecategoryDao().getTypeSampleresourcecategoryByName(name);
  }

}

