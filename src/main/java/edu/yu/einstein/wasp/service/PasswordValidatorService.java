/**
 *
 * PasswordValidatorService.java 
 * @author rdubin 
 *  
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

@Service
public interface PasswordValidatorService  {

  public boolean validatePassword(String s);
}