/**
 *
 * FileService.java 
 * @author echeng (table2type.pl)
 *  
 * the FileService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

@Service
public interface PasswordService  {

  public String encodePassword(String s);
  public boolean validatePassword(String s);
  public boolean matchPassword(String s1, String s2);
}
