/**
 * 
 */
package edu.yu.einstein.wasp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserMetaService;
import edu.yu.einstein.wasp.service.UserPendingMetaService;
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
	
	@Autowired
	private UserMetaService userMetaService;
	
	@Autowired
	private UserPendingMetaService userPendingMetaService;
	
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
	         String jsonString = new String();
	         jsonString = jsonString + "{\"source\": [";
	         for (User u : userList){
	        	 if(u.getFirstName().indexOf(adminNameFragment) > -1 || u.getLastName().indexOf(adminNameFragment) > -1 || u.getLogin().indexOf(adminNameFragment) > -1){
	        		 jsonString = jsonString + "\""+ u.getFirstName() + " " + u.getLastName() + " (" + u.getLogin() + ")\",";
	        	 }
	         }
	         jsonString = jsonString.replaceAll(",$", "") + "]}";
	         return jsonString;                
	  }
	  
	  /**
	   * Obtains a json message containing a list of all recorded institutes from the current users and pending PI lists. 
	   * Used to populate a JQuery autocomplete managed input box
	   * @param instituteNameFragment
	   * @return
	   */
	  @RequestMapping(value="/getInstitutesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getInstitutes(@RequestParam String instituteNameFragment) {
		  	Map institutionQueryMap = new HashMap();
		  	institutionQueryMap.put("k", "user.institution");
		  	List<MetaBase> list = (List<MetaBase>) userMetaService.findByMap(institutionQueryMap);
		  	institutionQueryMap.put("k", "piPending.institution");
		  	list.addAll((List<MetaBase>) userPendingMetaService.findByMap(institutionQueryMap));
	        
	        String jsonString = new String();
	        jsonString = jsonString + "{\"source\": [";
	        SortedSet<String> uniqueInstitutes = new TreeSet();
	        for (MetaBase meta : list){
	        	if (meta.getV() != null && !meta.getV().isEmpty())
	        		uniqueInstitutes.add(meta.getV());
	        }
	        for (String institute: uniqueInstitutes){
	        	jsonString = jsonString + "\""+ institute + "\",";
	        }
	        jsonString = jsonString.replaceAll(",$", "") + "]}";
	        return jsonString;                
	  }
}
