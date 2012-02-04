package edu.yu.einstein.wasp.controller;

/**
*
* 1. Stores user's locale to http session under SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME key so Spring MVC can serve proper ResourceBundles
*  
* 2. Initializes and stores locale-specific bundle in http session udner key Config.FMT_LOCALIZATION_CONTEXT so JSTL can serve proper bundles.
* 
* 3. TODO: cache bundles for #2
* 
* Executed once on app startup
*  
* @author Sasha Levchuk
*
*
**/

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UiFieldService;
import edu.yu.einstein.wasp.service.UserService;

public class UserLocaleInterceptor extends HandlerInterceptorAdapter {
   
	private final static String LOCK="wasp_"+SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UiFieldService uiFieldService;
	
    private static final Logger log = Logger.getLogger(UserLocaleInterceptor.class);
	
    @Override
	public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

    	
    	//return if we have already set user's locale
    	if (request.getSession().getAttribute(LOCK)!=null) {
   		 	return true;
    	}
    	
    	final Principal principal=request.getUserPrincipal();

    	//not logged in - do nothing
    	if (principal==null || principal.getName()==null) {
    		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.US);
    		request.getSession().setAttribute("jqLang", Locale.US.getLanguage());
    		
    		if (Config.get(request.getSession(),Config.FMT_LOCALIZATION_CONTEXT)==null) initJSTLResourceBundle(Locale.US,request.getSession());
    		return true;
    	}

    	//if we here -- we are logged in.
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
    		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.US);
    		request.getSession().setAttribute("jqLang", Locale.US.getLanguage());
    		if (Config.get(request.getSession(),Config.FMT_LOCALIZATION_CONTEXT)==null) initJSTLResourceBundle(Locale.US,request.getSession());
    		return true;
    	}
    	
    	String lang = user.getLocale().substring(0, 2);
		String cntry = user.getLocale().substring(3);
		
		Locale locale = new Locale(lang, cntry);
		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

		request.getSession().setAttribute("jqLang", lang);
		
		request.getSession().setAttribute(LOCK, "locale set");
    	
		initJSTLResourceBundle(locale, request.getSession()); 
		
        return true;    	
    }
    
	private void initJSTLResourceBundle(Locale locale, HttpSession session) {

		StringBuffer buf = new StringBuffer("");
	
		List<UiField> res = uiFieldService.findAll();

		for (UiField f : res) {

			if (f.getLocale()==null || f.getLocale().length()!=5) {
				log.error("invalid locale, defaulting to US "+f);
				f.setLocale("en_US");
			}
			
			String key = f.getArea() + "." + f.getName() + "."
					+ f.getAttrName();

			String lang = f.getLocale().substring(0, 2);
			
			
			if (!lang.equals(locale.getLanguage())) continue;
		

			buf.append(key + "=" + f.getAttrValue()
					+ System.getProperty("line.separator"));

		}

		InputStream is = null;
		try {

			is = new ByteArrayInputStream(buf.toString().getBytes("UTF-8"));
			
			ResourceBundle newBundle = new PropertyResourceBundle(is);

			Config.set(session,Config.FMT_LOCALIZATION_CONTEXT, new LocalizationContext(newBundle, locale));
			
		} catch (Throwable e) {
			throw new IllegalStateException("cant init JSTLResourceBundle");
		}

		
	}

}