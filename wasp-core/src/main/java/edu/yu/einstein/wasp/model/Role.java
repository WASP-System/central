
/**
 *
 * Role.java 
 * @author echeng (table2type.pl)
 *  
 * the Role
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
@Table(name="wrole")
public class Role extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8714161484537492624L;

	/**
	 * setRoleId(Integer roleId)
	 *
	 * @param roleId
	 *
	 */
	@Deprecated
	public void setRoleId (Integer roleId) {
		setId(roleId);
	}

	/**
	 * getRoleId()
	 *
	 * @return roleId
	 *
	 */
	@Deprecated
	public Integer getRoleId () {
		return getId();
	}




	/** 
	 * roleName
	 *
	 */
	@Column(name="rolename")
	protected String roleName;

	/**
	 * setRoleName(String roleName)
	 *
	 * @param roleName
	 *
	 */
	
	public void setRoleName (String roleName) {
		this.roleName = roleName;
	}

	/**
	 * getRoleName()
	 *
	 * @return roleName
	 *
	 */
	public String getRoleName () {
		return this.roleName;
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
	 * domain
	 *
	 */
	@Column(name="domain")
	protected String domain;

	/**
	 * setDomain(String domain)
	 *
	 * @param domain
	 *
	 */
	
	public void setDomain (String domain) {
		this.domain = domain;
	}

	/**
	 * getDomain()
	 *
	 * @return domain
	 *
	 */
	public String getDomain () {
		return this.domain;
	}




	/** 
	 * roleset
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="parentroleid", insertable=false, updatable=false)
	protected List<Roleset> roleset;


	/** 
	 * getRoleset()
	 *
	 * @return roleset
	 *
	 */
	@JsonIgnore
	public List<Roleset> getRoleset() {
		return this.roleset;
	}


	/** 
	 * setRoleset
	 *
	 * @param roleset
	 *
	 */
	public void setRoleset (List<Roleset> roleset) {
		this.roleset = roleset;
	}



	/** 
	 * rolesetViaChildroleId
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="childroleid", insertable=false, updatable=false)
	protected List<Roleset> rolesetViaChildroleId;


	/** 
	 * getRolesetViaChildroleId()
	 *
	 * @return rolesetViaChildroleId
	 *
	 */
	@JsonIgnore
	public List<Roleset> getRolesetViaChildroleId() {
		return this.rolesetViaChildroleId;
	}


	/** 
	 * setRolesetViaChildroleId
	 *
	 * @param roleset
	 *
	 */
	public void setRolesetViaChildroleId (List<Roleset> roleset) {
		this.rolesetViaChildroleId = roleset;
	}



	/** 
	 * userrole
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="roleid", insertable=false, updatable=false)
	protected List<Userrole> userrole;


	/** 
	 * getUserrole()
	 *
	 * @return userrole
	 *
	 */
	@JsonIgnore
	public List<Userrole> getUserrole() {
		return this.userrole;
	}


	/** 
	 * setUserrole
	 *
	 * @param userrole
	 *
	 */
	public void setUserrole (List<Userrole> userrole) {
		this.userrole = userrole;
	}



	/** 
	 * labUser
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="roleid", insertable=false, updatable=false)
	protected List<LabUser> labUser;


	/** 
	 * getLabUser()
	 *
	 * @return labUser
	 *
	 */
	@JsonIgnore
	public List<LabUser> getLabUser() {
		return this.labUser;
	}


	/** 
	 * setLabUser
	 *
	 * @param labUser
	 *
	 */
	public void setLabUser (List<LabUser> labUser) {
		this.labUser = labUser;
	}



	/** 
	 * jobUser
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="roleid", insertable=false, updatable=false)
	protected List<JobUser> jobUser;


	/** 
	 * getJobUser()
	 *
	 * @return jobUser
	 *
	 */
	@JsonIgnore
	public List<JobUser> getJobUser() {
		return this.jobUser;
	}


	/** 
	 * setJobUser
	 *
	 * @param jobUser
	 *
	 */
	public void setJobUser (List<JobUser> jobUser) {
		this.jobUser = jobUser;
	}



	/** 
	 * acctQuoteUser
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="roleid", insertable=false, updatable=false)
	protected List<AcctQuoteUser> acctQuoteUser;


	/** 
	 * getAcctQuoteUser()
	 *
	 * @return acctQuoteUser
	 *
	 */
	@JsonIgnore
	public List<AcctQuoteUser> getAcctQuoteUser() {
		return this.acctQuoteUser;
	}


	/** 
	 * setAcctQuoteUser
	 *
	 * @param acctQuoteUser
	 *
	 */
	public void setAcctQuoteUser (List<AcctQuoteUser> acctQuoteUser) {
		this.acctQuoteUser = acctQuoteUser;
	}



}
