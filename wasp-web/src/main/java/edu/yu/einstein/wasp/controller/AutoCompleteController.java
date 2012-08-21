/**
 * 
 */
package edu.yu.einstein.wasp.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.dao.UserPendingMetaDao;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.JobDao;

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
	private UserMetaDao userMetaDao;
	
	
	@Autowired
	private UserPendingMetaDao userPendingMetaDao;

	@Autowired
	private LabDao labDao;
	
	@Autowired
	private JobDao jobDao;

	/**
	   * NOT USED - but shows a way to have the json message contain list of all PIs where each entry in the list looks something like "Peter Piper" but once selected, it is "Peter Piper (PPiper)" that is actually put into the autocomplete input box"
	   * Used to populate a JQuery autocomplete managed input box
	   * @param piNameFragment
	   * @return json message
	   */
	  @RequestMapping(value="/getPiForAutocomplete", method=RequestMethod.GET)
	  public @ResponseBody String getPiForAutocomplete() {
	      
		  List<Lab> labList = labDao.findAll(); 
	      List<User> userList = new ArrayList<User>();
	      for(Lab lab : labList){
	    	  userList.add(lab.getUser());
	      }
		 
	      StringBuilder sb = new StringBuilder();
	      sb.append("{\"source\": [");
	      int counter = 0;
	      for (User u : userList){
	    	  if(counter++ > 0){
	    		  sb.append(",");
	    	  }
	    	  sb.append("{\"label\": \""+u.getFirstName()+" "+u.getLastName()+"\", \"value\":\""+u.getFirstName()+" "+u.getLastName()+" ("+u.getLogin()+")\"}");
	      }
	      sb.append("]}");
	      
	      String jsonOutput = new String(sb);
	      //System.out.println("jsonOutput: " + jsonOutput);
	      
	      return jsonOutput; 
	  }
	  
	/**
	   * Obtains a json message containing list of all PIs where each entry in the list looks something like "Peter Piper (PPiper)"
	   * OrderBy lastname, then firstname ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param piNameFragment
	   * @return json message
	   */
	  @RequestMapping(value="/getPiNamesAndLoginForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getPINames(@RequestParam String piNameFragment) {
	      
		  List<Lab> labList = labDao.findAll(); 
	      List<User> userList = new ArrayList<User>();
	      for(Lab lab : labList){
	    	  userList.add(lab.getUser());//PI of lab
	      }
	      class LastNameFirstNameComparator implements Comparator<User> {
	    	@Override
	    	public int compare(User arg0, User arg1) {
	    		return arg0.getLastName().concat(arg0.getFirstName()).compareToIgnoreCase(arg1.getLastName().concat(arg1.getFirstName()));
	    	}
	      }
	      Collections.sort(userList, new LastNameFirstNameComparator());
	      
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (User u : userList){
	    	  if(u.getFirstName().indexOf(piNameFragment) > -1 || u.getLastName().indexOf(piNameFragment) > -1 || u.getLogin().indexOf(piNameFragment) > -1){
	    		  jsonString = jsonString + "\""+ u.getFirstName() + " " + u.getLastName() + " (" + u.getLogin() + ")\",";
	    	  }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
	/**
	   * Obtains a json message containing list of all current users where each entry in the list looks something like "Peter Piper (PPiper)"
	   * Used to populate a JQuery autocomplete managed input box
	   * @param adminNameFragment
	   * @return json message
	   */
	  @RequestMapping(value="/getUserNamesAndLoginForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getNames(@RequestParam String adminNameFragment) {
	         List<User> userList = userDao.getActiveUsers();
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
		  	
		  	List<MetaBase> list = userMetaDao.findDistinctMetaOrderBy("user.institution" ,"ASC");
		  	list.addAll(userPendingMetaDao.findDistinctMetaOrderBy("piPending.institution","ASC") );
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
	  
	  /**
	   * Obtains a json message containing a list of all job names from the job list. 
	   * Used to populate a JQuery autocomplete managed input box
	   * @param jobName
	   * @return
	   */
	  @RequestMapping(value="/getJobNamesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getJobNames(@RequestParam String jobName) {
		  	
		  	 List<Job> jobList = jobDao.findAll();
	         String jsonString = new String();
	         jsonString = jsonString + "{\"source\": [";
	         for (Job job : jobList){
	        	 if(job.getName().indexOf(jobName) > -1){
	        		 jsonString = jsonString + "\""+ job.getName() + "\",";
	        	 }
	         }
	         jsonString = jsonString.replaceAll(",$", "") + "]}";
	         return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing list of ALL users where each entry in the list looks something like "Peter Piper (PPiper)"
	   * Order by lastname then firstname, ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param adminNameFragment
	   * @return json message
	   */
	  @RequestMapping(value="/getAllUserNamesAndLoginForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllUserNames(@RequestParam String adminNameFragment) {
		  
		  List<String> orderbyList = new ArrayList<String>();
		  orderbyList.add("lastName");
		  orderbyList.add("firstName");
	      List<User> userList = userDao.findByMapDistinctOrderBy(new HashMap(), null, orderbyList, "asc");
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
}
