
/**
 *
 * UserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the User object
 *
 *
 **/
 
package edu.yu.einstein.wasp.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UiField;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UiFieldDaoImpl extends WaspDaoImpl<UiField> implements edu.yu.einstein.wasp.dao.UiFieldDao {

  public UiFieldDaoImpl() {
    super();
    this.entityClass = UiField.class;
  }


}

