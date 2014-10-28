package edu.yu.einstein.wasp.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.DepartmentUser;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.util.AuthCode;
import edu.yu.einstein.wasp.util.DemoEmail;
import edu.yu.einstein.wasp.util.MetaHelper;


/**
 * EmailDaoImpl Class.
 * 
 * Handles sending emails implementing the {@link EmailDao} interface, based on Velocity Templates and {@link JavaMailSender}
 * 
 */
@Service
@Transactional("entityManager")
public class EmailServiceImpl extends WaspServiceImpl implements EmailService{

	
	static {
		System.setProperty("mail.mime.charset", "utf8");
	}
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private JobService jobService;

	@Autowired
	private UserService userService;

	@Autowired
	private LabUserDao labUserDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserPendingDao userPendingDao;
	
	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private DepartmentUserDao departmentUserDao;

	@Autowired
	private ConfirmEmailAuthDao confirmEmailAuthDao;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private DemoEmail demoEmail;
	
	@Autowired
	private FileService fileService;

	@Value("${wasp.customimage.logo}")
	private String customLogoResource;
	
	@Value("${email.sending.enabled:true}")
	private Boolean isSendingEmailEnabled;
	
	@Value("${email.sending.testmode.enabled:false}")
	private Boolean isSendingEmailInTestModeEnabled;
	
	@Value("${email.smtp.from}")
	private String emailSmtpFrom;
	
	@Value("${email.smtp.bcc}")
	private String emailSmtpBcc;
	
	@Value("${email.smtp.cc}")
	private String emailSmtpCc;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());


	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void sendUserEmailConfirm(final User user, final String authcode) {
		sendEmailConfirm(user, authcode, "emails/user_request_email_confirm");
	}
	
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void sendPendingUserEmailConfirm(final UserPending userPending, final String authcode) {
		sendEmailConfirm(userPending, authcode, "emails/pending_user_request_email_confirm");
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void sendPendingPIEmailConfirm(final UserPending userPending, final String authcode) {
		sendEmailConfirm(userPending, authcode, "emails/pending_pi_request_email_confirm");
	}
	
	/**
	 * Send confirmation email to user ({@link User}) providing an authorization code and using the velocity template specified
	 * @param user
	 * @param authcode
	 * @param emailTemplate
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void sendEmailConfirm(final User user, final String authcode, final String template){
		String urlEncodedEmail = "";
		try{
			urlEncodedEmail = URLEncoder.encode(user.getEmail(), "UTF-8");
		} catch(UnsupportedEncodingException e){
			throw new MailPreparationException(e);
		}
		Map model = new HashMap();
		model.put("user", user);
		model.put("authcode", authcode);
		model.put("urlencodedemail", urlEncodedEmail);
		prepareAndSend(user, template, model); 
	}
	
	/**
	 * Send confirmation email to pending user ({@link UserPending}) providing an authorization code and using the velocity template specified
	 * @param userPending
	 * @param authcode
	 * @param emailTemplate
	 */
	protected void sendEmailConfirm(final UserPending userPending, String authcode, String template){
		User user = new User();
		user.setFirstName(userPending.getFirstName());
		user.setLastName(userPending.getLastName());
		user.setEmail(userPending.getEmail());
		user.setLocale(userPending.getLocale());
		sendEmailConfirm(user, authcode, template);
	}
	
	/**
	 * {@inheritDoc}  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendPendingUserNotifyAccepted(final User user, final Lab lab){
		Map model = new HashMap();
		model.put("user", user);
		model.put("lab", lab);
		prepareAndSend(user, "emails/pending_user_notify_accepted", model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
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
	
	@Override
	public void sendPendingLabUserNotifyAccepted(User user, Lab lab) {
		sendPendingUserNotifyAccepted(user, lab);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendPendingLabUserNotifyRejected(User user, Lab lab) {
		Map model = new HashMap();
		model.put("pendinguser", user);
		model.put("lab", lab);
		prepareAndSend(user, "emails/pending_user_notify_rejected", model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendPendingLabNotifyAccepted(final Lab lab){
		User primaryUser = userDao.getUserByUserId(lab.getPrimaryUserId());
		Map model = new HashMap();
		model.put("primaryuser", primaryUser);
		model.put("lab", lab);
		prepareAndSend(primaryUser, "emails/pending_lab_notify_accepted", model);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws MailPreparationException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendPendingLabNotifyRejected(final LabPending labPending, final String comment) throws MailPreparationException{
		User user = new User();
		if (labPending.getUserpendingId() != null ) {
			// this PI is currently a pending user. 
			UserPending userPending = userPendingDao.getUserPendingByUserPendingId(labPending.getUserpendingId());
			user.setFirstName(userPending.getFirstName());
			user.setLastName(userPending.getLastName());
			user.setEmail(userPending.getEmail());
			user.setLocale(userPending.getLocale());
		} else if (labPending.getPrimaryUserId() != null ){
			// the referenced PI of this lab exists in the user table already so get their record
			user = userDao.getUserByUserId(labPending.getPrimaryUserId());
		}
		else{
			// shouldn't get here 
			throw new MailPreparationException("No user referenced to whom email should be sent for labPending with id '" + labPending.getId() + "'");
		}
		Map model = new HashMap();
		model.put("user", user);
		model.put("labpending", labPending);
		model.put("reasonForRejection", comment.trim());
		prepareAndSend(user, "emails/pending_lab_notify_rejected", model);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws MailPreparationException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendPendingUserConfirmRequest(final UserPending userPending) throws MailPreparationException {
		MetaHelper userPendingMetaHelper = new MetaHelper("userPending", UserPendingMeta.class, Locale.US);
		userPendingMetaHelper.syncWithMaster(userPending.getUserPendingMeta());
		User primaryUser;
		try{
			primaryUser = userDao.getUserByLogin(userPendingMetaHelper.getMetaByName("primaryuserid").getV());
		} catch(MetadataException e){
			throw new MailPreparationException("Cannot get principal user for pending user with id '" + Integer.toString(userPending.getId()), e); 
		}
		Map model = new HashMap();
		model.put("pendinguser", userPending);

		
		// send email to PI and all lab managers in the requested lab
		Map labManagerQueryMap = new HashMap();
		for (Lab lab : primaryUser.getLab()){
			if (lab.getPrimaryUserId().intValue() == primaryUser.getId().intValue()){
				labManagerQueryMap.put("labId", lab.getId());
				model.put("lab", lab);
				break;
			}
			throw new MailPreparationException("Cannot email principal user with id '" + Integer.toString(primaryUser.getId()) + "' because this user is not the principal of any labs"); 
		}
		
		// send email to PI
		model.put("primaryuser", primaryUser);
		prepareAndSend(primaryUser, "emails/pending_labuser_request_confirm", model);
		
		// send email to lab managers
		labManagerQueryMap.put("roleId", roleDao.getRoleByRoleName("lm").getId());
		for (LabUser labUserLM : labUserDao.findByMap(labManagerQueryMap)){
			User labManager = labUserLM.getUser();
			model.put("primaryuser", labManager);
			prepareAndSend(labManager, "emails/pending_labuser_request_confirm", model); 
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendPendingLabUserConfirmRequest(final LabUser labUser) {
		Lab lab = labUser.getLab(); 
		User pendingUser = labUser.getUser(); 
		User primaryUser= userDao.getUserByUserId(labUser.getLab().getPrimaryUserId());	
				
		Map model = new HashMap();
		model.put("lab", lab);
		model.put("pendinguser", pendingUser);
		
		// send email to PI
		model.put("primaryuser", primaryUser);
		prepareAndSend(primaryUser, "emails/pending_labuser_request_confirm", model);
		
		// send email to lab managers
		Map labManagerQueryMap = new HashMap();
		labManagerQueryMap.put("labId", labUser.getLabId());
		labManagerQueryMap.put("roleId", roleDao.getRoleByRoleName("lm").getId());
		for (LabUser labUserLM : labUserDao.findByMap(labManagerQueryMap)){
			User labManager = labUserLM.getUser();
			model.put("primaryuser", labManager);
			prepareAndSend(labManager, "emails/pending_labuser_request_confirm", model); 
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendPendingPrincipalConfirmRequest(final LabPending labPending) {
		User user;
		if (labPending.getUserpendingId() != null){
			UserPending userPending = userPendingDao.getUserPendingByUserPendingId(labPending.getUserpendingId());
			user = new User();
			user.setFirstName(userPending.getFirstName());
			user.setLastName(userPending.getLastName());
		} else if (labPending.getPrimaryUserId() != null){
			user = userDao.getUserByUserId(labPending.getPrimaryUserId());
		}
		else{
			throw new MailPreparationException("Cannot prepare email as labPending does not have an associated UserId or userPendingId");
		}
		
		Department department = departmentDao.getDepartmentByDepartmentId(labPending.getDepartmentId());
		Map model = new HashMap();
		model.put("labpending", labPending);
		model.put("user", user);
		model.put("department", department);
		for (DepartmentUser du: department.getDepartmentUser()){
			User administrator = userDao.getUserByUserId(du.getUserId());
			model.put("administrator", administrator);
			prepareAndSend(administrator, "emails/pending_pi_request_confirm", model); 
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendRequestNewPassword(final User user, final String authcode) {
		Map model = new HashMap();
		model.put("user", user);
		model.put("authcode", authcode);
		prepareAndSend(user, "emails/request_new_password", model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void informUserLoginChanged(User user) {
		Map model = new HashMap();
		model.put("user", user);
		prepareAndSend(user, "emails/inform_login_changed", model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void informUserAccountCreatedByAdmin(final User user, final String authcode) {
		if (authenticationService.isAuthenticationSetExternal())
			sendEmailConfirm(user, authcode, "emails/user_created_by_admin_ext");
		else
			sendEmailConfirm(user, authcode, "emails/user_created_by_admin_int");
	}
	
	/**
	 * Prepares a {@link MimeMessage} for the recipient {@link User} based on referenced Velocity template and associated model data then sends
	 * the message using {@link JavaMailSender}
	 * 
	 * @param user recipient user
	 * @param template velocityEngine .vm template file prefix (localisation and extension added within this method)
	 * @param model a Map object containing model data referenced within velocityEngine template
	 */
	@SuppressWarnings("rawtypes")
	protected void prepareAndSend(final User user, final String template, final Map model){
		if (!isSendingEmailEnabled){
			logger.info("Not sending email to user " + user.getLastName() + ", " + user.getFirstName() + 
					" with template '" + template  + "' and parameters: " + model.toString() + " because mail-sending is not enabled in configuration");
			return;
		}
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MailPreparationException {
				generateMessage(user, template, model, mimeMessage); 
			}
		};
		logger.debug("Sending email to user " + user.getLastName() + ", " + user.getFirstName() + 
					" with template '" + template  + "' and parameters: " + model.toString());
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void generateMessage(final User user, final String template, final Map model, MimeMessage mimeMessage) throws MailPreparationException {
		model.put("servletUrl", servletPath);
		model.put("customLogoResource", customLogoResource);
		String lang=user.getLocale().substring(0, 2);
		
		String headerVm = "emails/header_"+lang+".vm";
		String footerVm = "emails/footer_"+lang+".vm";
		String mainTextVm = template+ "_"+lang+".vm";

		String headerText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,	headerVm, "UTF-8", model);
		String footerText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,	footerVm, "UTF-8", model);
		String mainText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, mainTextVm, "UTF-8", model);

		String subject = extractSubject(mainText);
		String body = extractBody(mainText);
		String completeEmailTextHtml = headerText + body + footerText;
		
		final String EMAIL_RE = "(\\w+\\.?)+@(\\w+\\.?)+\\.(\\w{2,8})";
		
		Matcher m = Pattern.compile("([^<>]*?)\\s*<?(" + EMAIL_RE + ")>?").matcher(emailSmtpFrom);
		if (!m.matches() || m.groupCount() < 2 || m.group(2).isEmpty()){
			throw new MailPreparationException("'From' Email address property cannot be parsed. Maybe email is not of a suitable format: 'Foo Bar <foo@bar.com>' or 'foo@bar.com'");
		}
		String sendFromPerson = m.group(1);
		String sendFromEmail = m.group(2);
		
		String sendToEmail = user.getEmail();
		if (isSendingEmailInTestModeEnabled)
			sendToEmail = sendFromEmail;
		if (isInDemoMode)
			sendToEmail = demoEmail.getDemoEmail();
		if (!Pattern.matches(EMAIL_RE, sendToEmail)){
			throw new MailPreparationException("'To' Email address is not of a suitable format");
		}
		if (sendToEmail.isEmpty()){
			if (isInDemoMode)
				throw new MailPreparationException("Email address is not set in the cookie");
			else
				throw new MailPreparationException("Email address is empty");
		}
		try{
			logger.trace("Message prepared with settings: sendFromPerson='" + sendFromPerson + "', sendFromEmail='" + sendFromEmail + "', sendToEmail='" + sendToEmail + "'");
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			if (sendFromPerson.isEmpty())
				message.setFrom(sendFromEmail); 
			else
				message.setFrom(sendFromEmail, sendFromPerson); 
			message.setTo(sendToEmail);
			if (emailSmtpBcc != null && !emailSmtpBcc.isEmpty())
				for (String bcc : emailSmtpBcc.split("[,;:\\s]+"))
					message.addBcc(bcc.trim());
			if (emailSmtpCc != null && !emailSmtpCc.isEmpty())
				for (String cc : emailSmtpCc.split("[,;:\\s]+"))
					message.addBcc(cc.trim());
			message.setSubject(subject);
			String plainText = completeEmailTextHtml.replaceAll("\\<.*?>","");
			message.setText(plainText, completeEmailTextHtml);
		} catch(MessagingException | UnsupportedEncodingException e) {
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


	@Override
	public String getNewAuthcodeForUser(User user) {
		String authcode = AuthCode.create(20);
		ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthDao.getConfirmEmailAuthByUserId(user.getId());
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserId(user.getId());
		confirmEmailAuthDao.save(confirmEmailAuth);
		return authcode;
	}
	
	@Override
	public String getNewAuthcodeForUserPending(UserPending userpending) {
		String authcode = AuthCode.create(20);
		ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthDao.getConfirmEmailAuthByUserpendingId(userpending.getId());
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserpendingId(userpending.getId());
		confirmEmailAuthDao.save(confirmEmailAuth);
		return authcode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendExistingUserPendingPrincipalConfirmRequest(final LabPending labPending) {
		User user;
		if (labPending.getUserpendingId() != null){
			UserPending userPending = userPendingDao.getUserPendingByUserPendingId(labPending.getUserpendingId());
			user = new User();
			user.setFirstName(userPending.getFirstName());
			user.setLastName(userPending.getLastName());
		} else if (labPending.getPrimaryUserId() != null){
			user = userDao.getUserByUserId(labPending.getPrimaryUserId());
		}
		else{
			throw new MailPreparationException("Cannot prepare email as labPending does not have an associated UserId or userPendingId");
		}
		
		Department department = departmentDao.getDepartmentByDepartmentId(labPending.getDepartmentId());
		Map model = new HashMap();
		model.put("labpending", labPending);
		model.put("user", user);
		model.put("department", department);
		for (DepartmentUser du: department.getDepartmentUser()){
			User administrator = userDao.getUserByUserId(du.getUserId());
			model.put("administrator", administrator);
			prepareAndSend(administrator, "emails/existing_user_pending_pi_request_confirm", model); 
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendJobStarted(final Job job, User recipient, String emailTemplate){
		Map model = getJobSummaryMapForEmailDisplay(job);
		model.put("addressedTo", recipient);
		prepareAndSend(recipient, emailTemplate, model);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendJobAbandoned(final Job job, User recipient, String emailTemplate, String whoAbandonedJob, String reasonForAbandoned){
		Map model = getJobSummaryMapForEmailDisplay(job);
		model.put("addressedTo", recipient);
		model.put("whoAbandonedJob", whoAbandonedJob);
		model.put("reasonForAbandoned", reasonForAbandoned);
		prepareAndSend(recipient, emailTemplate, model);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendJobAccepted(final Job job, User recipient, String emailTemplate){
		Map model = getJobSummaryMapForEmailDisplay(job);
		model.put("addressedTo", recipient);
		prepareAndSend(recipient, emailTemplate, model);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendJobCompleted(final Job job, User recipient, String emailTemplate){
		Map model = getJobSummaryMapForEmailDisplay(job);
		model.put("addressedTo", recipient);
		prepareAndSend(recipient, emailTemplate, model);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getJobSummaryMapForEmailDisplay(final Job job){
		
		Map model = new HashMap();
		
		model.put("job", job);
		
		User jobSubmitter = job.getUser();
		model.put("jobSubmitter", jobSubmitter);
		
		Lab lab = job.getLab();
		User pi = lab.getUser();
		model.put("pi", pi);
		
		String machine = null;
		String readType = null;
		String readLength = null;
		/*
		 Do NOT go through using the key for extraJobDetailsMap, as it is the value of things like "jobdetail_for_import.Read_Length.label" which can easily change
		LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
		for(String key : extraJobDetailsMap.keySet()){
			if(key.contains("machine")){
				machine = new String(extraJobDetailsMap.get(key));
			}
			else if(key.contains("readType")){
				readType = new String(extraJobDetailsMap.get(key));
			}
			else if(key.contains("readLength")){
				readLength = new String(extraJobDetailsMap.get(key));
			}
		}
		*/
		List<JobResourcecategory> jobResourceCategoryList = job.getJobResourcecategory();
		for(JobResourcecategory jrc : jobResourceCategoryList){
			if(jrc.getResourceCategory().getResourceType().getIName().equals("mps")){
				  machine = jrc.getResourceCategory().getName();
				  break;
			}
		}
		for(JobMeta jm : job.getJobMeta()){
			if(jm.getK().toLowerCase().contains("readlength")){
				readLength = new String(jm.getV());
			}
			if(jm.getK().toLowerCase().contains("readtype")){
				readType = new String(jm.getV());
			}
			if(readType!=null && readLength!=null){
				break;
			}
		}
		if(machine==null){machine = new String("");}
		if(readType==null){readType = new String("");}
		if(readLength==null){readLength = new String("");}
		model.put("machine", machine);
		model.put("readType", readType);
		model.put("readLength", readLength);
		
		List<Sample> submittedSampleList = new ArrayList<Sample>();
		for(Sample s : job.getSample()){
			if(s.getParentId()==null){
				submittedSampleList.add(s);
			}
		}
		model.put("submittedSampleList", submittedSampleList);

		return model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override	
	public void sendQuoteAsAttachmentToPI(final Job job, final Set<User> ccEmailRepicients, final File fileToAttach, String fileName)throws MailPreparationException{
		User pI = job.getLab().getUser();
		Map model = getJobSummaryMapForEmailDisplay(job);
		model.put("addressedTo", pI);//used in body of email
		prepareAndSendEmailWithOrWithoutAttachment(pI, ccEmailRepicients, null, fileToAttach, fileName, "emails/send_quote_as_attachment_to_pi", model);
	}
		
	/**
	 * Prepares a {@link MimeMessage} for the recipient {@link User} based on referenced Velocity template and associated model data then sends
	 * the message using {@link JavaMailSender}. Can handle adding cc and bcc as needed; can handle attaching a file to the email as needed.
	 * 
	 * @param User emailRecipient user
	 * @param Set<User> ccEmailRepicients (can be null)
	 * @param Set<User> bccEmailRepicients (can be null)
	 * @param File fileToAttach (can be null)
	 * @param String fileName (can be null)
	 * @param String template velocityEngine .vm template file prefix (localisation and extension added within this method)
	 * @param Map model a Map object containing model data referenced within velocityEngine template
	 */
	@SuppressWarnings("rawtypes")
	protected void prepareAndSendEmailWithOrWithoutAttachment(final User emailRecipient, final Set<User> ccEmailRepicients, final Set<User> bccEmailRepicients, final File fileToAttach, final String fileName, final String template, final Map model) throws MailPreparationException{
		if (!isSendingEmailEnabled){
			logger.info("Not sending email to user " + emailRecipient.getLastName() + ", " + emailRecipient.getFirstName() + 
					" with template '" + template  + "' and parameters: " + model.toString() + " because mail-sending is not enabled in configuration");
			return;
		}
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MailPreparationException {
				generateMessageForEmailWithOrWithoutAttachment(emailRecipient, ccEmailRepicients, bccEmailRepicients, fileToAttach, fileName, template, model, mimeMessage); 
			}
		};
		logger.debug("Sending email to user " + emailRecipient.getLastName() + ", " + emailRecipient.getFirstName() + 
					" with template '" + template  + "' and parameters: " + model.toString());
		this.mailSender.send(preparator);
	}
	
	/**
	 * Prepares a {@link MimeMessage} message to send to user based on velocity engine template and model map supplied.
	 * Selects template language based on user locale. Can handle adding cc and bcc as needed; can handle attaching a file to the email as needed.
	 * 
	 * @param User emailRecipient {@link User}
	 * @param Set<User> ccEmailRepicients (can be null)
	 * @param Set<User> bccEmailRepicients  (can be null)
	 * @param File fileToAttach (can be null, along with null fileName)
	 * @param String fileName (can be null, along with null fileToAttach)
	 * @param String template velocityEngine .vm template file prefix (localisation and extension added within this method)
	 * @param Map model a {@link Map} object containing model data referenced within velocityEngine template
	 * @param MimeMessage mimeMessage MIME style email message
	 * @throws MailPreparationException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void generateMessageForEmailWithOrWithoutAttachment(final User emailRecipient, final Set<User> ccEmailRepicients, final Set<User> bccEmailRepicients, final File fileToAttach, final String fileName, final String template, final Map model, MimeMessage mimeMessage) throws MailPreparationException {
		model.put("servletUrl", servletPath);
		model.put("customLogoResource", customLogoResource);
		String lang=emailRecipient.getLocale().substring(0, 2);
		
		String headerVm = "emails/header_"+lang+".vm";
		String footerVm = "emails/footer_"+lang+".vm";
		String mainTextVm = template+ "_"+lang+".vm";

		String headerText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,	headerVm, "UTF-8", model);
		String footerText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,	footerVm, "UTF-8", model);
		String mainText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, mainTextVm, "UTF-8", model);

		String subject = extractSubject(mainText);
		String body = extractBody(mainText);
		String completeEmailTextHtml = headerText + body + footerText;
		
		final String EMAIL_RE = "(\\w+\\.?)+@(\\w+\\.?)+\\.(\\w{2,8})";
		
		Matcher m = Pattern.compile("([^<>]*?)\\s*<?(" + EMAIL_RE + ")>?").matcher(emailSmtpFrom);
		if (!m.matches() || m.groupCount() < 2 || m.group(2).isEmpty()){
			throw new MailPreparationException("'From' Email address property cannot be parsed. Maybe email is not of a suitable format: 'Foo Bar <foo@bar.com>' or 'foo@bar.com'");
		}
		String sendFromPerson = m.group(1);
		String sendFromEmail = m.group(2);
		
		String sendToEmail = emailRecipient.getEmail();
		if (isSendingEmailInTestModeEnabled)
			sendToEmail = sendFromEmail;
		if (isInDemoMode)
			sendToEmail = demoEmail.getDemoEmail();
		if (!Pattern.matches(EMAIL_RE, sendToEmail)){
			throw new MailPreparationException("'To' Email address is not of a suitable format");
		}
		if (sendToEmail.isEmpty()){
			if (isInDemoMode)
				throw new MailPreparationException("Email address is not set in the cookie");
			else
				throw new MailPreparationException("Email address is empty");
		}
		try{
			logger.trace("Message prepared with settings: sendFromPerson='" + sendFromPerson + "', sendFromEmail='" + sendFromEmail + "', sendToEmail='" + sendToEmail + "'");
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			if (sendFromPerson.isEmpty())
				message.setFrom(sendFromEmail); 
			else
				message.setFrom(sendFromEmail, sendFromPerson); 
			message.setTo(sendToEmail);
			if (emailSmtpBcc != null && !emailSmtpBcc.isEmpty())
				for (String bcc : emailSmtpBcc.split("[,;:\\s]+"))
					message.addBcc(bcc.trim());
			if (emailSmtpCc != null && !emailSmtpCc.isEmpty())
				for (String cc : emailSmtpCc.split("[,;:\\s]+"))
					message.addBcc(cc.trim());			
			if(ccEmailRepicients != null && !ccEmailRepicients.isEmpty()){
				for(User ccUser : ccEmailRepicients){
					if(!ccUser.getEmail().trim().isEmpty()){
						message.addCc(ccUser.getEmail().trim());
					}
				}
			}
			if(bccEmailRepicients != null && !bccEmailRepicients.isEmpty()){
				for(User bccUser : bccEmailRepicients){
					if(!bccUser.getEmail().trim().isEmpty()){
						message.addCc(bccUser.getEmail().trim());
					}
				}
			}			
			message.setSubject(subject);
			String plainText = completeEmailTextHtml.replaceAll("\\<.*?>","");
			message.setText(plainText, completeEmailTextHtml);
			if(fileToAttach!=null && fileName!=null && !fileName.isEmpty()){
				message.addAttachment(fileName, fileToAttach);
			}
		} catch(MessagingException | UnsupportedEncodingException e) {
			throw new MailPreparationException("problem generating MimeMessage from Velocity template", e); 
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override	
	public void sendJobComment(final Job job, final User commentWriter, final String comment, final User emailRecipient, final Set<User> ccEmailRecipients, final Set<User> bccEmailRecipients)throws MailPreparationException{
		if(comment == null || comment.trim().isEmpty()){
			return;
		}
		Map model = getJobSummaryMapForEmailDisplay(job);
		model.put("commentWriter", commentWriter);
		model.put("comment", comment);
		prepareAndSendEmailWithOrWithoutAttachment(emailRecipient, ccEmailRecipients, null, null, null, "emails/send_job_comment", model);
	}

}

