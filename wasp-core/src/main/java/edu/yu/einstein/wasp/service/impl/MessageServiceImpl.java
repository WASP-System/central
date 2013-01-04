package edu.yu.einstein.wasp.service.impl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.service.MessageService;

@Service
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
			logger.warn("Cannot resolve message '" + key + "' from messageSource (" + e.getMessage() + ")");
		}
		return message;
	}

	@Override
	public String getMetadataValue(String key) {
		return getMessage(key, Locale.US);
	}
}
