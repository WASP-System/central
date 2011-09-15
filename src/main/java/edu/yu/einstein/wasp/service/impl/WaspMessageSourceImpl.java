
/**
 *
 * Meta.java 
 * @author echeng (table2type.pl)
 *  
 * the Meta object
 *
 *
 */

package edu.yu.einstein.wasp.service.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.Assert;


public class WaspMessageSourceImpl extends AbstractMessageSource implements MessageSource {


	/** Map from 'code + locale' keys to message Strings */
	private final Map<String, String> messages = new HashMap<String, String>();

	
	
	private final Map<String, MessageFormat> cachedMessageFormats = new HashMap<String, MessageFormat>();


	public Set<String> getKeys(Locale locale) {
		Set<String> keys=new TreeSet<String>();
		
		String localeStr=locale.toString();
		
		for(String key:messages.keySet()) {
			if (key.endsWith(localeStr)) keys.add(key.substring(0, key.length()-localeStr.length()-1));
		}
		
		return keys;
		
	}
	
	
	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String msg= this.messages.get(code + "_" + locale.toString());
		if (msg == null) {
			msg = this.messages.get(code + "_" + Locale.US.toString());//fallback to US locale if message not defined. Sasha
			if (msg==null) msg=code;//fallback to code if message not defined. Sasha
		}
		return msg;
	}

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String key = code + "_" + locale.toString();
		String msg = this.messages.get(key);
		if (msg == null) {
			msg = this.messages.get(code + "_" + Locale.US.toString());//fallback to US locale if message not defined. Sasha
			if (msg==null) msg=code;//fallback to code if message not defined. Sasha
		}
		synchronized (this.cachedMessageFormats) {
			MessageFormat messageFormat = this.cachedMessageFormats.get(key);
			if (messageFormat == null) {
				messageFormat = createMessageFormat(msg, locale);
				this.cachedMessageFormats.put(key, messageFormat);
			}
			return messageFormat;
		}
	}

	/**
	 * Associate the given message with the given code.
	 * @param code the lookup code
	 * @param locale the locale that the message should be found within
	 * @param msg the message associated with this lookup code
	 */
	public void addMessage(String code, Locale locale, String msg) {
		Assert.notNull(code, "Code must not be null");
		Assert.notNull(locale, "Locale must not be null");
		Assert.notNull(msg, "Message must not be null");
		this.messages.put(code + "_" + locale.toString(), msg);
		if (logger.isDebugEnabled()) {
			logger.debug("Added message [" + msg + "] for code [" + code + "] and Locale [" + locale + "]");
		}
	}

	/**
	 * Associate the given message values with the given keys as codes.
	 * @param messages the messages to register, with messages codes
	 * as keys and message texts as values
	 * @param locale the locale that the messages should be found within
	 */
	public void addMessages(Map<String, String> messages, Locale locale) {
		Assert.notNull(messages, "Messages Map must not be null");
		for (Map.Entry<String, String> entry : messages.entrySet()) {
			addMessage(entry.getKey(), locale, entry.getValue());
		}
	}


	@Override
	public String toString() {
		return getClass().getName() + ": " + this.messages;
	}

	
}
