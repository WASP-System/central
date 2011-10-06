/**
 * 
 */
package edu.yu.einstein.wasp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;

/**
 * Methods for handling json responses for JQuery auto-complete on input boxes
 * @author andymac
 *
 */

@Controller
@Transactional
@RequestMapping("/autocomplete")
public class AutoCompleteController extends WaspController{
	  
	@Autowired
	private UserService userService;
	
	/**
	   * Obtains a json message containing list of all current users where each entry in the list looks something like "Peter Piper (PPiper)"
	   * Used to populate a JQuery autocomplete managed input box
	   * @param adminNameFragment
	   * @return json message
	   */
	  @RequestMapping(value="/getUserNamesAndLoginForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getNames(@RequestParam String adminNameFragment) {
	         Map activeUserQueryMap = new HashMap();
	         activeUserQueryMap.put("isActive", 1);
	         List<User> userList = userService.findByMap(activeUserQueryMap);
	         int counter = 0;
	         String jsonString = new String();
	         jsonString = jsonString + "{\"source\": [";
	         for (User u : userList){
	        	 if(u.getFirstName().indexOf(adminNameFragment) > -1 || u.getLastName().indexOf(adminNameFragment) > -1 || u.getLogin().indexOf(adminNameFragment) > -1){
	        		 if(counter > 0){
	        			 jsonString = jsonString + ",";
	        		 }
	        	 	jsonString = jsonString + "\""+ u.getFirstName() + " " + u.getLastName() + " (" + u.getLogin() + ")\"";
	        	 	counter++;
	        	 }
	         }
	         jsonString = jsonString + "]}";
	         return jsonString;                
	  }
}
