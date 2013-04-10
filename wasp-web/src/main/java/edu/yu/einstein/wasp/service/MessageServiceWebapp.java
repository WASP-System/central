package edu.yu.einstein.wasp.service;

/**
 * 
 * @author asmclellan
 *
 */
public interface MessageServiceWebapp extends MessageService{

	/** Get localized message using locale from HttpServletRequest
	 * @param key
	 * @return
	 */
	public String getMessage(String key);
	
}
