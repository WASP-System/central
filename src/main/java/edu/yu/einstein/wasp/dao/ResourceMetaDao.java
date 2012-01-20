
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceMetaDao extends WaspDao<ResourceMeta> {

  public ResourceMeta getResourceMetaByResourceMetaId (final Integer resourceMetaId);

  public ResourceMeta getResourceMetaByKResourceId (final String k, final Integer resourceId);


  public void updateByResourceId (final String area, final int resourceId, final List<ResourceMeta> metaList);

  public void updateByResourceId (final int resourceId, final List<ResourceMeta> metaList);




}

