
/**
*
* ConfirmEmailAuthDaoImpl.java 
* @author echeng (table2type.pl)
*  
* the ConfirmEmailAuth Dao Impl
*
*
**/

package edu.yu.einstein.wasp.dao.impl;



import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ConfirmEmailAuth;


@Transactional
@Repository
public class ConfirmEmailAuthDaoImpl extends WaspDaoImpl<ConfirmEmailAuth> implements edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao {
    
    /**
    * ConfirmEmailAuthDaoImpl() Constructor
    *
    *
    */
    public ConfirmEmailAuthDaoImpl() {
    	super();
    	this.entityClass = ConfirmEmailAuth.class;
	}
	    
	    
	/**
	* getConfirmEmailAuthByConfirmEmailAuthId(final int confirmEmailAuthId)
	*
	* @param final int confirmEmailAuthId
	*
	* @return confirmEmailAuth
	*/
	
	@Override
	@Transactional
	public ConfirmEmailAuth getConfirmEmailAuthByConfirmEmailAuthId (final int confirmEmailAuthId) {
	    HashMap<String, Integer> m = new HashMap<String, Integer>();
	    		m.put("id", confirmEmailAuthId);
	
	    List<ConfirmEmailAuth> results = this.findByMap(m);
	    
	    if (results.size() == 0) {
	    	ConfirmEmailAuth rt = new ConfirmEmailAuth();
	    	return rt;
	    }
	    return results.get(0);
	}
	
	
	
	/**
	* getConfirmEmailAuthByAuthcode(final String authcode)
	*
	* @param final String authcode
	*
	* @return confirmEmailAuth
	*/
	
	@Override
	@Transactional
	public ConfirmEmailAuth getConfirmEmailAuthByAuthcode (final String authcode) {
	    HashMap<String, String> m = new HashMap<String, String>();
	    		m.put("authcode", authcode);
	
	    List<ConfirmEmailAuth> results = this.findByMap(m);
	    
	    if (results.size() == 0) {
	    	ConfirmEmailAuth rt = new ConfirmEmailAuth();
	    	return rt;
	    }
	    return results.get(0);
	}
	
	
	
	@Override
	public ConfirmEmailAuth getConfirmEmailAuthByUserpendingId(int userPendingId) {
	    HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("userPendingId", userPendingId);
	
		List<ConfirmEmailAuth> results = this.findByMap(m);
	
		if (results.size() == 0) {
			ConfirmEmailAuth rt = new ConfirmEmailAuth();
			return rt;
		}
		return results.get(0);
	}
	
	
	
	@Override
	public ConfirmEmailAuth getConfirmEmailAuthByUserId(int userId) {
	    HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("userId", userId);
	
		List<ConfirmEmailAuth> results = this.findByMap(m);
	
		if (results.size() == 0) {
			ConfirmEmailAuth rt = new ConfirmEmailAuth();
			return rt;
		}
		return results.get(0);
	} 

}

