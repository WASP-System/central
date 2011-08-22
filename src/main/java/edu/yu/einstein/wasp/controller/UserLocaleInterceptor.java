package edu.yu.einstein.wasp.controller;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;

public class UserLocaleInterceptor extends HandlerInterceptorAdapter {
   
	private final static String LOCK="wasp_"+SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME;
	
	@Autowired
	private UserService userService;

	 private static final Logger log = Logger.getLogger(UserLocaleInterceptor.class);
	
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

    	//final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

    	//retun if we already set user's locale
    	if (request.getSession().getAttribute(LOCK)!=null) {
   		 	return true;
    	}
    	
    	final Principal principal=request.getUserPrincipal();

    	//not logged in - do thing
    	if (principal==null || principal.getName()==null) {
    		return true;
    	}

    	//if we here we are logged in.
    	//do nothing if locale is already set
    	if (request.getSession().getAttribute(LOCK)!=null) {
    		 return true;
    	}
    	
    	
    	String login=principal.getName();
    	
    	User user=userService.getUserByLogin(login);
    	
    	if (user==null) {
    		log.error("Logged in user not found in db??? "+login);
    		return true;
    	}
    	
    	if (user.getLocale()==null) {
    		log.error("User without locale!!!"+login);
    		return true;
    	}
    	
    	String lang = user.getLocale().substring(0, 2);
		String cntry = user.getLocale().substring(3);
		
		Locale locale = new Locale(lang, cntry);
		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

		request.getSession().setAttribute("jqLang", lang);
		
		request.getSession().setAttribute(LOCK, "locale set");
    	
        return true;
    	
    }
}