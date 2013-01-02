package edu.yu.einstein.wasp.service.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.yu.einstein.wasp.service.MessageServiceWebapp;

@Service
@Transactional("entityManager")
public class MessageServiceWebappImpl extends MessageServiceImpl implements MessageServiceWebapp{

	@Override
	public String getMessage(String key) {
		String message = key; // returns the original string by default
		try {
			/*
			 * get request from ThreadLocal (where Spring MVC keeps it; cannot use @Autowire feature because it makes it require 
			 * Tomcat container) Sasha
			 */
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			LocalizationContext nb = (LocalizationContext) Config.get(request.getSession(), Config.FMT_LOCALIZATION_CONTEXT);
			message = (String) nb.getResourceBundle().getObject(key);
		} catch (Throwable e) {
			logger.warn("Cannot resolve message '" + key + "' from resource bundle (" + e.getMessage() + "), will fallback to US locale");
		}

		if (message.equals(key)) {
			try {
				message = messageSource.getMessage(key, null, Locale.US); // try to fallback to US locale
			} catch (Throwable e) {
				logger.warn("Cannot resolve message '" + key + "' from messageSource (" + e.getMessage() + ")");
			}
		}
		return message;
	}

}
