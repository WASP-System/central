
/**
*
* ConfirmEmailAuthServiceImpl.java 
* @author echeng (table2type.pl)
*  
* the ConfirmEmailAuthService Implmentation 
*
*
**/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.ConfirmEmailAuthService;
import edu.yu.einstein.wasp.util.AuthCode;
import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmEmailAuthServiceImpl extends WaspServiceImpl<ConfirmEmailAuth> implements ConfirmEmailAuthService {
    
    /**
    * confirmEmailAuthDao;
    *
    */
    private ConfirmEmailAuthDao confirmEmailAuthDao;
    
    /**
    * setConfirmEmailAuthDao(ConfirmEmailAuthDao confirmEmailAuthDao)
    *
    * @param confirmEmailAuthDao
    *
    */
    @Autowired
    public void setConfirmEmailAuthDao(ConfirmEmailAuthDao confirmEmailAuthDao) {
        this.confirmEmailAuthDao = confirmEmailAuthDao;
        this.setWaspDao(confirmEmailAuthDao);
    }
    
    /**
    * getConfirmEmailAuthDao();
    *
    * @return confirmEmailAuthDao
    *
    */
    public ConfirmEmailAuthDao getConfirmEmailAuthDao() {
        return this.confirmEmailAuthDao;
    }
    
    public ConfirmEmailAuth getConfirmEmailAuthByConfirmEmailAuthId (final int confirmEmailAuthId) {
        return this.getConfirmEmailAuthDao().getConfirmEmailAuthByConfirmEmailAuthId(confirmEmailAuthId);
    }
    
    public ConfirmEmailAuth getConfirmEmailAuthByAuthcode (final String authcode) {
        return this.getConfirmEmailAuthDao().getConfirmEmailAuthByAuthcode(authcode);
    }

	public ConfirmEmailAuth getConfirmEmailAuthByUserpendingId(int userpendingId) {
		return this.getConfirmEmailAuthDao().getConfirmEmailAuthByUserpendingId(userpendingId);
	}

	public ConfirmEmailAuth getConfirmEmailAuthByUserId(int userId) {
		return this.getConfirmEmailAuthDao().getConfirmEmailAuthByUserId(userId);
	}

	public String getNewAuthcodeForUser(User user) {
		String authcode = AuthCode.create(20);
		ConfirmEmailAuth confirmEmailAuth = this.getConfirmEmailAuthByUserId(user.getUserId());
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserId(user.getUserId());
		this.save(confirmEmailAuth);
		return authcode;
	}
	
	public String getNewAuthcodeForUserPending(UserPending userpending) {
		String authcode = AuthCode.create(20);
		ConfirmEmailAuth confirmEmailAuth = this.getConfirmEmailAuthByUserpendingId(userpending.getUserPendingId());
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserpendingId(userpending.getUserPendingId());
		this.save(confirmEmailAuth);
		return authcode;
	}
        
}

