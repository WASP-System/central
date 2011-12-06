package edu.yu.einstein.wasp.service.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.yu.einstein.wasp.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService{
	
	  
	  
	  @Autowired
	  private MessageSource messageSource;

	  public String getMessage(String key) {
		  String message=null;
		  Logger logger = Logger.getLogger(getClass());
		  try {
			  
			  /*
			   * get request from ThreadLocal (where Spring MVC keeps it; cannot use @Autowire feature because it makes it require Tomcat container)
			   * Sasha 
			   */
			  HttpServletRequest request = 
				  ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				  .getRequest();
			  LocalizationContext nb=(LocalizationContext)Config.get(request.getSession(), Config.FMT_LOCALIZATION_CONTEXT);
			  message=(String)nb.getResourceBundle().getObject(key);		  
		  } catch (Throwable e) {
			  logger.warn("Cannot resolve message '" + key + "' from resource bundle (" + e.getMessage() + "), will fallback to US locale");
		  }
		  
		  if (message==null){
			  try{
				  message =  messageSource.getMessage(key, null, Locale.US); // try to fallback to US locale
			  } catch (Throwable e){
				  logger.warn("Cannot resolve message '" + key + "' from messageSource (" + e.getMessage() + ")");
			  }
		  }
		  return message;
	  }
	  
	  public String getMessage(String key, Locale locale) {
		  String message=null;
		  Logger logger = Logger.getLogger(getClass());
		  try{
			  message =  messageSource.getMessage(key, null, locale);
		  } catch (Throwable e){
			  logger.warn("Cannot resolve message '" + key + "' from messageSource (" + e.getMessage() + ")");
		  }
		  return message;
	  }
	  
	  public String getMetadataValue(String key){
		  return getMessage(key, Locale.US);
	  }
}
