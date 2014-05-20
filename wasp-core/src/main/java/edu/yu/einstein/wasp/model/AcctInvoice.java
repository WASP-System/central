
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
@Table(name="acct_invoice")
public class AcctInvoice extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6225389694897517159L;

	/**
	 * setInvoiceId(Integer invoiceId)
	 *
	 * @param invoiceId
	 *
	 */
	@Deprecated
	public void setInvoiceId (Integer invoiceId) {
		setId(invoiceId);
	}

	/**
	 * getInvoiceId()
	 *
	 * @return invoiceId
	 *
	 */
	@Deprecated
	public Integer getInvoiceId () {
		return getId();
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
	@JsonIgnore
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
