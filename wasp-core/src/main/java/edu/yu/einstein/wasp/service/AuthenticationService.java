package edu.yu.einstein.wasp.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.User;

public interface AuthenticationService {
	
	/**
	 * Gets current authenticated User from security context
	 * @return {@link User}
	 */
	public User getAuthenticatedUser();

	/**
	 * Gets list of Roles assigned to User from security context
	 * @return roles
	 */
	public String[] getRoles();
	
	/**
	 * Returns true if user is externally authenticated (e.g. LDAP / AD)
	 * @return
	 */
	public boolean isExternallyAuthenticated();
	
	/**
	 * Returns true if authentication set to be external
	 * @return
	 */
	public boolean isAuthenticationSetExternal();
	
	/**
	 * Returns true if User is an authenticated guest (i.e. has no WASP account but authenticates successfully
	 * against LDAP / AD
	 * @return
	 */
	public boolean isAuthenticatedGuest();
	
	
	/**
	 * Returns true if User has the role of theRole. theRole can be "su", "da", etc.
	 * @return
	 */
	public boolean hasRole(String theRole);
	
	/**
	 * Returns true if User has role of su, else returns false
	 * @return
	 */
	public boolean isSuperUser();
	
	/**
	 * Returns true if User has role of su, fm, ft, sa, ga, or da, else returns false
	 * @return
	 */
	public boolean isFacilityMember();

	/**
	 * Returns true if User has role of da AND IS NOT also a su, fm, ft, sa, or ga; else returns false
	 * @return
	 */
	public boolean isOnlyDepartmentAdministrator();

	/**
	 * Attempts to authenticate a user based on given credentials and returns true if user authenticates or false if not.
	 * @param name
	 * @param password
	 * @return
	 */
	public boolean authenticate(String name, String password);
	
	/**
	 * Checks that a login name is correctly formatted
	 * @param login
	 * @return
	 */
	public boolean isLoginNameWellFormed(String login);
	
	/**
	 * Checks database for existing use of supplied login
	 * @param login
	 * @param email
	 * @return
	 * @throws LoginNameException 
	 */
	public boolean isLoginAlreadyInUse(String login, String email) throws LoginNameException;
	
	/**
	 * Logs out currently logged in user
	 */
	public void logoutUser();

	/**
	 * Is authenticated
	 * @return boolean
	 */
	public boolean isAuthenticated();

	/**
	 * Check if user authenticates but DO NOT set authentication context
	 * @param name
	 * @param password
	 * @return
	 */
	public boolean authenticates(String name, String password);

	/**
	 * Returns true if logged in User has ANY of the roles in RoleArray. theRole can be "su", "da", etc.
	 * @param roleArray
	 * @return
	 */
	public boolean hasRoleInRoleArray(String[] roleArray);

	
	/**
	 * Returns true if any role in rolesToCompare is present in rolesBaseline. theRole can be "su", "da", etc.
	 * @param roleArray
	 * @return
	 */
	public boolean hasRoleInRoleArray(String[] rolesToCompare, String[] rolesBaseline);
	
	public String encodePassword(String s);
	  public boolean validatePassword(String s);
	  public boolean matchPassword(String s1, String s2);
	  public String getRandomPassword(int length);

   
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
	    public boolean hasPermission(String permission, Map<String, Integer> parameterMap) throws IOException;
	    
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
	    public boolean hasPermission(String permission) throws IOException;

	    /**
	     * Get value associates with a role or null if none. E.g. providing a role of "fm-7" returns 7, whereas "fm-*" or "fm" returns null
	     * @param role
	     * @return
	     */
	    public Integer getRoleValue(String role);

	    /**
	     * Gets a list of the ids of deparments managed by the currently logged in user or returns an empty list if none.
	     * @return
	     */
		public Set<Integer> idsOfDepartmentsManagedByCurrentUser();

		/**
		 * Gets a list of the ids of labse managed by the currently logged in user or returns an empty list if none.
		 * @return
		 */
		public Set<Integer> idsOfLabsManagedByCurrentUser();
	    
		/**
		 * is the authenticated user PI pending? (check labpending table)
		 * NOTE THAT THIS IS AN EXISTING USER (so, he/she is already a member of some lab)
		 * @return boolean
		 */
	    public boolean isThisExistingUserPIPending();
	
}
