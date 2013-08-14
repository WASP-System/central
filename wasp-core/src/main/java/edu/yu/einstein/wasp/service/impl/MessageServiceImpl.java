package edu.yu.einstein.wasp.service.impl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.service.MessageService;

@Service
@Qualifier("messageServiceImpl")
@Transactional("entityManager")
public class MessageServiceImpl implements MessageService {

	@Autowired
	protected MessageSource messageSource;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String getMessage(String key, Locale locale) {
		String message = key; // returns the original string by default
		try {
			message = messageSource.getMessage(key, null, locale);
		} catch (Throwable e) {
			logger.trace("Cannot resolve message '" + key + "' from messageSource (" + e.getMessage() + ")");
		}
		return message;
	}
	
	/**
	 * Defaults to message defined for Locale.US
	 */
	@Override
	public String getMessage(String key) {
		return getMessage(key, Locale.US);
	}

}
