/**
 *
 * AuthCodeService.java 
 * @author asmclellan 
 *  
 * the AuthCodeService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthCodeService {

  public String createAuthCode(int length);

}

