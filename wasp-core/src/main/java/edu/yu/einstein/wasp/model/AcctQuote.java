
/**
 *
 * AcctQuote.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuote
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="acct_quote")
public class AcctQuote extends WaspModel {

	/** 
	 * quoteId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * amount
	 *
	 */
	@Column(name="amount")
	protected float amount;

	/**
	 * setAmount(float amount)
	 *
	 * @param amount
	 *
	 */
	
	public void setAmount (float amount) {
		this.amount = amount;
	}

	/**
	 * getAmount()
	 *
	 * @return amount
	 *
	 */
	public float getAmount () {
		return this.amount;
	}




	/** 
	 * UserId
	 *
	 */
	@Column(name="userid")
	protected Integer UserId;

	/**
	 * setUserId(Integer UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (Integer UserId) {
		this.UserId = UserId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public Integer getUserId () {
		return this.UserId;
	}




	/** 
	 * comment
	 *
	 */
	@Column(name="comment")
	protected String comment;

	/**
	 * setComment(String comment)
	 *
	 * @param comment
	 *
	 */
	
	public void setComment (String comment) {
		this.comment = comment;
	}

	/**
	 * getComment()
	 *
	 * @return comment
	 *
	 */
	public String getComment () {
		return this.comment;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive;

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
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
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
		this.jobId = job.jobId;
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
		this.UserId = user.UserId;
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
	 * acctJobquotecurrent
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="quoteid", insertable=false, updatable=false)
	protected List<AcctJobquotecurrent> acctJobquotecurrent;


	/** 
	 * getAcctJobquotecurrent()
	 *
	 * @return acctJobquotecurrent
	 *
	 */
	public List<AcctJobquotecurrent> getAcctJobquotecurrent() {
		return this.acctJobquotecurrent;
	}


	/** 
	 * setAcctJobquotecurrent
	 *
	 * @param acctJobquotecurrent
	 *
	 */
	public void setAcctJobquotecurrent (List<AcctJobquotecurrent> acctJobquotecurrent) {
		this.acctJobquotecurrent = acctJobquotecurrent;
	}



	/** 
	 * acctQuoteUser
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="quoteid", insertable=false, updatable=false)
	protected List<AcctQuoteUser> acctQuoteUser;


	/** 
	 * getAcctQuoteUser()
	 *
	 * @return acctQuoteUser
	 *
	 */
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



	/** 
	 * acctInvoice
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="quoteid", insertable=false, updatable=false)
	protected List<AcctInvoice> acctInvoice;


	/** 
	 * getAcctInvoice()
	 *
	 * @return acctInvoice
	 *
	 */
	public List<AcctInvoice> getAcctInvoice() {
		return this.acctInvoice;
	}


	/** 
	 * setAcctInvoice
	 *
	 * @param acctInvoice
	 *
	 */
	public void setAcctInvoice (List<AcctInvoice> acctInvoice) {
		this.acctInvoice = acctInvoice;
	}



}
