/**
 * 
 */
package edu.yu.einstein.wasp.integration.messaging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.service.AuthenticationService;

/**
 * @author calder
 *
 */
public class Security implements MessageSelector {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	private Logger logger = LoggerFactory.getLogger(Security.class);

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public boolean accept(Message<?> message) {
		String user = message.getHeaders().get("user").toString();
		String password = String.copyValueOf((char[]) message.getHeaders().get("password")); 
		boolean validUser = authenticationService.authenticate(user, password);
		logger.debug(user +" is valid user=" + String.valueOf(validUser));
		if (!validUser)
			return false;
		return authenticationService.hasRole("su");
	}

}
