
/**
 *
 * TypeResourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResourceService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.model.TypeResource;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TypeResourceService extends WaspService<TypeResource> {

  public void setTypeResourceDao(TypeResourceDao typeResourceDao);
  public TypeResourceDao getTypeResourceDao();

  public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId);

  public TypeResource getTypeResourceByIName (final String iName);

  public TypeResource getTypeResourceByName (final String name);

}

