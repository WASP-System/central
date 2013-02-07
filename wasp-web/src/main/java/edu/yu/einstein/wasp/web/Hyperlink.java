package edu.yu.einstein.wasp.web;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.service.MessageServiceWebapp;

/**
 * Represents a localized label and url for hyperlinking
 * 
 * @author asmclellan
 */
public class Hyperlink {
	
	protected String localizedLabelKey;
	
	protected String targetLink;
	
	protected MessageServiceWebapp messageService;

	public void setMessageService(MessageServiceWebapp messageService) {
		this.messageService = messageService;
	}

	public Hyperlink(String localizedLabelKey, String targetLink) {
		this.localizedLabelKey = localizedLabelKey;
		this.targetLink = targetLink;
	}
	
	public String getLocalizedLabelKey() {
		return localizedLabelKey;
	}

	public void setLocalizedLabelKey(String localizedLabelKey) {
		this.localizedLabelKey = localizedLabelKey;
	}
	
	/**
	 * Translates localized label key into display text, e.g. 'wasp.my.message' to 'My Message'. If the label key is not translated
	 * you should try providing a messageService
	 * @return
	 */
	public String getLocalizedLabel() {
		if (messageService == null)
			return localizedLabelKey;
		return messageService.getMessage(localizedLabelKey);
	}
	
	/**
	 * Translates localized label key into display text, e.g. 'wasp.my.message' to 'My Message'.
	 * @return
	 */
	public String getLocalizedLabel(MessageServiceWebapp messageService) {
		this.messageService = messageService;
		if (messageService == null)
			return localizedLabelKey;
		return this.messageService.getMessage(localizedLabelKey);
	}
	
	public String getTargetLink() {
		return targetLink;
	}

	public void setTargetLink(String targetLink) {
		this.targetLink = targetLink;
	}


}
