
/**
 *
 * AcctGrant.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrant
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="acct_grant")
public class AcctGrant extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1564045006597566511L;

	/**
	 * setGrantId(Integer grantId)
	 *
	 * @param grantId
	 *
	 */
	@Deprecated
	public void setGrantId (Integer grantId) {
		setId(grantId);
	}

	/**
	 * getGrantId()
	 *
	 * @return grantId
	 *
	 */
	@Deprecated
	public Integer getGrantId () {
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
	 * code
	 *
	 */
	@Column(name="code")
	protected String code;

	/**
	 * setCode(String code)
	 *
	 * @param code
	 *
	 */
	
	public void setCode (String code) {
		this.code = code;
	}

	/**
	 * getCode()
	 *
	 * @return code
	 *
	 */
	public String getCode () {
		return this.code;
	}




	/** 
	 * expirationdt
	 *
	 */
	@Column(name="expirationdt")
	protected Date expirationdt;

	/**
	 * setExpirationdt(Date expirationdt)
	 *
	 * @param expirationdt
	 *
	 */
	
	public void setExpirationdt (Date expirationdt) {
		this.expirationdt = expirationdt;
	}

	/**
	 * getExpirationdt()
	 *
	 * @return expirationdt
	 *
	 */
	public Date getExpirationdt () {
		return this.expirationdt;
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
	 * acctGrantjob
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="grantid", insertable=false, updatable=false)
	protected List<AcctGrantjob> acctGrantjob;


	/** 
	 * getAcctGrantjob()
	 *
	 * @return acctGrantjob
	 *
	 */
	public List<AcctGrantjob> getAcctGrantjob() {
		return this.acctGrantjob;
	}


	/** 
	 * setAcctGrantjob
	 *
	 * @param acctGrantjob
	 *
	 */
	public void setAcctGrantjob (List<AcctGrantjob> acctGrantjob) {
		this.acctGrantjob = acctGrantjob;
	}



}
