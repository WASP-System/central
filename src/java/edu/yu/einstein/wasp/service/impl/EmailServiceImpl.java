package edu.yu.einstein.wasp.service.impl;

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

	  // @Autowired
	   //private Properties props;
	   
	   public void sendNewPassword(final User user, final String password) {
		   MimeMessagePreparator preparator = new MimeMessagePreparator() {
		         public void prepare(MimeMessage mimeMessage) throws Exception {
		        	 
		            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		            
		            message.setTo(user.getEmail());
		            
		            Properties props = ((JavaMailSenderImpl)mailSender).getJavaMailProperties();
		            
		            message.setFrom(props.getProperty("mail.smtp.from"));
		            Map model = new HashMap();
		            model.put("user", user);
		            model.put("password", password);
		            
		            String lang=user.getLocale().substring(0, 2);
		            
		            String text =
		            	VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
		            			"emails/new_password_"+lang+".vm", "UTF-8", model);
		            
		          
		            String subject = extractSubject(text);
		            String body = extractBody(text);
		            
		            //message.setSubject(subject,"UTF-8");		            
		            //message.setText(body, true);
		            mimeMessage.setSubject(subject,"UTF-8");
		            mimeMessage.setText(body,"UTF-8");
		            
		            
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

