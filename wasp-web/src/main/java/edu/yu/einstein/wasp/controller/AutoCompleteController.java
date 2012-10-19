/**
 * 
 */
package edu.yu.einstein.wasp.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.LinkedHashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.dao.UserPendingMetaDao;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleBarcode;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.AuthenticationService;

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

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private ResourceCategoryDao resourceCategoryDao;

	@Autowired
	private ResourceDao resourceDao;

	@Autowired
	private RunDao runDao;

	@Autowired
	private SampleDao sampleDao;
	
	@Autowired
	private SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	private FilterService filterService;
	
	@Autowired
	private AuthenticationService authenticationService;

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
	   * 9/4/12 added filter so that if the viewer is just a DA, then restrict PIs to those covered by the DA's department(s)
	   * OrderBy lastname, then firstname ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param piNameFragment
	   * @return json message
	   */
	  @RequestMapping(value="/getPiNamesAndLoginForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getPINames(@RequestParam String piNameFragment) {
	      
		  List<Lab> labList = labDao.findAll();
		  //perform next if block ONLY if the viewer is A DA but is NOT any other type of facility member; this will restrict the PI's that are 
		  //visible and available via this autocomplete widget to those labs that are in departments that this DA controls  
		  if(authenticationService.isOnlyDepartmentAdministrator()){//if viewer is just a DA, then retain only labs in the DA's department(s)
			  List<Lab> labsToKeep = filterService.filterLabListForDA(labList);
			  labList.retainAll(labsToKeep);
		  }
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
	   * 9/4/12 added filter so that if the viewer is just a DA, then restrict jobNames to those in jobs covered by the DA's department(s)
	   * Used to populate a JQuery autocomplete managed input box
	   * @param jobName
	   * @return
	   */
	  @RequestMapping(value="/getJobNamesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getJobNames(@RequestParam String jobName) {
		  	
		  	 List<Job> jobList = jobDao.findAll();
		  	 //perform next if block ONLY if the viewer is A DA but is NOT any other type of facility member; this will restrict the job names that are 
		  	 //visible and available via this autocomplete widget to those jobs that are in departments that this DA controls  
			 if(authenticationService.isOnlyDepartmentAdministrator()){//if viewer is just a DA, then retain only jobs in the DA's department(s)
				 List<Job> jobsToKeep = filterService.filterJobListForDA(jobList);
				 jobList.retainAll(jobsToKeep);
			 }
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
	   * 9/4/12 added filter so that if the viewer is just a DA, then restrict UserNames and Logins to those covered by the DA's department(s)
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
	      
		  //perform next if block ONLY if the viewer is A DA but is NOT any other type of facility member; this will restrict the Users that are 
		  //visible and available via this autocomplete widget to those that are in departments that this DA controls  
		  if(authenticationService.isOnlyDepartmentAdministrator()){//if viewer is just a DA, then retain only users in the DA's department(s)
			  List<User> usersToKeep = filterService.filterUserListForDA(userList);
			  userList.retainAll(usersToKeep);
		  }
	      
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
	   * Obtains a json message containing list of ALL users login"
	   * Order ascending
	   * 9/4/12 added filter so that if the viewer is just a DA, then restrict Users to those covered by the DA's department(s)
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getAllUserLoginsForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllUserLogins(@RequestParam String str) {
		  
		  List<User> userList = userDao.findAllOrderBy("login", "asc");
		  //perform next if block ONLY if the viewer is A DA but is NOT any other type of facility member; this will restrict the Users logins that are 
		  //visible and available via this autocomplete widget to those that are in departments that this DA controls  
		  if(authenticationService.isOnlyDepartmentAdministrator()){//if viewer is just a DA, then retain only users in the DA's department(s)
			  List<User> usersToKeep = filterService.filterUserListForDA(userList);
			  userList.retainAll(usersToKeep);
		  }
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (User u : userList){
	      	 if(u.getLogin().indexOf(str) > -1){
	       		 jsonString = jsonString + "\""+ u.getLogin()+"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing DISTINCT list of ALL users first names"
	   * Order ascending
	   * 9/4/12 added filter so that if the viewer is just a DA, then restrict User first names to those covered by the DA's department(s)
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getDistinctUserFirstNamesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getDistinctUserFirstNames(@RequestParam String str) {
		  
		  List<User> userList = userDao.findAllOrderBy("firstName", "asc");
		  //perform next if block ONLY if the viewer is A DA but is NOT any other type of facility member; this will restrict the Users that are 
		  //visible and available via this autocomplete widget to those that are in departments that this DA controls  
		  if(authenticationService.isOnlyDepartmentAdministrator()){//if viewer is just a DA, then retain only users in the DA's department(s)
			  List<User> usersToKeep = filterService.filterUserListForDA(userList);
			  userList.retainAll(usersToKeep);
		  }
		  Set<String> distinctSetUserFirstName = new LinkedHashSet<String>();
		  for(User user : userList){
			  distinctSetUserFirstName.add(user.getFirstName());//use Set to collect Distinct list of names
		  }
		  
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (String firstName : distinctSetUserFirstName){
	      	 if(firstName.indexOf(str) > -1){
	       		 jsonString = jsonString + "\""+ firstName+"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing DISTINCT list of ALL users last names"
	   * Order ascending
	   * 9/4/12 added filter so that if the viewer is just a DA, then restrict user's last names to those covered by the DA's department(s)
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getDistinctUserLastNamesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getDistinctUserLastNames(@RequestParam String str) {
		  
		  List<User> userList = userDao.findAllOrderBy("lastName", "asc");
		  //perform next if block ONLY if the viewer is A DA but is NOT any other type of facility member; this will restrict the Users that are 
		  //visible and available via this autocomplete widget to those that are in departments that this DA controls  
		  if(authenticationService.isOnlyDepartmentAdministrator()){//if viewer is just a DA, then retain only users in the DA's department(s)
			  List<User> usersToKeep = filterService.filterUserListForDA(userList);
			  userList.retainAll(usersToKeep);
		  }
		  Set<String> distinctSetUserLastName = new LinkedHashSet<String>();
		  for(User user : userList){
			  distinctSetUserLastName.add(user.getLastName());//use Set to collect Distinct list of names
		  }
		  
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (String lastName : distinctSetUserLastName){
	      	 if(lastName.indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ lastName+"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing list of ALL users email addresses"
	   * Order ascending
	   * 9/4/12 added filter so that if the viewer is just a DA, then restrict user's email address to those covered by the DA's department(s)
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getAllUserEmailsForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllUserEmails(@RequestParam String str) {
		  
		  List<User> userList = userDao.findAllOrderBy("email", "asc");
		  //perform next if block ONLY if the viewer is A DA but is NOT any other type of facility member; this will restrict the Users email addresses that are 
		  //visible and available via this autocomplete widget to those that are in departments that this DA controls  
		  if(authenticationService.isOnlyDepartmentAdministrator()){//if viewer is just a DA, then retain only users in the DA's department(s)
			  List<User> usersToKeep = filterService.filterUserListForDA(userList);
			  userList.retainAll(usersToKeep);
		  }		  
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (User u : userList){
	      	 if(u.getEmail().indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ u.getEmail()+"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing list of ALL department names"
	   * 9/4/12 added filter so that if the viewer is just a DA, then restrict PIs to those covered by the DA's department(s)
	   * Order ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getDepartmentNamesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllDepartments(@RequestParam String str) {
		  
		  List<Department> departmentList = departmentDao.findAllOrderBy("name", "asc");
		  //perform next if block ONLY if the viewer is A DA but is NOT any other type of facility member; this will restrict the  
		  //departments that are visible and available via this autocomplete widget to those departments that this DA controls  
		  if(authenticationService.isOnlyDepartmentAdministrator()){//if viewer is just a DA, then retain departments that this DA controls
			  List<Department> departmentsToKeep = filterService.filterDepartmentListForDA(departmentList);
			  departmentList.retainAll(departmentsToKeep);
		  }
		  		  
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (Department d: departmentList){
	      	 if(d.getName().indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ d.getName()+"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing list of ALL platformUnit names"
	   * Order ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getPlatformUnitNamesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllPlatformUnitNames(@RequestParam String str) {
		  
		  Map queryMap = new HashMap();
		  queryMap.put("sampleType.iName", "platformunit");//restrict to platformUnit
		  List<String> orderByColumnNames = new ArrayList<String>();
		  orderByColumnNames.add("name");
		  String direction = "asc";
		  List<Sample> sampleList = sampleDao.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, direction);
			
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (Sample s: sampleList){
	      	 if(s.getName().indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ s.getName()+"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing list of ALL platformUnit barcodes"
	   * Order ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getPlatformUnitBarcodesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllPlatformUnitBarcodes(@RequestParam String str) {
		  
		  List<Sample> sampleList = sampleDao.findAllPlatformUnits();
		  List<String> platformUnitBarcodeList = new ArrayList<String>();
		  for(Sample s : sampleList){
			  List<SampleBarcode> sbList = s.getSampleBarcode();
			  if(sbList != null && sbList.size()>0){
				  platformUnitBarcodeList.add(sbList.get(0).getBarcode().getBarcode());
			  }			  
		  }
		  Collections.sort(platformUnitBarcodeList);
		  
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (String barcodeAsString : platformUnitBarcodeList){
	      	 if(barcodeAsString.indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ barcodeAsString +"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing list of ALL platformUnit samplesubtypes (such as Illumina Flow Cell Version 3)"
	   * Order ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getPlatformUnitSubtypesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllPlatformUnitSubtypes(@RequestParam String str) {
		  
		  Map queryMap = new HashMap();
		  queryMap.put("sampleType.iName", "platformunit");
		  List<String> orderByList = new ArrayList<String>();
		  orderByList.add("name");
		  List<SampleSubtype> sampleSubtypeList = sampleSubtypeDao.findByMapDistinctOrderBy(queryMap, null, orderByList, "asc");  
		  
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (SampleSubtype ss : sampleSubtypeList){
	      	 if(ss.getName().indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ ss.getName() +"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	  
		/**
	   * Obtains a json message containing list of ALL platformUnit readTypes (distinct list; such as single and paired)"
	   * Order ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getReadTypesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllReadTypes(@RequestParam String str) {
		  
		  Map queryMap = new HashMap();
		  queryMap.put("resourceType.iName", "mps");
		  List<ResourceCategory> resourceCategoryList = resourceCategoryDao.findByMap(queryMap); 

		  Set<String> readTypeSet = new HashSet<String>();//for adding distinct character to list
	      
		  for(ResourceCategory rc : resourceCategoryList){
			  List<ResourceCategoryMeta> resourceCategoryMetaList = rc.getResourceCategoryMeta();
			  for(ResourceCategoryMeta rcm : resourceCategoryMetaList){
				  if(rcm.getK().indexOf("readType") > -1){
					  String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
					  for(String token : tokens){//token could be single:single
						  String[] colonTokens = token.split(":");
						  readTypeSet.add(colonTokens[0]);
					  }
					  break;
				  }	      		 
			  }
	      }
	      List<String> readTypeList = new ArrayList<String>(readTypeSet);
	      Collections.sort(readTypeList);
	      
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (String readType : readTypeList){
	      	 if(readType.indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ readType +"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                 
	  }
	  
		/**
	   * Obtains a json message containing list of ALL resourceCategoryNames for resources of type "MPS" (list of names of types of sequencing machines, such as Illumina HiSeq2000)
	   * Order ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getMpsResourceCategoryNamesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllMpsResourceCategories(@RequestParam String str) {
		  
		  Map queryMap = new HashMap();
		  queryMap.put("resourceType.iName", "mps");
		  List<ResourceCategory> resourceCategoryList = resourceCategoryDao.findByMap(queryMap); 
		  List<String> resourceCategoryNameList = new ArrayList<String>();
		  for(ResourceCategory rc : resourceCategoryList){
			  resourceCategoryNameList.add(rc.getName());
		  }
	      Collections.sort(resourceCategoryNameList);
	      
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (String resourceCategoryName : resourceCategoryNameList){
	      	 if(resourceCategoryName.indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ resourceCategoryName +"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                 
	  }
	  
		/**
	   * Obtains a json message containing list of ALL resourceNames for resources of type "MPS" (list of names of sequencing machines), such as X123)
	   * ALSO TAG ON THE RESOURCE CATEGORY FOR DISPLAY
	   * Order ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getMpsResourceNamesAndCategoryForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllMpsResourcesAndCategory(@RequestParam String str) {
		  
		  Map queryMap = new HashMap();
		  queryMap.put("resourceType.iName", "mps");
		  List<String> orderByColumnNames = new ArrayList<String>();
		  orderByColumnNames.add("name");
		  String direction = "asc";
		  List<Resource> resourceList = resourceDao.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, direction);
	      
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (Resource r : resourceList){
	      	 if(r.getName().indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	      		 String machineAndType = r.getName() + " - " + r.getResourceCategory().getName();
	       		 jsonString = jsonString + "\""+ machineAndType +"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                 
	  }
	  
		/**
	   * Obtains a json message containing list of ALL sequence run names"
	   * Order ascending
	   * Used to populate a JQuery autocomplete managed input box
	   * @param str
	   * @return json message
	   */
	  @RequestMapping(value="/getSequenceRunNamesForDisplay", method=RequestMethod.GET)
	  public @ResponseBody String getAllSequenceRunNames(@RequestParam String str) {
		  
		  Map queryMap = new HashMap();
		  queryMap.put("resource.resourceType.iName", "mps");
		  queryMap.put("resourceCategory.resourceType.iName", "mps");
		  List<String> orderByColumnNames = new ArrayList<String>();
		  orderByColumnNames.add("name");
		  String direction = "asc";
		  List<Run> runList = runDao.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, direction);
			
	      String jsonString = new String();
	      jsonString = jsonString + "{\"source\": [";
	      for (Run r: runList){
	      	 if(r.getName().indexOf(str) > -1){//note: if str equals "", this, perhaps unexpectedly, evaluates to true
	       		 jsonString = jsonString + "\""+ r.getName()+"\",";
	       	 }
	      }
	      jsonString = jsonString.replaceAll(",$", "") + "]}";
	      return jsonString;                
	  }
	 
}
