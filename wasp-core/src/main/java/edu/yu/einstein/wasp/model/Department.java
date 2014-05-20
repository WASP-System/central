
/**
 *
 * Department.java 
 * @author echeng (table2type.pl)
 *  
 * the Department
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="department")
public class Department extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -268564748121397442L;

	/**
	 * setDepartmentId(Integer departmentId)
	 *
	 * @param departmentId
	 *
	 */
	@Deprecated
	public void setDepartmentId (Integer departmentId) {
		setId(departmentId);
	}

	/**
	 * getDepartmentId()
	 *
	 * @return departmentId
	 *
	 */
	@Deprecated
	public Integer getDepartmentId () {
		return getId();
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
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
	 * isInternal
	 *
	 */
	@Column(name="isinternal")
	protected Integer isInternal = 1;

	/**
	 * setIsInternal(Integer isInternal)
	 *
	 * @param isInternal
	 *
	 */
	
	public void setIsInternal (Integer isInternal) {
		this.isInternal = isInternal;
	}

	/**
	 * getIsInternal()
	 *
	 * @return isInternal
	 *
	 */
	public Integer getIsInternal () {
		return this.isInternal;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 1;

	/**
	 * setIsActive(Integer isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public Integer getIsActive () {
		return this.isActive;
	}

	/** 
	 * departmentUser
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="departmentid", insertable=false, updatable=false)
	protected List<DepartmentUser> departmentUser;


	/** 
	 * getDepartmentUser()
	 *
	 * @return departmentUser
	 *
	 */
	@JsonIgnore
	public List<DepartmentUser> getDepartmentUser() {
		return this.departmentUser;
	}


	/** 
	 * setDepartmentUser
	 *
	 * @param departmentUser
	 *
	 */
	public void setDepartmentUser (List<DepartmentUser> departmentUser) {
		this.departmentUser = departmentUser;
	}



	/** 
	 * lab
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="departmentid", insertable=false, updatable=false)
	protected List<Lab> lab;


	/** 
	 * getLab()
	 *
	 * @return lab
	 *
	 */
	@JsonIgnore
	public List<Lab> getLab() {
		return this.lab;
	}


	/** 
	 * setLab
	 *
	 * @param lab
	 *
	 */
	public void setLab (List<Lab> lab) {
		this.lab = lab;
	}



	/** 
	 * labPending
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="departmentid", insertable=false, updatable=false)
	protected List<LabPending> labPending;


	/** 
	 * getLabPending()
	 *
	 * @return labPending
	 *
	 */
	@JsonIgnore
	public List<LabPending> getLabPending() {
		return this.labPending;
	}


	/** 
	 * setLabPending
	 *
	 * @param labPending
	 *
	 */
	public void setLabPending (List<LabPending> labPending) {
		this.labPending = labPending;
	}



}
