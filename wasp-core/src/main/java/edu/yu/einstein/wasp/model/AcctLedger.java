
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
@Table(name="acct_ledger")
public class AcctLedger extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2976336579608094205L;
	

	/**
	 * setLedgerId(Integer ledgerId)
	 *
	 * @param ledgerId
	 *
	 */
	@Deprecated
	public void setLedgerId (Integer ledgerId) {
		setId(ledgerId);
	}

	/**
	 * getLedgerId()
	 *
	 * @return ledgerId
	 *
	 */
	@Deprecated
	public Integer getLedgerId () {
		return getId();
	}




	/** 
	 * invoiceId
	 *
	 */
	@Column(name="invoiceid")
	protected Integer invoiceId;

	/**
	 * setInvoiceId(Integer invoiceId)
	 *
	 * @param invoiceId
	 *
	 */
	
	public void setInvoiceId (Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * getInvoiceId()
	 *
	 * @return invoiceId
	 *
	 */
	public Integer getInvoiceId () {
		return this.invoiceId;
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
		this.invoiceId = acctInvoice.getId();
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
	 * acctGrantjob
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobid", referencedColumnName="jobid", insertable=false, updatable=false)
	protected List<AcctGrantjob> acctGrantjob;


	/** 
	 * getAcctGrantjob()
	 *
	 * @return acctGrantjob
	 *
	 */
	@JsonIgnore
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
