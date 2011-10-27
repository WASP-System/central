package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.HashMap;
import java.util.Locale;
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


import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.DepartmentUser;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.MetaHelper;
import edu.yu.einstein.wasp.model.MetaHelper.WaspMetadataException;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.LabUserService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.service.UserService;
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
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserPendingService userPendingService;
	
	@Autowired
	private DepartmentService departmentService;


	
	
	
	/**
	 * {@inheritDoc} 
	 */
	public void sendPendingUserEmailConfirm(final UserPending userPending, final String authcode) {
		sendEmailConfirm(userPending, authcode, "emails/pending_user_request_email_confirm");
	}
	
	/**
	 * {@inheritDoc} 
	 */
	public void sendPendingPIEmailConfirm(final UserPending userPending, final String authcode) {
		sendEmailConfirm(userPending, authcode, "emails/pending_pi_request_email_confirm");
	}
	
	/**
	 * Send confirmation email to pending user ({@link UserPending}) providing an authorization code and using the velocity template specified
	 * @param userPending
	 * @param authcode
	 * @param emailTemplate
	 */
	protected void sendEmailConfirm(final UserPending userPending, final String authcode, final String template){
		User pendinguser = new User();
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
		prepareAndSend(pendinguser, template, model); 
	}
	
	/**
	 * {@inheritDoc}  
	 */
	public void sendPendingUserNotifyAccepted(final User user, final Lab lab){
		Map model = new HashMap();
		model.put("user", user);
		model.put("lab", lab);
		prepareAndSend(user, "emails/pending_user_notify_accepted", model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPendingUserNotifyRejected(final UserPending userPending, final Lab lab){
		User pendinguser = new User();
		pendinguser.setFirstName(userPending.getFirstName());
		pendinguser.setLastName(userPending.getLastName());
		pendinguser.setEmail(userPending.getEmail());
		pendinguser.setLocale(userPending.getLocale());
		Map model = new HashMap();
		model.put("pendinguser", pendinguser);
		model.put("lab", lab);
		prepareAndSend(pendinguser, "emails/pending_user_notify_rejected", model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPendingLabNotifyAccepted(final Lab lab){
		User primaryUser = userService.getUserByUserId(lab.getPrimaryUserId());
		Map model = new HashMap();
		model.put("primaryuser", primaryUser);
		model.put("lab", lab);
		prepareAndSend(primaryUser, "emails/pending_lab_notify_accepted", model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPendingLabNotifyRejected(final LabPending labPending){
		User user = new User();
		if (labPending.getUserpendingId() != null ) {
			// this PI is currently a pending user. 
			UserPending userPending = userPendingService.getUserPendingByUserPendingId(labPending.getUserpendingId());
			user.setFirstName(userPending.getFirstName());
			user.setLastName(userPending.getLastName());
			user.setEmail(userPending.getEmail());
			user.setLocale(userPending.getLocale());
		} else {
			// the referenced PI of this lab exists in the user table already so get their record
			user = userService.getUserByUserId(labPending.getPrimaryUserId());
		}
		Map model = new HashMap();
		model.put("user", user);
		model.put("labpending", labPending);
		prepareAndSend(user, "emails/pending_lab_notify_rejected", model);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws MailPreparationException 
	 */
	public void sendPendingUserConfirmRequest(final UserPending userPending) throws MailPreparationException {
		MetaHelper userPendingMetaHelper = new MetaHelper("userPending", UserPendingMeta.class, Locale.US);
		userPendingMetaHelper.syncWithMaster(userPending.getUserPendingMeta());
		User primaryUser;
		try{
			primaryUser = userService.getUserByLogin(userPendingMetaHelper.getMetaByName("primaryuserid").getV());
		} catch(WaspMetadataException e){
			throw new MailPreparationException("Cannot get principal user for pending user with id '" + Integer.toString(userPending.getUserPendingId()), e); 
		}
		Map model = new HashMap();
		model.put("pendinguser", userPending);

		
		// send email to PI and all lab managers in the requested lab
		Map labManagerQueryMap = new HashMap();
		for (Lab lab : primaryUser.getLab()){
			if (lab.getPrimaryUserId() == primaryUser.getUserId()){
				labManagerQueryMap.put("labId", lab.getLabId());
				model.put("lab", lab);
				break;
			}
			throw new MailPreparationException("Cannot email principal user with id '" + Integer.toString(primaryUser.getUserId()) + "' because this user is not the principal of any labs"); 
		}
		
		// send email to PI
		model.put("primaryuser", primaryUser);
		prepareAndSend(primaryUser, "emails/pending_labuser_request_confirm", model);
		
		// send email to lab managers
		labManagerQueryMap.put("roleId", roleService.getRoleByRoleName("lm").getRoleId());
		for (LabUser labUserLM : (List<LabUser>) labUserService.findByMap(labManagerQueryMap)){
			User labManager = labUserLM.getUser();
			model.put("primaryuser", labManager);
			prepareAndSend(labManager, "emails/pending_labuser_request_confirm", model); 
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendPendingLabUserConfirmRequest(final LabUser labUser) {
		Lab lab = labUser.getLab(); 
		User pendingUser = labUser.getUser(); 
		User primaryUser= userService.getUserByUserId(labUser.getLab().getPrimaryUserId());	
				
		Map model = new HashMap();
		model.put("lab", lab);
		model.put("pendinguser", pendingUser);
		
		// send email to PI
		model.put("primaryuser", primaryUser);
		prepareAndSend(primaryUser, "emails/pending_labuser_request_confirm", model);
		
		// send email to lab managers
		Map labManagerQueryMap = new HashMap();
		labManagerQueryMap.put("labId", labUser.getLabId());
		labManagerQueryMap.put("roleId", roleService.getRoleByRoleName("lm").getRoleId());
		for (LabUser labUserLM : (List<LabUser>) labUserService.findByMap(labManagerQueryMap)){
			User labManager = labUserLM.getUser();
			model.put("primaryuser", labManager);
			prepareAndSend(labManager, "emails/pending_labuser_request_confirm", model); 
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendPendingPrincipalConfirmRequest(final LabPending labPending) {
		User user;
		if (labPending.getUserpendingId() != null){
			UserPending userPending = userPendingService.getUserPendingByUserPendingId(labPending.getUserpendingId());
			user = new User();
			user.setFirstName(userPending.getFirstName());
			user.setLastName(userPending.getLastName());
		} else if (labPending.getPrimaryUserId() != null){
			user = userService.getUserByUserId(labPending.getPrimaryUserId());
		}
		else{
			throw new MailPreparationException("Cannot prepare email as labPending does not have an associated userId or userPendingId");
		}
		
		Department department = departmentService.getDepartmentByDepartmentId(labPending.getDepartmentId());
		Map model = new HashMap();
		model.put("labpending", labPending);
		model.put("user", user);
		model.put("department", department);
		for (DepartmentUser du: department.getDepartmentUser()){
			User administrator = userService.getUserByUserId(du.getUserId());
			model.put("administrator", administrator);
			prepareAndSend(administrator, "emails/pending_pi_request_confirm", model); 
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
	protected void prepareAndSend(final User user, final String template, final Map model){
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
		String completeEmailTextHtml = headerText + body + footerText;
		try{
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			Properties props = ((JavaMailSenderImpl)mailSender).getJavaMailProperties();
		
			message.setFrom(props.getProperty("mail.smtp.from")); //TODO: remove this line and un-comment line below in production code
			// message.setTo(user.getEmail());
			
			message.setTo(props.getProperty("mail.smtp.from"));
			message.setSubject(subject);
			String plainText = completeEmailTextHtml.replaceAll("\\<.*?>","");
			message.setText(plainText, completeEmailTextHtml);
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

