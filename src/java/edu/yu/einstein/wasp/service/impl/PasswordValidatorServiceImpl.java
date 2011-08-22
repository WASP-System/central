package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.service.PasswordValidatorService;


@Service
public class PasswordValidatorServiceImpl implements PasswordValidatorService {
	
  public boolean validatePassword(String s) {

	  //return s.matches("[a-zA-Z]+[0-9]+");//testing
	  //from http://regexlib.com/REDetails.aspx?regexp_id=31&AspxAutoDetectCookieSupport=1
	  //http://www.the-art-of-web.com/javascript/validate-password/
	  //original:  ^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$   note replaced \d with [0-9]
	  return s.matches("^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{6,20}$"); //6 - 20 characters allowed
	  
  }
	
}