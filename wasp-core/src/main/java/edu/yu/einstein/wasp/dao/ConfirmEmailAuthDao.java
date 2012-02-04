/**
*
* ConfirmEmailAuthDao.java 
* @author echeng (table2type.pl)
*  
* the ConfirmEmailAuth Dao 
*
*
**/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.ConfirmEmailAuth;


public interface ConfirmEmailAuthDao extends WaspDao<ConfirmEmailAuth> {
    
	public ConfirmEmailAuth getConfirmEmailAuthByConfirmEmailAuthId (final int confirmEmailAuthId);

	public ConfirmEmailAuth getConfirmEmailAuthByAuthcode (final String authcode);
	
	public ConfirmEmailAuth getConfirmEmailAuthByUserpendingId (final int userpendingId);
	  
	public ConfirmEmailAuth getConfirmEmailAuthByUserId (final int userId);


}


