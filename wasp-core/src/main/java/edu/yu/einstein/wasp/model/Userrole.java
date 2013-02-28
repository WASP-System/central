
/**
 *
 * Userrole.java 
 * @author echeng (table2type.pl)
 *  
 * the Userrole
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;

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
@Table(name="userrole")
public class Userrole extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9165015615365746207L;
	
	/**
	 * setUserroleId(Integer userroleId)
	 *
	 * @param userroleId
	 *
	 */
	@Deprecated
	public void setUserroleId (Integer userroleId) {
		setId(userroleId);
	}

	/**
	 * getUserroleId()
	 *
	 * @return userroleId
	 *
	 */
	@Deprecated
	public Integer getUserroleId () {
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
	 * roleId
	 *
	 */
	@Column(name="roleid")
	protected Integer roleId;

	/**
	 * setRoleId(Integer roleId)
	 *
	 * @param roleId
	 *
	 */
	
	public void setRoleId (Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * getRoleId()
	 *
	 * @return roleId
	 *
	 */
	public Integer getRoleId () {
		return this.roleId;
	}

	/**
	 * role
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="roleid", insertable=false, updatable=false)
	protected WRole role;

	/**
	 * setRole (WRole role)
	 *
	 * @param role
	 *
	 */
	public void setRole (WRole role) {
		this.role = role;
		this.roleId = role.getId();
	}

	/**
	 * getRole ()
	 *
	 * @return role
	 *
	 */
	
	public WRole getRole () {
		return this.role;
	}


	/**
	 * user
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="userid", insertable=false, updatable=false)
	protected WUser user;

	/**
	 * setUser (WUser user)
	 *
	 * @param user
	 *
	 */
	public void setUser (WUser user) {
		this.user = user;
		this.userId = user.getId();
	}

	/**
	 * getUser ()
	 *
	 * @return user
	 *
	 */
	
	public WUser getUser () {
		return this.user;
	}


}
