package edu.yu.einstein.wasp.taskMapping;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.WebAuthenticationService;
import edu.yu.einstein.wasp.web.WebHyperlink;

/**
 * 
 * @author asmclellan
 *
 */
public abstract class WaspTaskMapping extends WebHyperlink{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String permission;
	
	private Integer dashboardSortOrder;
	
	private WebAuthenticationService webAuthenticationService;

	public WaspTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink);
		this.permission = permission;
	}
	
	@Autowired
	@Override // override to autowire
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	@Autowired
	public void setAuthenticationService(WebAuthenticationService authenticationService) {
		this.webAuthenticationService = authenticationService;
	}
	
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
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
			return webAuthenticationService.hasPermission(permission);
		} catch (IOException e) {
			logger.warn("Recieved unexpected exception attempting to authorize logged in user against a permission string: " + e.getLocalizedMessage());
			return false;
		}
	}
	
	/**
	 * Return boolean value indicating whether or not to display the link (no point if nothing actionable).
	 * @return
	 */
	public abstract boolean isRequirementToShowLink(Object o) throws WaspException;
	
	
	/**
	 * Return boolean indicating whether or not there is a requirement to display the link and whether the logged in user
	 * has permission to view the link.
	 * @return
	 */
	public boolean isLinkToBeShown(Object o){
		try {
			return isUserAuthorizedToViewLink() && isRequirementToShowLink(o);
		} catch (WaspException e) {
			logger.warn("Unable to determine if there is a requirement to show the link so returning false: " + e.getLocalizedMessage());
		}
		return false;
	}
	
	/**
	 * Return boolean indicating whether or not there is a requirement to display the link and whether the logged in user
	 * has permission to view the link.
	 * @return
	 */
	public boolean isLinkToBeShown(){
		try {
			return isUserAuthorizedToViewLink() && isRequirementToShowLink(null);
		} catch (WaspException e) {
			logger.warn("Unable to determine if there is a requirement to show the link so returning false: " + e.getLocalizedMessage());
		}
		return false;
	}
	
}
