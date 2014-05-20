
/**
 *
 * LabUser.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUser
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
@Table(name="labuser")
public class LabUser extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3166199902005427273L;

	/**
	 * setLabUserId(Integer labUserId)
	 *
	 * @param labUserId
	 *
	 */
	@Deprecated
	public void setLabUserId (Integer labUserId) {
		setId(labUserId);
	}

	/**
	 * getLabUserId()
	 *
	 * @return labUserId
	 *
	 */
	@Deprecated
	public Integer getLabUserId () {
		return getId();
	}




	/** 
	 * labId
	 *
	 */
	@Column(name="labid")
	protected Integer labId;

	/**
	 * setLabId(Integer labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (Integer labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public Integer getLabId () {
		return this.labId;
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
	 * lab
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected Lab lab;

	/**
	 * setLab (Lab lab)
	 *
	 * @param lab
	 *
	 */
	public void setLab (Lab lab) {
		this.lab = lab;
		this.labId = lab.getId();
	}

	/**
	 * getLab ()
	 *
	 * @return lab
	 *
	 */
	
	public Lab getLab () {
		return this.lab;
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


	/**
	 * role
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="roleid", insertable=false, updatable=false)
	protected Role role;

	/**
	 * setRole (Role role)
	 *
	 * @param role
	 *
	 */
	public void setRole (Role role) {
		this.role = role;
		this.roleId = role.getId();
	}

	/**
	 * getRole ()
	 *
	 * @return role
	 *
	 */
	
	public Role getRole () {
		return this.role;
	}


}
