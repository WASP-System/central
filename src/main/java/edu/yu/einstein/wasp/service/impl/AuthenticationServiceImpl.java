package edu.yu.einstein.wasp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.tribes.util.Logs;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.MetaHelper;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserPendingService userPendingService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public User getAuthenticatedUser() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 User user = this.userService.getUserByLogin(authentication.getName());
		 return user;
	}

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
		String authenticationIsExternalString = messageService.getMetadataValue("wasp.isAuthenticationExternal.data");
		if (authenticationIsExternalString != null && 
				!authenticationIsExternalString.isEmpty() && 
				(authenticationIsExternalString.toUpperCase().equals("TRUE") ||
				authenticationIsExternalString.equals("1") ) ){
			return true;
		}
		return false;
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
	public boolean authenticate(String name, String password){
		try {
	        Authentication request = new UsernamePasswordAuthenticationToken(name, password);
	        Authentication result = authenticationManager.authenticate(request);
	        SecurityContextHolder.getContext().setAuthentication(result);
	    } catch(AuthenticationException e) {
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
		if (userService.getUserByLogin(login).getUserId() != 0){
			return true;
		} else {
			Map loginQueryMap = new HashMap();
			loginQueryMap.put("login", login);
			for (UserPending up : (List<UserPending>) userPendingService.findByMap(loginQueryMap)){
				if ( (up.getStatus().equals("WAIT_EMAIL") || up.getStatus().equals("PENDING")) && !up.getEmail().equals(email) ){
					return true; // login name already chosen by someone with different email address
				}
			}
		}
		return false;
	}
}
