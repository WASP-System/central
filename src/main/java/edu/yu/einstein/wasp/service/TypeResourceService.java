
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

import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.model.TypeResource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

  public TypeResource getTypeResourceByTypeResourceId (final Integer typeResourceId);

  public TypeResource getTypeResourceByIName (final String iName);

  public TypeResource getTypeResourceByName (final String name);


}

