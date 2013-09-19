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
	 * Get localized message for specified locale
	 * @param key
	 * @param locale
	 * @return
	 */
	public String getMessage(String key, Locale locale);

	/**
	 * Attempts to resolve a nested code: e.g 'user.name.label=plugin.user.name' where the i18n file pertaining to 'locale' contains 
	 * 'plugin.user.name=Jason'
	 * Given code='user.name.label', this method returns "Jason".
	 * If nested value fails to resolve initially (using provided locale) then various attempts to resolve are made:<ul>
	 * <li>attempts to resolve nested code as Locale.US (locale isn't set to Locale.US)</li>
	 * <li>attempts to resolve 'code' with provided locale</li>
	 * <li>finally returns value for 'code' resolved as Locale.US</li>
	 * <li>throws NoSuchMessageException if unable to resolve at all</li></ul>
	 * 
	 * @param key - the code to lookup up, such as 'user.name.label'
	 * @param locale - the Locale in which to do the lookup 
	 * @return
	 */
	public String getNestedMessage(String key, Locale locale);
	
	/**
	 * Attempts to resolve a nested code: e.g 'user.name.label=plugin.user.name' where the i18n file pertaining to 'locale' contains 
	 * 'plugin.user.name=Jason'
	 * Given code='user.name.label', this method returns "Jason".
	 * If nested value fails to resolve initially (using provided locale) then various attempts to resolve are made:<ul>
	 * <li>attempts to resolve nested code as Locale.US (locale isn't set to Locale.US)</li>
	 * <li>attempts to resolve 'code' with provided locale</li>
	 * <li>finally returns value for 'code' resolved as Locale.US</li>
	 * <li>throws NoSuchMessageException if unable to resolve at all</li></ul>
	 * 
	 * @param key - the code to lookup up, such as 'user.name.label'
	 * @return
	 */
	public String getNestedMessage(String key);
	
	
}
