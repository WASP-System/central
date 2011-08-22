
/**
 *
 * TypeResourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResourceService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.TypeResource;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TypeResourceServiceImpl extends WaspServiceImpl<TypeResource> implements TypeResourceService {

  private TypeResourceDao typeResourceDao;
  @Autowired
  public void setTypeResourceDao(TypeResourceDao typeResourceDao) {
    this.typeResourceDao = typeResourceDao;
    this.setWaspDao(typeResourceDao);
  }
  public TypeResourceDao getTypeResourceDao() {
    return this.typeResourceDao;
  }

  // **

  
  public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId) {
    return this.getTypeResourceDao().getTypeResourceByTypeResourceId(typeResourceId);
  }

  public TypeResource getTypeResourceByIName (final String iName) {
    return this.getTypeResourceDao().getTypeResourceByIName(iName);
  }

  public TypeResource getTypeResourceByName (final String name) {
    return this.getTypeResourceDao().getTypeResourceByName(name);
  }
}

