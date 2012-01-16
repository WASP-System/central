
/**
 *
 * TypeResourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResourceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.model.TypeResource;

@Service
public interface TypeResourceService extends WaspService<TypeResource> {

	/**
	 * setTypeResourceDao(TypeResourceDao typeResourceDao)
	 *
	 * @param typeResourceDao
	 *
	 */
	public void setTypeResourceDao(TypeResourceDao typeResourceDao);

	/**
	 * getTypeResourceDao();
	 *
	 * @return typeResourceDao
	 *
	 */
	public TypeResourceDao getTypeResourceDao();

  public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId);

  public TypeResource getTypeResourceByIName (final String iName);

  public TypeResource getTypeResourceByName (final String name);


}

