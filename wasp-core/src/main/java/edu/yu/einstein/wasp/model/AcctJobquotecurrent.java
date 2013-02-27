
/**
 *
 * AcctJobquotecurrent.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrent
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
@Table(name="acct_jobquotecurrent")
public class AcctJobquotecurrent extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1955514379181334967L;

	/**
	 * setCurrentId(Integer currentId)
	 *
	 * @param currentId
	 *
	 */
	@Deprecated
	public void setCurrentId (Integer currentId) {
		setId(currentId);
	}

	/**
	 * getCurrentId()
	 *
	 * @return currentId
	 *
	 */
	@Deprecated
	public Integer getCurrentId () {
		return getId();
	}



	/** 
	 * jobId
	 *
	 */
	@Column(name="jobid")
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
	 * quoteId
	 *
	 */
	@Column(name="quoteid")
	protected Integer quoteId;

	/**
	 * setQuoteId(Integer quoteId)
	 *
	 * @param quoteId
	 *
	 */
	
	public void setQuoteId (Integer quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * getQuoteId()
	 *
	 * @return quoteId
	 *
	 */
	public Integer getQuoteId () {
		return this.quoteId;
	}


	/**
	 * job
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected Job job;

	/**
	 * setJob (Job job)
	 *
	 * @param job
	 *
	 */
	public void setJob (Job job) {
		this.job = job;
		this.jobId = job.getId();
	}

	/**
	 * getJob ()
	 *
	 * @return job
	 *
	 */
	
	public Job getJob () {
		return this.job;
	}


	/**
	 * acctQuote
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="quoteid", insertable=false, updatable=false)
	protected AcctQuote acctQuote;

	/**
	 * setAcctQuote (AcctQuote acctQuote)
	 *
	 * @param acctQuote
	 *
	 */
	public void setAcctQuote (AcctQuote acctQuote) {
		this.acctQuote = acctQuote;
		this.quoteId = acctQuote.getId();
	}

	/**
	 * getAcctQuote ()
	 *
	 * @return acctQuote
	 *
	 */
	
	public AcctQuote getAcctQuote () {
		return this.acctQuote;
	}


}
