
/**
 *
 * ConfirmEmailAuth.java 
 * @author echeng (table2type.pl)
 *  
 * the ConfirmEmailAuth
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="confirmemailauth")
public class ConfirmEmailAuth extends WaspModel {

	/** 
	 * userpendingId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int userpendingId;

	/**
	 * setUserpendingId(int userpendingId)
	 *
	 * @param userpendingId
	 *
	 */
	
	public void setUserpendingId (int userpendingId) {
		this.userpendingId = userpendingId;
	}

	/**
	 * getUserpendingId()
	 *
	 * @return userpendingId
	 *
	 */
	public int getUserpendingId () {
		return this.userpendingId;
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
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected int lastUpdUser;

	/**
	 * setLastUpdUser(int lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (int lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public int getLastUpdUser () {
		return this.lastUpdUser;
	}




	/**
	 * userPending
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="userpendingid", insertable=false, updatable=false)
	protected UserPending userPending;

	/**
	 * setUserPending (UserPending userPending)
	 *
	 * @param userPending
	 *
	 */
	public void setUserPending (UserPending userPending) {
		this.userPending = userPending;
		this.userpendingId = userPending.userPendingId;
	}

	/**
	 * getUserPending ()
	 *
	 * @return userPending
	 *
	 */
	
	public UserPending getUserPending () {
		return this.userPending;
	}


}
