
/**
 *
 * Userpasswordauth.java 
 * @author echeng (table2type.pl)
 *  
 * the Userpasswordauth
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="userpasswordauth")
public class Userpasswordauth extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2900128970352856878L;
	/** 
	 * UserId
	 *
	 */
	@Id 
	protected Integer UserId;

	/**
	 * setUserId(Integer UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (Integer UserId) {
		this.UserId = UserId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public Integer getUserId () {
		return this.UserId;
	}




	/** 
	 * authcode
	 *
	 */
	@Column(name="authcode")
	protected String authcode;

	/**
	 * setAuthcode(String authcode)
	 *
	 * @param authcode
	 *
	 */
	
	public void setAuthcode (String authcode) {
		this.authcode = authcode;
	}

	/**
	 * getAuthcode()
	 *
	 * @return authcode
	 *
	 */
	public String getAuthcode () {
		return this.authcode;
	}


	/**
	 * user
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="userid", insertable=false, updatable=false)
	protected User user;

	/**
	 * setUser (User user)
	 *
	 * @param user
	 *
	 */
	public void setUser (User user) {
		this.user = user;
		this.UserId = user.UserId;
	}

	/**
	 * getUser ()
	 *
	 * @return user
	 *
	 */
	
	public User getUser () {
		return this.user;
	}


}
