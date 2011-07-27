
/**
 *
 * AcctInvoice.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctInvoice object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.*;

@Entity
@Audited
@Table(name="acct_invoice")
public class AcctInvoice extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int invoiceId;
  public void setInvoiceId (int invoiceId) {
    this.invoiceId = invoiceId;
  }
  public int getInvoiceId () {
    return this.invoiceId;
  }


  @Column(name="quoteid")
  protected int quoteId;
  public void setQuoteId (int quoteId) {
    this.quoteId = quoteId;
  }
  public int getQuoteId () {
    return this.quoteId;
  }


  @Column(name="jobid")
  protected int jobId;
  public void setJobId (int jobId) {
    this.jobId = jobId;
  }
  public int getJobId () {
    return this.jobId;
  }


  @Column(name="amount")
  protected float amount;
  public void setAmount (float amount) {
    this.amount = amount;
  }
  public float getAmount () {
    return this.amount;
  }


  @Column(name="comment")
  protected String comment;
  public void setComment (String comment) {
    this.comment = comment;
  }
  public String getComment () {
    return this.comment;
  }


  @Column(name="lastupdts")
  protected Date lastUpdTs;
  public void setLastUpdTs (Date lastUpdTs) {
    this.lastUpdTs = lastUpdTs;
  }
  public Date getLastUpdTs () {
    return this.lastUpdTs;
  }


  @Column(name="lastupduser")
  protected int lastUpdUser;
  public void setLastUpdUser (int lastUpdUser) {
    this.lastUpdUser = lastUpdUser;
  }
  public int getLastUpdUser () {
    return this.lastUpdUser;
  }



  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="quoteid", insertable=false, updatable=false)
  protected AcctQuote acctQuote;
  public void setAcctQuote (AcctQuote acctQuote) {
    this.acctQuote = acctQuote;
    this.quoteId = acctQuote.quoteId;
  }
  public AcctQuote getAcctQuote () {
    return this.acctQuote;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected Job job;
  public void setJob (Job job) {
    this.job = job;
    this.jobId = job.jobId;
  }
  public Job getJob () {
    return this.job;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="invoiceid", insertable=false, updatable=false)
  protected List<AcctLedger> acctLedger;
  public List<AcctLedger> getAcctLedger()  {
    return this.acctLedger;
  }
  public void setAcctLedger (List<AcctLedger> acctLedger)  {
    this.acctLedger = acctLedger;
  }



}
