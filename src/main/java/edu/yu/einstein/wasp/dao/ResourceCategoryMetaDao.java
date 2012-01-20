
/**
 *
 * ResourceCategoryMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategoryMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceCategoryMetaDao extends WaspDao<ResourceCategoryMeta> {

  public ResourceCategoryMeta getResourceCategoryMetaByResourceCategoryMetaId (final Integer resourceCategoryMetaId);

  public ResourceCategoryMeta getResourceCategoryMetaByKResourcecategoryId (final String k, final Integer resourcecategoryId);


  public void updateByResourcecategoryId (final String area, final int resourcecategoryId, final List<ResourceCategoryMeta> metaList);

  public void updateByResourcecategoryId (final int resourcecategoryId, final List<ResourceCategoryMeta> metaList);




}

