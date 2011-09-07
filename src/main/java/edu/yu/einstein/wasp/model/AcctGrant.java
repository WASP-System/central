
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

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="acct_grant")
public class AcctGrant extends WaspModel {

	/** 
	 * grantId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int grantId;

	/**
	 * setGrantId(int grantId)
	 *
	 * @param grantId
	 *
	 */
	
	public void setGrantId (int grantId) {
		this.grantId = grantId;
	}

	/**
	 * getGrantId()
	 *
	 * @return grantId
	 *
	 */
	public int getGrantId () {
		return this.grantId;
	}




	/** 
	 * labId
	 *
	 */
	@Column(name="labid")
	protected int labId;

	/**
	 * setLabId(int labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (int labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public int getLabId () {
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
		this.labId = lab.labId;
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
