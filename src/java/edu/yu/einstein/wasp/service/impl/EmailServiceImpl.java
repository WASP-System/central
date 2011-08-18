package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
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

	// @Autowired
	//private Properties props;
	protected MimeMessage generateMessage(final User user, String template, final Map model, MimeMessage mimeMessage) throws Exception {
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage);


		Properties props = ((JavaMailSenderImpl)mailSender).getJavaMailProperties();
		message.setFrom(props.getProperty("mail.smtp.from"));

		// message.setTo(user.getEmail());
		message.setTo(props.getProperty("mail.smtp.from"));

		String lang=user.getLocale().substring(0, 2);

		String text =
			VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				template+ "_"+lang+".vm", "UTF-8", model);

		String subject = extractSubject(text);
		String body = extractBody(text);

		//message.setSubject(subject,"UTF-8");		            
		//message.setText(body, true);
		mimeMessage.setSubject(subject,"UTF-8");
		mimeMessage.setText(body,"UTF-8");

		return mimeMessage;
	}

	public void sendPendingLabUser(final LabUser labUser) {

		final Lab lab = labUser.getLab(); 
		final User user = labUser.getUser(); 
		final User primaryUser= labUser.getLab().getUser();	
		Role role = roleService.getRoleByRoleName("lm");

		Map labManagerQueryMap = new HashMap();
		labManagerQueryMap.put("labId", labUser.getLabId());
		labManagerQueryMap.put("roleId", role.getRoleId());

		List<User> toList= labUserService.findByMap(labManagerQueryMap);
		toList.add(primaryUser);
		toList.add(user);

		// todo iterate through all the users

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				Map model = new HashMap();
				model.put("user", user);
				model.put("pendinguser", user);
				model.put("lab", lab);
				
				generateMessage(user, "emails/pending_labuser", model, mimeMessage); 
			}
		};
		this.mailSender.send(preparator);
	}

	public void sendForgotPassword(final User user, final String authcode) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				Map model = new HashMap();
				model.put("user", user);
				model.put("authcode", authcode);
				
				generateMessage(user, "emails/forgot_password", model, mimeMessage); 
			}
		};
		this.mailSender.send(preparator);
	}
	   
	public void sendNewPassword(final User user, final String password) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				Map model = new HashMap();
				model.put("user", user);
				model.put("password", password);
				
				generateMessage(user, "emails/new_password", model, mimeMessage); 
			}
		};
		this.mailSender.send(preparator);
	}

	
	private String extractSubject(String text) {
		
		int idx=text.indexOf("\n");
		
		String subject=text.substring(0,idx).trim();
		
		return subject;
		
	}
	
	private String extractBody(String text) {
		
		int idx=text.indexOf("\n");
		
		String body=text.substring(idx).trim();
		
		return body;
		
	}
	
}

