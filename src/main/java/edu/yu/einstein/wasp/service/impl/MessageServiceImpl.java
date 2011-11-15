package edu.yu.einstein.wasp.service.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService{
	
	  @Autowired
	  HttpServletRequest request;
	  
	  @Autowired
	  private MessageSource messageSource;

	  public String getMessage(String key) {
		  String message=null;
		  Logger logger = Logger.getLogger(getClass());
		  try {
			  LocalizationContext nb=(LocalizationContext)Config.get(request.getSession(), Config.FMT_LOCALIZATION_CONTEXT);
			  message=(String)nb.getResourceBundle().getObject(key);		  
		  } catch (Throwable e) {
			  logger.warn("Cannot resolve message '" + key + "' from resource bundle (" + e.getMessage() + ")");
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
}
