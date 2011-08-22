
/**
 *
 * AcctQuote.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuote object
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
@Table(name="acct_quote")
public class AcctQuote extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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


  @Column(name="userid")
  protected Integer UserId;
  public void setUserId (Integer UserId) {
    this.UserId = UserId;
  }
  public Integer getUserId () {
    return this.UserId;
  }


  @Column(name="comment")
  protected String comment;
  public void setComment (String comment) {
    this.comment = comment;
  }
  public String getComment () {
    return this.comment;
  }


  @Column(name="isactive")
  protected int isActive;
  public void setIsActive (int isActive) {
    this.isActive = isActive;
  }
  public int getIsActive () {
    return this.isActive;
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
  @ManyToOne
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected User user;
  public void setUser (User user) {
    this.user = user;
    this.UserId = user.UserId;
  }
  public User getUser () {
    return this.user;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="quoteid", insertable=false, updatable=false)
  protected List<AcctJobquotecurrent> acctJobquotecurrent;
  public List<AcctJobquotecurrent> getAcctJobquotecurrent()  {
    return this.acctJobquotecurrent;
  }
  public void setAcctJobquotecurrent (List<AcctJobquotecurrent> acctJobquotecurrent)  {
    this.acctJobquotecurrent = acctJobquotecurrent;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="quoteid", insertable=false, updatable=false)
  protected List<AcctQuoteUser> acctQuoteUser;
  public List<AcctQuoteUser> getAcctQuoteUser()  {
    return this.acctQuoteUser;
  }
  public void setAcctQuoteUser (List<AcctQuoteUser> acctQuoteUser)  {
    this.acctQuoteUser = acctQuoteUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="quoteid", insertable=false, updatable=false)
  protected List<AcctInvoice> acctInvoice;
  public List<AcctInvoice> getAcctInvoice()  {
    return this.acctInvoice;
  }
  public void setAcctInvoice (List<AcctInvoice> acctInvoice)  {
    this.acctInvoice = acctInvoice;
  }



}
