
package edu.yu.einstein.wasp.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

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
	 * Sends an email message to a pending user requesting that they confirm their email address
	 * 
	 * @param userPending
	 * @param authcode
	 */
	public void sendPendingUserEmailConfirm(final UserPending userPending, final String authcode);
	
	
	/**
	 * Sends an email message to a pending user notifying an accepted application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 */
	public void sendPendingUserNotifyAccepted(final UserPending userPending);
	
	/**
	 * Sends an email message to a pending user notifying a rejected application to
	 * join a lab. 
	 * 
	 * @param userPending the pending user
	 */
	public void sendPendingUserNotifyRejected(final UserPending userPending);
	
	/**
	 * Sends an email message to the list of lab managers and principle investigator of the lab 
	 * which the pending user would like to join. 
	 *
	 * @param userPending the pending user
	 */
	public void sendPendingUserPrimaryConfirm(final UserPending userPending) ;
	
	/**
	 * Sends an email message to the list of lab managers and principle investigator of the lab 
	 * which the pending labUser would like to join.  
	 *
	 * @param labUser the pending lab user
	 *
	 */
	public void sendPendingLabUserPrimaryConfirm(final LabUser labUser);


	/**
	 * Sends an email message to anonymous (not logged in) user containing an auth code to permit
	 * entering of a new password.
	 * 
	 * @param user
	 * @param authcode
	 */
	public void sendForgotPassword(User user, String authcode);

}

