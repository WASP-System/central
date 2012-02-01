package edu.yu.einstein.wasp.service;

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
	 * Attempts to authenticate a user based on given credentials and returns true if user authenticates or false if not.
	 * @param name
	 * @param password
	 * @return
	 */
	boolean authenticate(String name, String password);
	
	/**
	 * Checks that a login name is correctly formatted
	 * @param login
	 * @return
	 */
	boolean isLoginNameWellFormed(String login);
	
	/**
	 * Checks database for existing use of supplied login
	 * @param login
	 * @param email
	 * @return
	 * @throws LoginNameException 
	 */
	boolean isLoginAlreadyInUse(String login, String email) throws LoginNameException;
	
	/**
	 * Logs out currently logged in user
	 */
	void logoutUser();

	/**
	 * Is authenticated
	 * @return boolean
	 */
	boolean isAuthenticated();

	/**
	 * Check if user authenticates but DO NOT set authentication context
	 * @param name
	 * @param password
	 * @return
	 */
	boolean authenticates(String name, String password);
	
	
}
