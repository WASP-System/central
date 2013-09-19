package edu.yu.einstein.wasp.service.impl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.MessageService;

@Transactional("entityManager")
public class MessageServiceImpl implements MessageService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String getMessage(String key, Locale locale) {
		String message = key; // returns the original string by default
		try {
			message = DBResourceBundle.MESSAGE_SOURCE.getMessage(key, null, locale);
		} catch (NoSuchMessageException e) {
			logger.trace("Cannot resolve message '" + key + "' from messageSource (" + e.getMessage() + ")");
		}
		return message;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage(String key) {
		return getMessage(key, Locale.US);
	}
	


}
