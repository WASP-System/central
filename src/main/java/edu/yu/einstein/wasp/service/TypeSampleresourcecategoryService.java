
/**
 *
 * TypeSampleresourcecategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleresourcecategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.TypeSampleresourcecategoryDao;
import edu.yu.einstein.wasp.model.TypeSampleresourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface TypeSampleresourcecategoryService extends WaspService<TypeSampleresourcecategory> {

	/**
	 * setTypeSampleresourcecategoryDao(TypeSampleresourcecategoryDao typeSampleresourcecategoryDao)
	 *
	 * @param typeSampleresourcecategoryDao
	 *
	 */
	public void setTypeSampleresourcecategoryDao(TypeSampleresourcecategoryDao typeSampleresourcecategoryDao);

	/**
	 * getTypeSampleresourcecategoryDao();
	 *
	 * @return typeSampleresourcecategoryDao
	 *
	 */
	public TypeSampleresourcecategoryDao getTypeSampleresourcecategoryDao();

  public TypeSampleresourcecategory getTypeSampleresourcecategoryByTypeSampleresourcecategoryId (final Integer typeSampleresourcecategoryId);

  public TypeSampleresourcecategory getTypeSampleresourcecategoryByIName (final String iName);

  public TypeSampleresourcecategory getTypeSampleresourcecategoryByName (final String name);


}

