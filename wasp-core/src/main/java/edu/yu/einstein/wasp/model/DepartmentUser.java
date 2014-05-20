
/**
 *
 * DepartmentUser.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUser
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
@Table(name="departmentuser")
public class DepartmentUser extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1273356683061562536L;
	
	/**
	 * setDepartmentUserId(Integer departmentUserId)
	 *
	 * @param departmentUserId
	 *
	 */
	@Deprecated
	public void setDepartmentUserId (Integer departmentUserId) {
		setId(departmentUserId);
	}

	/**
	 * getDepartmentUserId()
	 *
	 * @return departmentUserId
	 *
	 */
	@Deprecated
	public Integer getDepartmentUserId () {
		return getId();
	}




	/** 
	 * departmentId
	 *
	 */
	@Column(name="departmentid")
	protected Integer departmentId;

	/**
	 * setDepartmentId(Integer departmentId)
	 *
	 * @param departmentId
	 *
	 */
	
	public void setDepartmentId (Integer departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * getDepartmentId()
	 *
	 * @return departmentId
	 *
	 */
	public Integer getDepartmentId () {
		return this.departmentId;
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


	/**
	 * department
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="departmentid", insertable=false, updatable=false)
	protected Department department;

	/**
	 * setDepartment (Department department)
	 *
	 * @param department
	 *
	 */
	public void setDepartment (Department department) {
		this.department = department;
		this.departmentId = department.getId();
	}

	/**
	 * getDepartment ()
	 *
	 * @return department
	 *
	 */
	
	public Department getDepartment () {
		return this.department;
	}


}
