
/**
 *
 * LabPending.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPending
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

@Entity
@Audited
@Table(name="labpending")
public class LabPending extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7924789000322141025L;
	

	/**
	 * setLabPendingId(Integer labPendingId)
	 *
	 * @param labPendingId
	 *
	 */
	@Deprecated
	public void setLabPendingId (Integer labPendingId) {
		setId(labPendingId);
	}

	/**
	 * getLabPendingId()
	 *
	 * @return labPendingId
	 *
	 */
	@Deprecated
	public Integer getLabPendingId () {
		return getId();
	}




	/** 
	 * departmentId
	 *
	 */
	@Column(name="departmentid")
	@Range(min=1)
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
	 * name
	 *
	 */
	@Column(name="name")
	@NotEmpty
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
	}




	/** 
	 * primaryUserId
	 *
	 */
	@Column(name="primaryuserid")
	@Range(min=1)
	protected Integer primaryUserId;

	/**
	 * setPrimaryUserId(Integer primaryUserId)
	 *
	 * @param primaryUserId
	 *
	 */
	
	public void setPrimaryUserId (Integer primaryUserId) {
		this.primaryUserId = primaryUserId;
	}

	/**
	 * getPrimaryUserId()
	 *
	 * @return primaryUserId
	 *
	 */
	public Integer getPrimaryUserId () {
		return this.primaryUserId;
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
	
	public void setUserpendingId (Integer userPendingId) {
		this.userPendingId = userPendingId;
	}

	/**
	 * getUserpendingId()
	 *
	 * @return userPendingId
	 *
	 */
	public Integer getUserpendingId () {
		return this.userPendingId;
	}




	/** 
	 * status
	 *
	 */
	@Column(name="status", length=10)
	protected String status = "PENDING";

	/**
	 * setStatus(String status)
	 *
	 * @param status
	 *
	 */
	
	public void setStatus (String status) {
		this.status = status;
	}

	/**
	 * getStatus()
	 *
	 * @return status
	 *
	 */
	public String getStatus () {
		return this.status;
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


	/**
	 * user
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="primaryuserid", insertable=false, updatable=false)
	protected User user;

	/**
	 * setUser (User user)
	 *
	 * @param user
	 *
	 */
	public void setUser (User user) {
		this.user = user;
		this.primaryUserId = user.getId();
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


	/** 
	 * labPendingMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="labpendingid", insertable=false, updatable=false)
	protected List<LabPendingMeta> labPendingMeta;


	/** 
	 * getLabPendingMeta()
	 *
	 * @return labPendingMeta
	 *
	 */
	@JsonIgnore
	public List<LabPendingMeta> getLabPendingMeta() {
		return this.labPendingMeta;
	}


	/** 
	 * setLabPendingMeta
	 *
	 * @param labPendingMeta
	 *
	 */
	public void setLabPendingMeta (List<LabPendingMeta> labPendingMeta) {
		this.labPendingMeta = labPendingMeta;
	}



}
