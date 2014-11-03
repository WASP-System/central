
package edu.yu.einstein.wasp.service;

import java.io.File;
import java.io.OutputStream;
import java.util.Set;

import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailPreparationException;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.User;
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
	 * @param comment reason for rejection
	 */
	public void sendPendingLabNotifyRejected(final LabPending labPending, final String comment);
	
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

	/**
	 * Inform User that login name has been changed. Necessary if Admin changes user profile and login is updated.
	 * @param user
	 */
	public void informUserLoginChanged(User user);

	/**
	 * Inform user than admin has created an account for them
	 * @param user
	 * @param authcode
	 */
	void informUserAccountCreatedByAdmin(User user, String authcode);
	
    /**
    * setConfirmEmailAuthDao(ConfirmEmailAuthDao confirmEmailAuthDao)
    *
    * @param confirmEmailAuthDao
    *
    */

	
	public String getNewAuthcodeForUser(final User user);

	public String getNewAuthcodeForUserPending(UserPending userpending);

	/**
	 * Sends an email message to the department administrator to confirm or deny a principal investigator
	 * application from an already existing user that is now trying to create a lab of his/her own.
	 * @param userPending
	 */
	public void sendExistingUserPendingPrincipalConfirmRequest(final LabPending labPending);

	/**
	 * Sends an email to specified recipient stating that a new job has been submitted  
	 * The emailTemplate tailors the email to the recipient  
	 * @param Job job
	 * @param User recipient
	 * @param String emailTemplate (with format like emails/inform_submitter_job_started)
	 */
	public void sendJobStarted(final Job job, User recipient, String emailTemplate);
	
	/**
	 * Sends an email to specified recipient stating that a job has been abandoned  
	 * @param Job job
	 * @param User recipient
	 * @param String emailTemplate (with format emails/inform_job_abandoned)
	 * @param String whoAbandonedJob (weaved into the email text; it's not a name, but it is a role [such as Facility Manger])
	 * @param String Comment (weaved into email text: comment written by the rejector)
	 */
	public void sendJobAbandoned(final Job job, User recipient, String emailTemplate, String whoAbandonedJob, String reasonForAbandoned);

	/**
	 * Sends an email to specified recipient stating that a job has been accepted 
	 * @param Job job
	 * @param User recipient
	 * @param String emailTemplate (with format emails/inform_job_accepted)
	 * @param String whoAbandonedJob (weaved into the email text; it's not a name, but it is a role [such as Facility Manger])
	 * @param String Comment (weaved into email text: comment written by the rejector)
	 */
	public void sendJobAccepted(final Job job, User recipient, String emailTemplate);

	/**
	 * Sends an email to specified recipient stating that a job has been completed   
	 * @param Job job
	 * @param User recipient
	 * @param String emailTemplate (with format like emails/inform_submitter_job_started)
	 */
	public void sendJobCompleted(final Job job, User recipient, String emailTemplate);

	/**
	 * Sends an email to job's PI with newly created (or newly uploaded) quote attached. In addition to lab PI, cc to users in ccEmailRecipient list if it is not null; currently that is passed in with facility manager and the person that actually clicked the web form).
	 *     
	 * @param Job job (for extracting job details and the PI's email address, who is the emailRecipient)
	 * @param Set<User> ccEmailRepicients (can be null; list of users to be cc'ed on email)
	 * @param File fileToAttach (can be null; the quote attachment)
	 * @param String fileName (can be null if fileToAttach is null)
	 * @throws MailPreparationException
	 */
	public void sendQuoteAsAttachmentToPI(final Job job, final Set<User> ccEmailRepicients, final File file, String fileName)throws MailPreparationException;

	/**
	 * Sends email containing a specific job comment (and brief summary of a job) to emailRecipient. In addition send email copy to ccEmailRecipients if its not null.
	 *     
	 * @param Job job (needed for extracting job details)
	 * @param User commentWriter (person that wrote this job comment)
	 * @param String comment (if null or comment.trim() is empty, immediately return without sending email)
	 * @param User emailRecipient (Should NOT be included in ccEmailRecipients or bccEmailRecipients lists)
	 * @param Set<User> ccEmailRepicients (can be null; list of users to be cc'ed on email)
	 * @param Set<User> bccEmailRepicients (can be null; list of users to be cc'ed on email)
	 * @throws MailPreparationException
	 */
	public void sendJobComment(final Job job, final User commentWriter, final String comment, final User emailRecipient, final Set<User> ccEmailRecipients, final Set<User> bccEmailRecipients)throws MailPreparationException;
}

