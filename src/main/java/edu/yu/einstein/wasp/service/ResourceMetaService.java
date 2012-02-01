
/**
 *
 * ResourceMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.model.ResourceMeta;

@Service
public interface ResourceMetaService extends WaspService<ResourceMeta> {

	/**
	 * setResourceMetaDao(ResourceMetaDao resourceMetaDao)
	 *
	 * @param resourceMetaDao
	 *
	 */
	public void setResourceMetaDao(ResourceMetaDao resourceMetaDao);

	/**
	 * getResourceMetaDao();
	 *
	 * @return resourceMetaDao
	 *
	 */
	public ResourceMetaDao getResourceMetaDao();

  public ResourceMeta getResourceMetaByResourceMetaId (final Integer resourceMetaId);

  public ResourceMeta getResourceMetaByKResourceId (final String k, final Integer resourceId);


  public void updateByResourceId (final String area, final int resourceId, final List<ResourceMeta> metaList);

  public void updateByResourceId (final int resourceId, final List<ResourceMeta> metaList);


}

