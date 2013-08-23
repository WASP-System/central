package edu.yu.einstein.wasp.service.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import edu.yu.einstein.wasp.service.MessageServiceWebapp;

/**
 * 
 * @author asmclellan
 *
 */
@Service
@Transactional("entityManager")
public class MessageServiceWebappImpl extends MessageServiceImpl implements MessageServiceWebapp{

	@Override
	public String getMessage(String key) {
		String message = key; // returns the original string by default
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			message =  messageSource.getMessage(key, null, RequestContextUtils.getLocale(request));
		} catch (Exception e) {
			logger.trace("Cannot resolve message '" + key + "' using Locale from resource HttpServletRequest (" + e.getMessage() + "), will fallback to US locale");
			try {
				message = messageSource.getMessage(key, null, Locale.US); // try to fallback to US locale
			} catch (NoSuchMessageException nsme) {
				logger.trace("Cannot resolve message '" + key + "' from messageSource (" + nsme.getMessage() + ")");
			}
		}
		return message;
	}

}
