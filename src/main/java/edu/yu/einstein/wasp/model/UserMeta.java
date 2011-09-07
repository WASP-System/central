
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="usermeta")
public class UserMeta extends MetaBase {

	/** 
	 * userMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int userMetaId;

	/**
	 * setUserMetaId(int userMetaId)
	 *
	 * @param userMetaId
	 *
	 */
	
	public void setUserMetaId (int userMetaId) {
		this.userMetaId = userMetaId;
	}

	/**
	 * getUserMetaId()
	 *
	 * @return userMetaId
	 *
	 */
	public int getUserMetaId () {
		return this.userMetaId;
	}




	/** 
	 * UserId
	 *
	 */
	@Column(name="userid")
	protected int UserId;

	/**
	 * setUserId(int UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (int UserId) {
		this.UserId = UserId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public int getUserId () {
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
