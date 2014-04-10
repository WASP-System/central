package edu.yu.einstein.wasp.service;

import java.io.IOException;
import java.util.Map;

public interface WebAuthenticationService extends AuthenticationService {
	
	/**
	 * Logs out currently logged in user
	 */
	public void logoutUser();
	
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
    


}
