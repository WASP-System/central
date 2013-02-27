package edu.yu.einstein.wasp.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.MessageService;


@Service
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
	@Qualifier("messageServiceImpl")
	private MessageService messageService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Value("${authentication.method.external:false}")
	boolean isAuthenticationExternal;

	public static HttpServletRequest getHttpServletRequest() {
		try {
			HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			return request;
		} catch (Throwable e) {
			// logger.warn("could not get HttpServletRequest");
		}
		return null;
	}

	@Override
	public User getAuthenticatedUser() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 User user = this.userDao.getUserByLogin(authentication.getName());
		 return user;
	}
	@Override
	public boolean isAuthenticated(){
		return (getAuthenticatedUser().getUserId() != null);
	}
	
	@Override
	public void logoutUser(){
		if (this.isAuthenticated()){
			SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
			logoutHandler.setInvalidateHttpSession(true);
			logoutHandler.logout(getHttpServletRequest(), null, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getRoles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    List<GrantedAuthority>  col = (List<GrantedAuthority>) authentication.getAuthorities();
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
	    	logger.warn("failed to authenticate:" + e.getCause());
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
	    	logger.warn("failed to authenticate:" + e.getCause());
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
		if (userDao.getUserByLogin(login).getUserId() != null){
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
		 * Enables parsing of Spring security expressions against the logged in user's security context. Using the 
		 * parameter map, parameters can be substituted in the permission string. Consider "hasRole('su') or hasRole('fm') or hasRole('jv-#jobId')".
		 * If parameterMap contains the parameter "jobId" with value "3" the following will be evaluated: ""
		 * "hasRole('su') or hasRole('fm') or hasRole('jv-3')"
		 * 
		 * See://static.springsource.org/spring-security/site/docs/3.0.x/reference/el-access.html for more expression options
		 * @param permsission
		 * @return
		 * @throws IOException
		 */
	    @Override
	    public boolean hasPermission(String permission, Map<String, Integer> parameterMap) throws IOException {
	    	Assert.assertParameterNotNull(permission, "permission must be set");
	    	if (parameterMap != null && !parameterMap.isEmpty()){
	    		for (String key: parameterMap.keySet()){
	    			parameterMap.get(key);
	    			permission = permission.replaceAll("#" + key, parameterMap.get(key).toString());
	    		}
	    		if (permission.contains("#"))
	    			throw new IOException("not all placeholders in permission string have been resolved from parameter map");
	    	}
	    	return hasPermission(permission);
	    }
	    
	    
		/**
		 * Enables parsing of Spring security expressions against the logged in user's security context.
		 * 
		 * Can parse a string such as "hasRole('su') or hasRole('fm') or hasRole('ft')".
		 * 
		 * See://static.springsource.org/spring-security/site/docs/3.0.x/reference/el-access.html for more expression options
		 * @param permsission
		 * @return
		 * @throws IOException
		 */
	    @Override
		public boolean hasPermission(String permission) throws IOException {
	    	logger.debug("Evaluating permission string: " + permission);
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				return false;
			}
			SecurityExpressionHandler<FilterInvocation> handler = getExpressionHandler();
			Expression accessExpression;
			try {
				accessExpression = handler.getExpressionParser().parseExpression(permission);
			} catch (ParseException e) {
				IOException ioException = new IOException();
				ioException.initCause(e);
				throw ioException;
			}
			return ExpressionUtils.evaluateAsBoolean(accessExpression, createExpressionEvaluationContext(handler));

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
	    
	    

		private static SecurityExpressionHandler<FilterInvocation> getExpressionHandler() throws IOException {
			ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getHttpServletRequest().getSession().getServletContext());
			for (SecurityExpressionHandler<FilterInvocation> handler : appContext.getBeansOfType(SecurityExpressionHandler.class).values()) {
				if (FilterInvocation.class.equals(GenericTypeResolver.resolveTypeArgument(handler.getClass(),
				SecurityExpressionHandler.class))) {
					return handler;
				}
			}
			throw new IOException("No visible WebSecurityExpressionHandler instance could be found in the application "
			+ "context. There must be at least one in order to support expressions in JSP 'authorize' tags.");
		}


		private static EvaluationContext createExpressionEvaluationContext(SecurityExpressionHandler<FilterInvocation> handler) {
			HttpServletRequest request = getHttpServletRequest();
			FilterInvocation f = new FilterInvocation(request.getServletPath(), request.getMethod());
			return handler.createEvaluationContext(SecurityContextHolder.getContext().getAuthentication(), f);

		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isThisExistingUserPIPending(){
			User me = this.getAuthenticatedUser();
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("primaryUserId", me.getUserId());
			m.put("status", "PENDING");
			List<LabPending> lpList = labPendingDao.findByMap(m);
			if(lpList.isEmpty()){return false;}
			return true;
		}
}
