package edu.yu.einstein.wasp.web;

import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.service.MessageService;

/**
 * Represents a localized label and url for hyperlinking. Extends Hyperlink by providing a i18n key
 * instead of of a hyperlink label which is translated into a localized label
 * 
 * @author asmclellan
 */
public class WebHyperlink extends Hyperlink implements Comparable<WebHyperlink>{
	
	protected String localizedLabelKey;
	
	protected MessageService messageService;

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	public WebHyperlink(Hyperlink hyperlink) {
		super(null, hyperlink.getTargetLink());
		this.localizedLabelKey = hyperlink.getLabel();
	}
	
	public WebHyperlink(Hyperlink hyperlink, MessageService messageService) {
		super(null, hyperlink.getTargetLink());
		this.localizedLabelKey = hyperlink.getLabel();
		this.messageService = messageService;
	}

	public WebHyperlink(String localizedLabelKey, String targetLink) {
		super(null, targetLink);
		this.localizedLabelKey = localizedLabelKey;
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
	@Override
	public String getLabel() {
		if (messageService == null)
			return localizedLabelKey;
		this.label = messageService.getMessage(localizedLabelKey);
		return this.label;
	}
	
	/**
	 * Translates localized label key into display text, e.g. 'wasp.my.message' to 'My Message'.
	 * @return
	 */
	public String getLabel(MessageService messageService) {
		this.messageService = messageService;
		if (messageService == null)
			return localizedLabelKey;
		this.label =  this.messageService.getMessage(localizedLabelKey);
		return this.label;
	}

	@Override
	public int compareTo(WebHyperlink o) {
		return getLabel().compareToIgnoreCase(o.getLabel());
	}


}
