
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

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

  public ConfirmEmailAuth getConfirmEmailAuthByUserpendingId (final int userpendingId);

  public ConfirmEmailAuth getConfirmEmailAuthByAuthcode (final String authcode);


}

