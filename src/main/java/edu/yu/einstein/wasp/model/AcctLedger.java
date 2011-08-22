
/**
 *
 * AcctLedger.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedger object
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
@Table(name="acct_ledger")
public class AcctLedger extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int ledgerId;
  public void setLedgerId (int ledgerId) {
    this.ledgerId = ledgerId;
  }
  public int getLedgerId () {
    return this.ledgerId;
  }


  @Column(name="invoiceid")
  protected int invoiceId;
  public void setInvoiceId (int invoiceId) {
    this.invoiceId = invoiceId;
  }
  public int getInvoiceId () {
    return this.invoiceId;
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
  @ManyToOne
   @JoinColumn(name="invoiceid", insertable=false, updatable=false)
  protected AcctInvoice acctInvoice;
  public void setAcctInvoice (AcctInvoice acctInvoice) {
    this.acctInvoice = acctInvoice;
    this.invoiceId = acctInvoice.invoiceId;
  }
  public AcctInvoice getAcctInvoice () {
    return this.acctInvoice;
  }

  @NotAudited
  @ManyToOne
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
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<AcctGrantjob> acctGrantjob;
  public List<AcctGrantjob> getAcctGrantjob()  {
    return this.acctGrantjob;
  }
  public void setAcctGrantjob (List<AcctGrantjob> acctGrantjob)  {
    this.acctGrantjob = acctGrantjob;
  }



}
