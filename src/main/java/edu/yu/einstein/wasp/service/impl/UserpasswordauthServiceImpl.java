
/**
 *
 * UserpasswordauthService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserpasswordauthService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.UserpasswordauthService;
import edu.yu.einstein.wasp.dao.UserpasswordauthDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Userpasswordauth;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserpasswordauthServiceImpl extends WaspServiceImpl<Userpasswordauth> implements UserpasswordauthService {

  private UserpasswordauthDao userpasswordauthDao;
  @Autowired
  public void setUserpasswordauthDao(UserpasswordauthDao userpasswordauthDao) {
    this.userpasswordauthDao = userpasswordauthDao;
    this.setWaspDao(userpasswordauthDao);
  }
  public UserpasswordauthDao getUserpasswordauthDao() {
    return this.userpasswordauthDao;
  }

  // **

  
  public Userpasswordauth getUserpasswordauthByUserId (final int UserId) {
    return this.getUserpasswordauthDao().getUserpasswordauthByUserId(UserId);
  }

  public Userpasswordauth getUserpasswordauthByAuthcode (final String authcode) {
    return this.getUserpasswordauthDao().getUserpasswordauthByAuthcode(authcode);
  }
  
  public String createAuthCode(int length){
		if (length < 5 || length > 50){
			length = 20; //default 
		}
		String authcode = new String();
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
				authcode = authcode.concat(String.valueOf( (char)ascii ));  
		}
		return authcode;
	}
  
}

