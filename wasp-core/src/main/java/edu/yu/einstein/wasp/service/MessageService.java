package edu.yu.einstein.wasp.service;

import java.util.Locale;

public interface MessageService {
	
	/**
	 * Get message
	 * @param key
	 * @return
	 */
	public String getMessage(String key);

	/**
	 * Get localized parameterized message for specified locale.
	 * Can parameterize a message, e.g. where message associated with key is "Programmer {0} {1} age {2} wears a {3} hat"
	 * and: args = {"John", "Smith", "red"}
	 * @param key
	 * @param args
	 * @param locale
	 * @return
	 */
	public String getMessage(String key, Object[] args, Locale locale);
	
	/**
	 * Get localized message for specified locale
	 * @param key
	 * @param locale
	 * @return
	 */
	public String getMessage(String key, Locale locale);
	
	/**
	 * Get parameterized message for specified locale.
	 * Can parameterize a message, e.g. where message associated with key is "Programmer {0} {1} age {2} wears a {3} hat"
	 * and: args = {"John", "Smith", 40, "red"}
	 * @param key
	 * @param args
	 * @return
	 */
	public String getMessage(String key, Object[] args);
	
	
}
