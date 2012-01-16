
/**
 *
 * ResourceMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceMeta;


public interface ResourceMetaDao extends WaspDao<ResourceMeta> {

  public ResourceMeta getResourceMetaByResourceMetaId (final int resourceMetaId);

  public ResourceMeta getResourceMetaByKResourceId (final String k, final int resourceId);


  public void updateByResourceId (final String area, final int resourceId, final List<ResourceMeta> metaList);

  public void updateByResourceId (final int resourceId, final List<ResourceMeta> metaList);




}

