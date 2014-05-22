
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

@Entity
@Audited
@Table(name="acct_quote")
public class AcctQuote extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1878774856095048045L;

	/**
	 * setQuoteId(Integer quoteId)
	 *
	 * @param quoteId
	 *
	 */
	@Deprecated
	public void setQuoteId (Integer quoteId) {
		setId(quoteId);
	}

	/**
	 * getQuoteId()
	 *
	 * @return quoteId
	 *
	 */
	@Deprecated
	public Integer getQuoteId () {
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
	@JsonIgnore
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

	
	/** 
	 * acctQuoteMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="quoteid", insertable=false, updatable=false)
	protected List<AcctQuoteMeta> acctQuoteMeta;


	/** 
	 * getAcctQuoteMeta()
	 *
	 * @return acctQuoteMeta
	 *
	 */
	@JsonIgnore
	public List<AcctQuoteMeta> getAcctQuoteMeta() {
		return this.acctQuoteMeta;
	}


	/** 
	 * setAcctQuoteMeta
	 *
	 * @param acctQuoteMeta
	 *
	 */
	public void setAcctQuoteMeta (List<AcctQuoteMeta> acctQuoteMeta) {
		this.acctQuoteMeta = acctQuoteMeta;
	}

}
