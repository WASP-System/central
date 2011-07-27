
/**
 *
 * AcctJobquotecurrent.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrent object
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
@Table(name="acct_jobquotecurrent")
public class AcctJobquotecurrent extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobId;
  public void setJobId (int jobId) {
    this.jobId = jobId;
  }
  public int getJobId () {
    return this.jobId;
  }


  @Column(name="quoteid")
  protected int quoteId;
  public void setQuoteId (int quoteId) {
    this.quoteId = quoteId;
  }
  public int getQuoteId () {
    return this.quoteId;
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

}
