
/**
 *
 * TypeSampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeSampleDao;
import edu.yu.einstein.wasp.model.TypeSample;

@Service
public interface TypeSampleService extends WaspService<TypeSample> {

	/**
	 * setTypeSampleDao(TypeSampleDao typeSampleDao)
	 *
	 * @param typeSampleDao
	 *
	 */
	public void setTypeSampleDao(TypeSampleDao typeSampleDao);

	/**
	 * getTypeSampleDao();
	 *
	 * @return typeSampleDao
	 *
	 */
	public TypeSampleDao getTypeSampleDao();

  public TypeSample getTypeSampleByTypeSampleId (final int typeSampleId);

  public TypeSample getTypeSampleByIName (final String iName);

  public TypeSample getTypeSampleByName (final String name);


}

