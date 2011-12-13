
package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.UserPending;

/**
 * EmailService interface.
 * 
 * Specifies interface for sending WASP emails
 * 
 */
@Service
public interface EmailService  {
	
	/**
	 * Sends an email message to a user requesting that they confirm their email address
	 * 
	 * @param user
	 * @param authcode
	 */
	public void sendUserEmailConfirm(final User user, final String authcode);

	/**
	 * Sends an email message to a pending user requesting that they confirm their email address
	 * 
	 * @param userPending
	 * @param authcode
	 */
	public void sendPendingUserEmailConfirm(final UserPending userPending, final String authcode);
	
	/**
	 * Sends an email message to a pending PI requesting that they confirm their email address
	 * 
	 * @param userPending
	 * @param authcode
	 */
	public void sendPendingPIEmailConfirm(final UserPending userPending, final String authcode);
	
	
	/**
	 * Sends an email message to a pending user notifying an accepted application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingUserNotifyAccepted(final User user, final Lab lab);
	
	/**
	 * Sends an email message to a pending lab user notifying an accepted application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingLabUserNotifyAccepted(final User user, final Lab lab);
	
	/**
	 * Sends an email message to a pending lab user notifying a rejected application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingLabUserNotifyRejected(final User user, final Lab lab);
	
	/**
	 * Sends an email message to a pending user notifying a rejected application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingUserNotifyRejected(final UserPending userPending, final Lab lab);
	
	/**
	 * Sends an email message to PI notifying an accepted application to create a lab 
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingLabNotifyAccepted(final Lab lab);
	
	/**
	 * Sends an email message to a PI notifying a rejected application to create a lab
	 * 
	 * @param userPending the pending user
	 * @param lab the lab applied for
	 */
	public void sendPendingLabNotifyRejected(final LabPending labPending);
	
	/**
	 * Sends an email message to the list of lab managers and principal investigator of the lab 
	 * which the pending user would like to join. 
	 *
	 * @param userPending the pending user
	 */
	public void sendPendingUserConfirmRequest(final UserPending userPending) ;
	
	/**
	 * Sends an email message to the list of lab managers and principal investigator of the lab 
	 * which the pending labUser would like to join.  
	 *
	 * @param labUser the pending lab user
	 *
	 */
	public void sendPendingLabUserConfirmRequest(final LabUser labUser);
	
	/**
	 * Sends an email message to the department administrator to confirm or deny a principal investigator
	 * application.
	 * @param userPending
	 */
	public void sendPendingPrincipalConfirmRequest(final LabPending labPending);

	/**
	 * Sends an email message to anonymous (not logged in) user containing an auth code to permit
	 * entering of a new password.
	 * 
	 * @param user
	 * @param authcode
	 */
	public void sendRequestNewPassword(User user, String authcode);

}

