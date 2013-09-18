
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;


public class WaspMessageSourceImpl extends AbstractMessageSource implements MessageSource {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());


	/** Map from 'code + locale' keys to message Strings */
	private final Map<String, String> messages = new HashMap<String, String>();

	
	public boolean contains(String code, Locale locale)  {
		String key = code + "_" + locale.toString();
		
		return messages.containsKey(key) && messages.get(key)!=null;
	}
	
	
	private final Map<String, MessageFormat> cachedMessageFormats = new HashMap<String, MessageFormat>();


	public Set<String> getKeys(Locale locale) {
		Set<String> keys=new TreeSet<String>();
		
		String localeStr=locale.toString();
		
		for(String key:messages.keySet()) {
			if (key.endsWith(localeStr)) keys.add(key.substring(0, key.length()-localeStr.length()-1));
		}
		
		return keys;
		
	}
	
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
	 * @param code - the code to lookup up, such as 'user.name.label'
	 * @param args - Array of arguments that will be filled in for params within the message (params look like "{0}", "{1,date}", "{2,time}" within a 
	 * message), or null if none.
	 * @param locale - the Locale in which to do the lookup 
	 * @return
	 * @throws NoSuchMessageException
	 */
	public String getNestedMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException{
		boolean isUsLocale = locale.equals(Locale.US);
		String codeValue = null;
		
		// First try and get a value for the provided code. It should be under Locale.US but we cannot assume that.
		try {
			codeValue = getMessage(code, null, Locale.US); // try and get the value of code from Locale.US (expected). Set args to null at this stage
		} catch (NoSuchMessageException e){
			if (isUsLocale)
				throw new NoSuchMessageException("Unable to retrieve a message with key code = '" + code + "' and locale = '" + Locale.US.toString() + "'");
			logger.debug("Unable to retrieve nested message with key code = '" + code + "' and locale = '" + Locale.US.toString() + "'. Going to try '" 
				+ locale.toString() + "'");
			try{
				codeValue = getMessage(code, null, locale); // return nested message. Set args to null at this stage
			} catch (NoSuchMessageException e1){
				throw new NoSuchMessageException("Unable to retrieve a message with key code = '" + code + "' and locale = '" + locale.toString() + "' or '" 
						+  Locale.US.toString() + "'");
			}
		}
		
		// to get here we have managed to resolve the code to a value. Now lets see if we can resolve that value as a code itself. First try with locale then
		// Locale.US (if not the same as locale)
		try{
			return getMessage(codeValue, args, locale);// return message using 'codeValue' and 'args' provided
		} catch (NoSuchMessageException e){
			if (isUsLocale){
				logger.debug("Unable to retrieve nested message with key code = '" + codeValue + "' and locale = '" + locale.toString() 
						+ "'. Going to return the non-nested value");
			}
			else {
				logger.debug("Unable to retrieve nested message with key code = '" + codeValue + "' and locale = '" + locale.toString() 
						+ "'. Going to try Locale.US");
				try{
					return getMessage(codeValue, args, Locale.US); // return message using 'codeValue' and 'args' provided from Locale.US
				} catch (NoSuchMessageException e1){
					logger.debug("Unable to retrieve nested message with key code = '" + codeValue + "' and locale = '" + Locale.US.toString() 
							+ "'. Going to return the non-nested value");
				}
			}
		}
		
		// Ok so it seems the value obtained with 'code' cannot be reolved as a nested code itself. We should therefore return the internationalized 
		// version of that value taking into account the provided args.
		if (!isUsLocale){
			try{
				return getMessage(code, args, locale); // return message using 'code' and 'args' provided
			} catch (NoSuchMessageException e1){
				logger.debug("Unable to retrieve nested message with key code = '" + code + "' and locale = '" +locale.toString() + "'. Going to return value for Locale.US"); 
			}
		}
		return getMessage(code, args, Locale.US); // return message using 'code' and 'args' provided for  Locale.US
	}
	

	
	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		
		if (code==null) return null;
		
		String msg = getMessageInternal(code, locale);
		
		if (msg!=null) return msg;
			
		if (StringUtils.split(code, ".").length==3) {//valid 3-part key. return "code" instead of the message
			return code;
		} else {//invalid key. return null. 
			return null;
		}
		
	}

	public String getUSMessage(String code,String defaultMessage) {
		String msg=messages.get(code + "_" + Locale.US.toString());
		if (msg==null) msg=defaultMessage;
		return msg;
	}
	private String getMessageInternal(String code, Locale locale) {
		String msg = this.messages.get(code + "_" + locale.toString());
		if (msg == null) messages.get(code + "_" + Locale.US.toString());//fallback to US locale if message not defined. Sasha
		return msg;
	}
	
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String key = code + "_" + locale.toString();
		String msg = getMessageInternal(code, locale);
		if (msg == null) {
			if (StringUtils.split(code, ".").length==3) {//valid 3-part key. return "code" instead of the message
				msg=code;//fallback to code if message not defined. Sasha
			} else {//invalid key.  
				msg=null;
			}			 
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
		logger.trace("Added message [" + msg + "] for code [" + code + "] and Locale [" + locale + "]");
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
