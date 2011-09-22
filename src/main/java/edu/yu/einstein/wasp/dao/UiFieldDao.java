
/**
 *
 * UserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.UiField;


public interface UiFieldDao extends WaspDao<UiField> {
	List<String> getUniqueAreas();
	boolean exists(String locale, String area, String name, String attrName);
	
	String dumpUiFieldTable();
}

