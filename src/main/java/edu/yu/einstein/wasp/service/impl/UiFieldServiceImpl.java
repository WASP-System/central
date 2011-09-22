
/**
 *
 * UserroleService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserroleService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UiFieldDao;
import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.UiFieldService;

@Service
public class UiFieldServiceImpl extends WaspServiceImpl<UiField> implements UiFieldService {

	  private UiFieldDao dao;
	  @Autowired
	  public void setDao(UiFieldDao dao) {
	    this.dao = dao;
	    this.setWaspDao(dao);
	  }
	  
	  public List<String> getUniqueAreas() {
		  return this.dao.getUniqueAreas();
	  }
	  
	  public boolean exists(String locale, String area, String name, String attrName) {
		  return this.dao.exists(locale, area, name, attrName);
	  }
	
	  public String dumpUiFieldTable() {
		  return this.dao.dumpUiFieldTable();
	  }
	  
}

