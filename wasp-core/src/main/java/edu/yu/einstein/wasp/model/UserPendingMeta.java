
/**
 *
 * UserPendingMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingMeta
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
@Table(name="userpendingmeta")
public class UserPendingMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7947772869605124208L;
	

	/**
	 * setUserPendingMetaId(Integer userPendingMetaId)
	 *
	 * @param userPendingMetaId
	 *
	 */
	@Deprecated
	public void setUserPendingMetaId (Integer userPendingMetaId) {
		setId(userPendingMetaId);
	}

	/**
	 * getUserPendingMetaId()
	 *
	 * @return userPendingMetaId
	 *
	 */
	@Deprecated
	public Integer getUserPendingMetaId () {
		return getId();
	}




	/** 
	 * userPendingId
	 *
	 */
	@Column(name="userpendingid")
	protected Integer userPendingId;

	/**
	 * setUserpendingId(Integer userPendingId)
	 *
	 * @param userPendingId
	 *
	 */
	
	public void setUserPendingId (Integer userPendingId) {
		this.userPendingId = userPendingId;
	}

	/**
	 * getUserpendingId()
	 *
	 * @return userPendingId
	 *
	 */
	public Integer getUserPendingId () {
		return this.userPendingId;
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
		this.userPendingId = userPending.getId();
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
