
/**
 *
 * UserMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the UserMeta
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="usermeta")
public class UserMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8388877884886731036L;
	/** 
	 * userMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer userMetaId;

	/**
	 * setUserMetaId(Integer userMetaId)
	 *
	 * @param userMetaId
	 *
	 */
	
	public void setUserMetaId (Integer userMetaId) {
		this.userMetaId = userMetaId;
	}

	/**
	 * getUserMetaId()
	 *
	 * @return userMetaId
	 *
	 */
	public Integer getUserMetaId () {
		return this.userMetaId;
	}




	/** 
	 * UserId
	 *
	 */
	@Column(name="userid")
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
