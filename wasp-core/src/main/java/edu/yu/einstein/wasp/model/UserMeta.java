
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
	 * setUserMetaId(Integer userMetaId)
	 *
	 * @param userMetaId
	 *
	 */
	@Deprecated
	public void setUserMetaId (Integer userMetaId) {
		setId(userMetaId);
	}

	/**
	 * getUserMetaId()
	 *
	 * @return userMetaId
	 *
	 */
	@Deprecated
	public Integer getUserMetaId () {
		return getId();
	}




	/** 
	 * UserId
	 *
	 */
	@Column(name="userid")
	protected Integer userId;

	/**
	 * setUserId(Integer UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (Integer userId) {
		this.userId = userId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public Integer getUserId () {
		return this.userId;
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
		this.userId = user.getId();
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
