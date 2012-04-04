
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
@Table(name="userpendingmeta")
public class UserPendingMeta extends MetaBase {

	/** 
	 * userPendingMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer userPendingMetaId;

	/**
	 * setUserPendingMetaId(Integer userPendingMetaId)
	 *
	 * @param userPendingMetaId
	 *
	 */
	
	public void setUserPendingMetaId (Integer userPendingMetaId) {
		this.userPendingMetaId = userPendingMetaId;
	}

	/**
	 * getUserPendingMetaId()
	 *
	 * @return userPendingMetaId
	 *
	 */
	public Integer getUserPendingMetaId () {
		return this.userPendingMetaId;
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
		this.userPendingId = userPending.userPendingId;
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
