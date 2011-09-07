
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="userpendingmeta")
public class UserPendingMeta extends MetaBase {

	/** 
	 * userPendingMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int userPendingMetaId;

	/**
	 * setUserPendingMetaId(int userPendingMetaId)
	 *
	 * @param userPendingMetaId
	 *
	 */
	
	public void setUserPendingMetaId (int userPendingMetaId) {
		this.userPendingMetaId = userPendingMetaId;
	}

	/**
	 * getUserPendingMetaId()
	 *
	 * @return userPendingMetaId
	 *
	 */
	public int getUserPendingMetaId () {
		return this.userPendingMetaId;
	}




	/** 
	 * userpendingId
	 *
	 */
	@Column(name="userpendingid")
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
