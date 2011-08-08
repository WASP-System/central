
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

import edu.yu.einstein.wasp.model.User;

@Service
public interface EmailService  {

  void sendNewPassword(User user,String password);

}

