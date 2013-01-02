package edu.yu.einstein.wasp.taskMapping;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;

public abstract class WaspTaskMapping {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String localizedLabelKey;
	
	private String targetLink;
	
	private String permission;
	
	private Integer dashboardSortOrder;
	
	private AuthenticationService authenticationService;
	
	private MessageServiceWebapp messageService;

	@Autowired
	public void setMessageService(MessageServiceWebapp messageService) {
		this.messageService = messageService;
	}

	public WaspTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		this.localizedLabelKey = localizedLabelKey;
		this.targetLink = targetLink;
		this.permission = permission;
	}
	
	@Autowired
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}


	public String getLocalizedLabelKey() {
		return localizedLabelKey;
	}

	public void setLocalizedLabelKey(String localizedLabelKey) {
		this.localizedLabelKey = localizedLabelKey;
	}
	
	/**
	 * Translates localized label key into display text, e.g. 'wasp.my.message' to 'My Message'
	 * @return
	 */
	public String getLocalizedLabel() {
		return messageService.getMessage(localizedLabelKey);
	}

	
	public String getTargetLink() {
		return targetLink;
	}

	public void setTargetLink(String targetLink) {
		this.targetLink = targetLink;
	}

	public Integer getDashboardSortOrder() {
		return dashboardSortOrder;
	}

	public void setDashboardSortOrder(Integer dashboardSortOrder) {
		this.dashboardSortOrder = dashboardSortOrder;
	}
	
	/**
	 * checks that the currently logged in user is authorized to view the link (based on evaluation of the 'permission' attribute in
	 * by Spring Security)
	 * @return
	 */
	public boolean isUserAuthorizedToViewLink(){
		try {
			return authenticationService.hasPermission(permission);
		} catch (IOException e) {
			logger.warn("Recieved unexpected exception attempting to authorize logged in user against a permission string: " + e.getLocalizedMessage());
			return false;
		}
	}
	
	/**
	 * Return boolean value indicating whether or not to display the link (no point if nothing actionable).
	 * @return
	 */
	public abstract boolean isRequirementToShowLink() throws WaspException;
	
	/**
	 * Return boolean indicating whether or not there is a requirement to display the link and whether the logged in user
	 * has permission to view the link.
	 * @return
	 */
	public boolean isLinkToBeShown(){
		try {
			return isUserAuthorizedToViewLink() && isRequirementToShowLink();
		} catch (WaspException e) {
			logger.warn("Unable to determine if there is a requirement to show the link so returning false: " + e.getLocalizedMessage());
		}
		return false;
	}
	
}
