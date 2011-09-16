
/**
 *
 * UserService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.model.UiField;

@Service
public interface UiFieldService extends WaspService<UiField> {

	List<String> getUniqueAreas();
	boolean exists(String locale, String area, String name, String attrName);
}

