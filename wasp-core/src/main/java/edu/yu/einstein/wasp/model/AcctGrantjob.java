
/**
 *
 * AcctGrantjob.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjob
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="acct_grantjob")
public class AcctGrantjob extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8452582861645453970L;
	/** 
	 * jobId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobId;

	/**
	 * setJobId(Integer jobId)
	 *
	 * @param jobId
	 *
	 */
	
	public void setJobId (Integer jobId) {
		this.jobId = jobId;
	}

	/**
	 * getJobId()
	 *
	 * @return jobId
	 *
	 */
	public Integer getJobId () {
		return this.jobId;
	}




	/** 
	 * grantId
	 *
	 */
	@Column(name="grantid")
	protected Integer grantId;

	/**
	 * setGrantId(Integer grantId)
	 *
	 * @param grantId
	 *
	 */
	
	public void setGrantId (Integer grantId) {
		this.grantId = grantId;
	}

	/**
	 * getGrantId()
	 *
	 * @return grantId
	 *
	 */
	public Integer getGrantId () {
		return this.grantId;
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
	 * acctLedger
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected AcctLedger acctLedger;

	/**
	 * setAcctLedger (AcctLedger acctLedger)
	 *
	 * @param acctLedger
	 *
	 */
	public void setAcctLedger (AcctLedger acctLedger) {
		this.acctLedger = acctLedger;
		this.jobId = acctLedger.jobId;
	}

	/**
	 * getAcctLedger ()
	 *
	 * @return acctLedger
	 *
	 */
	
	public AcctLedger getAcctLedger () {
		return this.acctLedger;
	}


	/**
	 * acctGrant
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="grantid", insertable=false, updatable=false)
	protected AcctGrant acctGrant;

	/**
	 * setAcctGrant (AcctGrant acctGrant)
	 *
	 * @param acctGrant
	 *
	 */
	public void setAcctGrant (AcctGrant acctGrant) {
		this.acctGrant = acctGrant;
		this.grantId = acctGrant.grantId;
	}

	/**
	 * getAcctGrant ()
	 *
	 * @return acctGrant
	 *
	 */
	
	public AcctGrant getAcctGrant () {
		return this.acctGrant;
	}


}
