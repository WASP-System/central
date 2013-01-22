
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


public interface ResourceMetaDao extends WaspMetaDao<ResourceMeta> {

  public ResourceMeta getResourceMetaByResourceMetaId (final Integer resourceMetaId);

  public ResourceMeta getResourceMetaByKResourceId (final String k, final Integer resourceId);





}

