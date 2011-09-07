
/**
 *
 * AcctLedger.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedger
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
@Table(name="acct_ledger")
public class AcctLedger extends WaspModel {

	/** 
	 * ledgerId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int ledgerId;

	/**
	 * setLedgerId(int ledgerId)
	 *
	 * @param ledgerId
	 *
	 */
	
	public void setLedgerId (int ledgerId) {
		this.ledgerId = ledgerId;
	}

	/**
	 * getLedgerId()
	 *
	 * @return ledgerId
	 *
	 */
	public int getLedgerId () {
		return this.ledgerId;
	}




	/** 
	 * invoiceId
	 *
	 */
	@Column(name="invoiceid")
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
	 * acctInvoice
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="invoiceid", insertable=false, updatable=false)
	protected AcctInvoice acctInvoice;

	/**
	 * setAcctInvoice (AcctInvoice acctInvoice)
	 *
	 * @param acctInvoice
	 *
	 */
	public void setAcctInvoice (AcctInvoice acctInvoice) {
		this.acctInvoice = acctInvoice;
		this.invoiceId = acctInvoice.invoiceId;
	}

	/**
	 * getAcctInvoice ()
	 *
	 * @return acctInvoice
	 *
	 */
	
	public AcctInvoice getAcctInvoice () {
		return this.acctInvoice;
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
	 * acctGrantjob
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", insertable=false, updatable=false)
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
