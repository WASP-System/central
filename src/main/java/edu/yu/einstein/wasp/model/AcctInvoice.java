
/**
 *
 * AcctInvoice.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctInvoice
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
@Table(name="acct_invoice")
public class AcctInvoice extends WaspModel {

	/** 
	 * invoiceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int invoiceId;

	/**
	 * setInvoiceId(int invoiceId)
	 *
	 * @param invoiceId
	 *
	 */
	
	public void setInvoiceId (int invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * getInvoiceId()
	 *
	 * @return invoiceId
	 *
	 */
	public int getInvoiceId () {
		return this.invoiceId;
	}




	/** 
	 * quoteId
	 *
	 */
	@Column(name="quoteid")
	protected int quoteId;

	/**
	 * setQuoteId(int quoteId)
	 *
	 * @param quoteId
	 *
	 */
	
	public void setQuoteId (int quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * getQuoteId()
	 *
	 * @return quoteId
	 *
	 */
	public int getQuoteId () {
		return this.quoteId;
	}




	/** 
	 * jobId
	 *
	 */
	@Column(name="jobid")
	protected int jobId;

	/**
	 * setJobId(int jobId)
	 *
	 * @param jobId
	 *
	 */
	
	public void setJobId (int jobId) {
		this.jobId = jobId;
	}

	/**
	 * getJobId()
	 *
	 * @return jobId
	 *
	 */
	public int getJobId () {
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
		this.quoteId = acctQuote.quoteId;
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
	 * acctLedger
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="invoiceid", insertable=false, updatable=false)
	protected List<AcctLedger> acctLedger;


	/** 
	 * getAcctLedger()
	 *
	 * @return acctLedger
	 *
	 */
	public List<AcctLedger> getAcctLedger() {
		return this.acctLedger;
	}


	/** 
	 * setAcctLedger
	 *
	 * @param acctLedger
	 *
	 */
	public void setAcctLedger (List<AcctLedger> acctLedger) {
		this.acctLedger = acctLedger;
	}



}
