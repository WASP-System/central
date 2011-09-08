
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
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.UserPending;

@Service
public interface EmailService  {

  public void sendNewPassword(User user,String password);

  public void sendPendingLabUser(LabUser labUser);
  
  public void sendPendingUserEmailConfirm(UserPending userPending, String authcode);
  
  public void sendPendingUserValidate(UserPending userPending);

  public void sendForgotPassword(User user, String authcode);

}

