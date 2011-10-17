
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="department")
public class Department extends WaspModel {

	/** 
	 * departmentId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int departmentId;

	/**
	 * setDepartmentId(int departmentId)
	 *
	 * @param departmentId
	 *
	 */
	
	public void setDepartmentId (int departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * getDepartmentId()
	 *
	 * @return departmentId
	 *
	 */
	public int getDepartmentId () {
		return this.departmentId;
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
	protected int isInternal;

	/**
	 * setIsInternal(int isInternal)
	 *
	 * @param isInternal
	 *
	 */
	
	public void setIsInternal (int isInternal) {
		this.isInternal = isInternal;
	}

	/**
	 * getIsInternal()
	 *
	 * @return isInternal
	 *
	 */
	public int getIsInternal () {
		return this.isInternal;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected int isActive;

	/**
	 * setIsActive(int isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (int isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public int getIsActive () {
		return this.isActive;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected int lastUpdUser;

	/**
	 * setLastUpdUser(int lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (int lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public int getLastUpdUser () {
		return this.lastUpdUser;
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
