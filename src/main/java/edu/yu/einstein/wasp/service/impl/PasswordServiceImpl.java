package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import edu.yu.einstein.wasp.service.PasswordService;


@Service
public class PasswordServiceImpl implements PasswordService {
	
  public String encodePassword(String s) {
    PasswordEncoder encoder = new ShaPasswordEncoder();
    String hashedPassword = encoder.encodePassword(s, null);

    return hashedPassword;
  }
	
  public boolean validatePassword(String s) {
	  //http://www.the-art-of-web.com/javascript/validate-password/
	  //see orange box on this web page
	  //only letters and numbers, at least one number, at least one letter, and at least 8 characters
	  //I replaced the \w with [0-9a-zA-Z]
	  if (s == null || s.isEmpty()){
		  // defensive: not valid if not set
		  return false;
	  }
	  return s.matches("^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}$"); 	  
  }
  
  public boolean matchPassword(String s1, String s2){
	  if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()){
		  // defensive: must have a value as well as match (do not want to try to match null or empty passwords)
		  return false;
	  }
	  return s1.equals(s2);
  }
}

