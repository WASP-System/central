
/**
*
* ConfirmEmailAuthService.java 
* @author echeng (table2type.pl)
*  
* the ConfirmEmailAuthService
*
*
**/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;

@Service
public interface ConfirmEmailAuthService extends WaspService<ConfirmEmailAuth> {
    
    /**
    * setConfirmEmailAuthDao(ConfirmEmailAuthDao confirmEmailAuthDao)
    *
    * @param confirmEmailAuthDao
    *
    */
    public void setConfirmEmailAuthDao(ConfirmEmailAuthDao confirmEmailAuthDao);
    
    /**
    * getConfirmEmailAuthDao();
    *
    * @return confirmEmailAuthDao
    *
    */
    public ConfirmEmailAuthDao getConfirmEmailAuthDao();
    
    public ConfirmEmailAuth getConfirmEmailAuthByConfirmEmailAuthId (final int confirmEmailAuthId);

    public ConfirmEmailAuth getConfirmEmailAuthByAuthcode (final String authcode);
    
    public ConfirmEmailAuth getConfirmEmailAuthByUserpendingId (final int userpendingId);
	  
	public ConfirmEmailAuth getConfirmEmailAuthByUserId (final int userId);
	
	public String getNewAuthcodeForUser(final User user);

	public String getNewAuthcodeForUserPending(UserPending userpending);


}

