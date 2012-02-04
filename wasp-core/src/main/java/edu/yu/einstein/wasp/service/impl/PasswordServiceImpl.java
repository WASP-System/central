package edu.yu.einstein.wasp.service.impl;

import java.util.Random;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.service.PasswordService;


@Service
public class PasswordServiceImpl implements PasswordService {
	
  @Override
public String encodePassword(String s) {
    PasswordEncoder encoder = new ShaPasswordEncoder();
    String hashedPassword = encoder.encodePassword(s, null);

    return hashedPassword;
  }
	
  @Override
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
  
  @Override
public boolean matchPassword(String s1, String s2){
	  if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()){
		  // defensive: must have a value as well as match (do not want to try to match null or empty passwords)
		  return false;
	  }
	  return s1.equals(s2);
  }
  
  @Override
public String getRandomPassword(int length){
	  if (length < 5 || length > 50){
			length = 10; //default 
		}
		String password = new String();
		Random random = new Random();
		for (int i=0; i < length; i++){
			int ascii = 0;
				switch(random.nextInt(3)){
		  		case 0:
		  			ascii = 48 + random.nextInt(10); // 0-9
		  		break;
		  		case 1:
		  			ascii = 65 + random.nextInt(26); // A-Z 
		  		break;
		  		case 2:
		  			ascii = 97 + random.nextInt(26); // a-z
		  		break;	
			}
				password = password.concat(String.valueOf( (char)ascii ));  
		}
		return password;
  }
  
}

