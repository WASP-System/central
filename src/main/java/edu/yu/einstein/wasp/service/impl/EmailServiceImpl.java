package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;


import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.LabUserService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.model.UserPending;


/**
 * EmailServiceImpl Class.
 * 
 * Handles sending emails implementing the {@link EmailService} interface, based on Velocity Templates and {@link JavaMailSender}
 * 
 */
@Service
public class EmailServiceImpl implements EmailService {
	
	static {
		System.setProperty("mail.mime.charset", "utf8");
	}
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private LabUserService labUserService;

	@Autowired
	private RoleService roleService;

	
	
	
	/**
	 * {@inheritDoc} 
	 */
	public void sendPendingUserEmailConfirm(final UserPending userPending, final String authcode) {
		final User pendinguser = new User();
		pendinguser.setFirstName(userPending.getFirstName());
		pendinguser.setLastName(userPending.getLastName());
		pendinguser.setEmail(userPending.getEmail());
		pendinguser.setLocale(userPending.getLocale());
		String urlEncodedEmail = "";
		try{
			urlEncodedEmail = URLEncoder.encode(pendinguser.getEmail(), "UTF-8");
		} catch(UnsupportedEncodingException e){
			throw new MailPreparationException(e);
		}
		Map model = new HashMap();
		model.put("pendinguser", pendinguser);
		model.put("authcode", authcode);
		model.put("urlencodedemail", urlEncodedEmail);
		prepareAndSend(pendinguser, "emails/pending_user_request_email_confirm", model); 
	}
	
	/**
	 * {@inheritDoc}  
	 */
	public void sendPendingUserNotifyAccepted(final UserPending userPending){
		 
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPendingUserNotifyRejected(final UserPending userPending){
		 
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPendingUserPrimaryConfirm(final UserPending userPending) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendPendingLabUserPrimaryConfirm(final LabUser labUser) {
		final Lab lab = labUser.getLab(); 
		final User pendingUser = labUser.getUser(); 
		final User primaryUser= labUser.getLab().getUser();	
		Role role = roleService.getRoleByRoleName("lm");

		Map labManagerQueryMap = new HashMap();
		labManagerQueryMap.put("labId", labUser.getLabId());
		labManagerQueryMap.put("roleId", role.getRoleId());

		List<User> toList= labUserService.findByMap(labManagerQueryMap);
		toList.add(primaryUser);

		for (final User toUser : toList){
			Map model = new HashMap();
			model.put("primaryuser", toUser);
			model.put("pendinguser", pendingUser);
			model.put("lab", lab);
			prepareAndSend(toUser, "emails/pending_labuser_request_confirm", model); 
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendForgotPassword(final User user, final String authcode) {
		Map model = new HashMap();
		model.put("user", user);
		model.put("authcode", authcode);
		prepareAndSend(user, "emails/forgot_password", model);
	}
	
	/**
	 * Prepares a {@link MimeMessage} for the recipient {@link User} based on referenced Velocity template and associated model data then sends
	 * the message using {@link JavaMailSender}
	 * 
	 * @param user recipient user
	 * @param template velocityEngine .vm template file prefix (localisation and extension added within this method)
	 * @param model a Map object containing model data referenced within velocityEngine template
	 */
	private void prepareAndSend(final User user, final String template, final Map model){
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws MailPreparationException {
				generateMessage(user, template, model, mimeMessage); 
			}
		};
		this.mailSender.send(preparator);
	}
	
	/**
	 * Prepares a {@link MimeMessage} message to send to user based on velocity engine template and model map supplied.
	 * Selects template language based on user locale.
	 * 
	 * @param user recipient {@link User}
	 * @param template velocityEngine .vm template file prefix (localisation and extension added within this method)
	 * @param model a {@link Map} object containing model data referenced within velocityEngine template
	 * @param mimeMessage MIME style email message
	 * @throws MailPreparationException
	 */
	protected void generateMessage(final User user, final String template, final Map model, MimeMessage mimeMessage) throws MailPreparationException {
		
		String lang=user.getLocale().substring(0, 2);
		String headerText = 
				VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
						"emails/header_"+lang+".vm", "UTF-8", (HashMap) null);
		
		String footerText = 
				VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
						"emails/footer_"+lang+".vm", "UTF-8", (HashMap) null);
		
		String mainText =
			VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				template+ "_"+lang+".vm", "UTF-8", model);

		String subject = extractSubject(mainText);
		String body = extractBody(mainText);
		String completeEmailText = headerText + body + footerText;
		
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		Properties props = ((JavaMailSenderImpl)mailSender).getJavaMailProperties();
		try{
			message.setFrom(props.getProperty("mail.smtp.from")); //TODO: remove this line and un-comment line below in production code
			// message.setTo(user.getEmail());
			message.setTo(props.getProperty("mail.smtp.from"));
			//message.setSubject(subject,"UTF-8");		            
			//message.setText(body, true);
			mimeMessage.setSubject(subject,"UTF-8");
			mimeMessage.setText(completeEmailText,"UTF-8");
		} catch(MessagingException e) {
			throw new MailPreparationException("problem generating MimeMessage from Velocity template", e); 
		}
	}
	   
	/**
	 * Returns the first line from the provided text to use as an email subject line
	 * 
	 * @param text
	 * @return the subject line
	 */
	private String extractSubject(String text) {
		
		int idx=text.indexOf("\n");
		
		String subject=text.substring(0,idx).trim();
		
		return subject;
		
	}
	
	/**
	 * Returns all but the first line from the provided text to use as an email body.
	 * 
	 * @param text
	 * @return email body text
	 */
	private String extractBody(String text) {
		
		int idx=text.indexOf("\n");
		
		String body=text.substring(idx).trim();
		
		return body;
		
	}
	
}

