
/**
 *
 * ResourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.model.Resource;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ResourceService extends WaspService<Resource> {

  public void setResourceDao(ResourceDao resourceDao);
  public ResourceDao getResourceDao();

  public Resource getResourceByResourceId (final int resourceId);

  public Resource getResourceByName (final String name);

}

