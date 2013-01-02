package edu.yu.einstein.wasp.service;

import java.util.Locale;

public interface MessageService {

	/**
	 * Get localized message for specified locale
	 * @param key
	 * @param locale
	 * @return
	 */
	public String getMessage(String key, Locale locale);
	
	/**
	 * Get a WASP metadata message
	 * @param key
	 * @return
	 */
	public String getMetadataValue(String key);
	
}
