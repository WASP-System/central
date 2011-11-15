package edu.yu.einstein.wasp.service;

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
	
}
