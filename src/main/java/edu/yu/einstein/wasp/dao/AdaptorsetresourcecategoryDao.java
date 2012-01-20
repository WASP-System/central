
/**
 *
 * AdaptorsetresourcecategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorsetresourcecategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AdaptorsetresourcecategoryDao extends WaspDao<Adaptorsetresourcecategory> {

  public Adaptorsetresourcecategory getAdaptorsetresourcecategoryByAdaptorsetresourcecategoryId (final Integer adaptorsetresourcecategoryId);

  public Adaptorsetresourcecategory getAdaptorsetresourcecategoryByAdaptorsetIdResourcecategoryId (final Integer adaptorsetId, final Integer resourcecategoryId);


}

