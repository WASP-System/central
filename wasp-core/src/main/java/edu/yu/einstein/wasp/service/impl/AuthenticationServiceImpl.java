package edu.yu.einstein.wasp.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.MessageService;


@Transactional("entityManager")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Autowired
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Autowired
	private UserPendingDao userPendingDao;

	@Autowired
	private LabPendingDao labPendingDao;

	public void setUserPendingDao(UserPendingDao userPendingDao) {
		this.userPendingDao = userPendingDao;
	}
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Value("${authentication.method.external:false}")
	boolean isAuthenticationExternal;

	

	@Override
	public User getAuthenticatedUser() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 User user = this.userDao.getUserByLogin(authentication.getName());
		 return user;
	}
	@Override
	public boolean isAuthenticated(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().size() == 1){
			Object[] authorities = authentication.getAuthorities().toArray();
			if (authentication.isAuthenticated() && ( (GrantedAuthority) authorities[0] ).getAuthority().equals("ROLE_ANONYMOUS")){
				logger.debug("An anonymous user is authenticated but we don't recognise anonymous users so consider NOT authenticated for wasp system purposes");
				return false;
			}
		}
		return authentication.isAuthenticated();
	}
	
	@Override
	public boolean isAuthenticatedWaspUser(){
		return (getAuthenticatedUser().getId() != null);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public String[] getRoles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    List<GrantedAuthority> col = (List<GrantedAuthority>) authentication.getAuthorities();
	    String[] roles = new String[col.size()];
	    int i = 0;
	    for (GrantedAuthority role : col) {
	    	roles[i] = role.getAuthority();
	    	i++;
	    }
	    return roles;
	}

	@Override
	public boolean isExternallyAuthenticated(){
		for (String role: this.getRoles()){
			if (role.equals("ldap"))
					return true;
		}
		return false;
	}

	@Override
	public boolean isAuthenticationSetExternal() {
		return isAuthenticationExternal;
	}

	@Override
	public boolean isAuthenticatedGuest() {
		for (String role: this.getRoles()){
			if (role.equals("ag"))
					return true;
		}
		return false;
	}
	
	@Override
	public boolean hasRole(String theRole) {
		String[] theRoleList = new String[1];
		theRoleList[0] = theRole;
		return hasRoleInRoleArray(theRoleList, this.getRoles());
	}
	
	@Override
	public boolean hasRoleInRoleArray(String[] rolesToCompare, String[] rolesBaseline) {
		for (String testRole: rolesToCompare){
			if (testRole == null)
				continue;
			for (String myrole: rolesBaseline) {
				if (myrole == null)
					continue;
				if(myrole.equals(testRole))
					return true;
				if(testRole.endsWith("*") && myrole.contains(StringUtils.substringBefore(testRole, "-") + "-"))
					return true; // e.g. theRole = "lm-*" and role = "lm-7"
			}
		}
		return false;
	}
	
	@Override public Integer getRoleValue(String role){
		String[] splitRole = role.split("-");
		if (splitRole.length != 2) {
			return null;
		}
		if (splitRole[1].equals("*")) {
			return null;
		}
		Integer value = null;
		try {
			value = Integer.parseInt(splitRole[1]);
		} catch (NumberFormatException e) {
			return null;
		}
		return value;
	}
	
	@Override
	public boolean hasRoleInRoleArray(String[] rolesToCompare) {
		// baseline to logged in user
		return hasRoleInRoleArray(rolesToCompare, this.getRoles());
	}
	
	@Override
	public boolean isSuperUser() {
		for (String role: this.getRoles()){
			if (role.equals("su"))
					return true;
		}
		return false;
	}
	
	@Override
	public boolean isFacilityMember() {
		for (String role: this.getRoles()){
			if (role.equals("su") || role.equals("fm") || role.equals("ft") || role.equals("sa") || role.equals("ga") || role.equals("da")){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isOnlyDepartmentAdministrator() {
		boolean isDA = false;
		boolean isFacilityMemberOtherThanDA = false;
		for (String role: this.getRoles()){
			if (role.equals("da-*")){
				isDA = true;
			}
			if (role.equals("su") || role.equals("fm") || role.equals("ft") || role.equals("sa") || role.equals("ga")){
				isFacilityMemberOtherThanDA = true;
			}
		}
		if(isDA == true && isFacilityMemberOtherThanDA == false){return true;}
		return false;
	}
		
	@Override
	public boolean authenticate(String name, String password){
		try {
	        Authentication authRequest = new UsernamePasswordAuthenticationToken(name, password);
	        Authentication result = authenticationManager.authenticate(authRequest);
	        SecurityContextHolder.getContext().setAuthentication(result);
	    } catch(AuthenticationException e) {
	    	logger.warn("failed to authenticate:" + e);
	        return false;
	    }
		return true;
	}
	
	@Override
	public boolean authenticates(String name, String password){
		try {
	        Authentication authRequest = new UsernamePasswordAuthenticationToken(name, password);
	        authenticationManager.authenticate(authRequest);
	    } catch(AuthenticationException e) {
	    	logger.warn("failed to authenticate:" + e);
	        return false;
	    }
		return true;
	}

	@Override
	public boolean isLoginNameWellFormed(String login) {
		if (login == null || login.isEmpty()){
			return false;
		}
		if (login.matches("[-@\\.\\w]+")){
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * Checks user table then userPending table (in latter checks for no same login name not-yet approved without same email address)
	 * 
	 */
	@Override
	public boolean isLoginAlreadyInUse(String login, String email) throws LoginNameException {
		if (!this.isLoginNameWellFormed(login)){
			throw new LoginNameException();
		}

		// NV 12132011
		//if (userService.getUserByLogin(login).getUserId() != null){
		if (userDao.getUserByLogin(login).getId() != null){
			return true;
		} else {
			Map<String, String> loginQueryMap = new HashMap<String, String>();
			loginQueryMap.put("login", login);
			for (UserPending up : userPendingDao.findByMap(loginQueryMap)){
				if ( (up.getStatus().equals("WAIT_EMAIL") || up.getStatus().equals("PENDING")) && !up.getEmail().equals(email) ){
					return true; // login name already chosen by someone with different email address
				}
			}
		}
		return false;
	}
	
	  @Override
	  public String encodePassword(String s) {
	      PasswordEncoder encoder = new ShaPasswordEncoder();
	      String hashedPassword = encoder.encodePassword(s, null);

	      return hashedPassword;
	    }
	  	
	    @Override
	  public boolean validatePassword(String s) {
	  	  //http://www.the-art-of-web.com/javascript/validate-password/
	  	  //see orange box on this web page
	  	  //only letters and numbers, at least one number, at least one letter, and at least 8 characters
	  	  //I replaced the \w with [0-9a-zA-Z]
	  	  if (s == null || s.isEmpty()){
	  		  // defensive: not valid if not set
	  		  return false;
	  	  }
	  	  return s.matches("^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}$"); 	  
	    }
	    
	    @Override
	  public boolean matchPassword(String s1, String s2){
	  	  if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()){
	  		  // defensive: must have a value as well as match (do not want to try to match null or empty passwords)
	  		  return false;
	  	  }
	  	  return s1.equals(s2);
	    }
	    
	    @Override
	  public String getRandomPassword(int length){
	  	  if (length < 5 || length > 50){
	  			length = 10; //default 
	  		}
	  		String password = new String();
	  		Random random = new Random();
	  		for (int i=0; i < length; i++){
	  			int ascii = 0;
	  				switch(random.nextInt(3)){
	  		  		case 0:
	  		  			ascii = 48 + random.nextInt(10); // 0-9
	  		  		break;
	  		  		case 1:
	  		  			ascii = 65 + random.nextInt(26); // A-Z 
	  		  		break;
	  		  		case 2:
	  		  			ascii = 97 + random.nextInt(26); // a-z
	  		  		break;	
	  			}
	  				password = password.concat(String.valueOf( (char)ascii ));  
	  		}
	  		return password;
	    }
	    
	    
	    /**
	     * {@inheritDoc}
	     * @return
	     */
	    @Override
	    public Set<Integer> idsOfDepartmentsManagedByCurrentUser(){
	    	Set<Integer> departmentIdList = new HashSet<Integer>();
	    	if (! hasRole("da-*") )
	    		return departmentIdList;
			// get list of departmentId values for this authenticated user
			
			for (String role : getRoles()) {
				if (role.startsWith("da-")){
					Integer deptId = getRoleValue(role);
					if (deptId != null)
						departmentIdList.add(deptId);
				}
			}
			return departmentIdList;
	    }
	    
	    /**
	     * {@inheritDoc}
	     * @return
	     */
	    @Override
	    public Set<Integer> idsOfLabsManagedByCurrentUser(){
	    	Set<Integer> labIdList = new HashSet<Integer>();
	    	if (! (hasRole("lm-*") || hasRole("pi-*")))
	    		return labIdList;
			for (String role : getRoles()) {
				if (role.startsWith("lm-") || role.startsWith("pi-")){
					Integer labId = getRoleValue(role);
					if (labId != null)
						labIdList.add(labId);
				}
			}
			return labIdList;
	    }
	    
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isThisExistingUserPIPending(){
			User me = this.getAuthenticatedUser();
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("primaryUserId", me.getId());
			m.put("status", "PENDING");
			List<LabPending> lpList = labPendingDao.findByMap(m);
			if(lpList.isEmpty()){return false;}
			return true;
		}
}
