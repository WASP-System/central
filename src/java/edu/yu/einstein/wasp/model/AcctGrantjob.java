
/**
 *
 * AcctGrantjob.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjob object
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
@Table(name="acct_grantjob")
public class AcctGrantjob extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobId;
  public void setJobId (int jobId) {
    this.jobId = jobId;
  }
  public int getJobId () {
    return this.jobId;
  }


  @Column(name="grantid")
  protected int grantId;
  public void setGrantId (int grantId) {
    this.grantId = grantId;
  }
  public int getGrantId () {
    return this.grantId;
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
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected AcctLedger acctLedger;
  public void setAcctLedger (AcctLedger acctLedger) {
    this.acctLedger = acctLedger;
    this.jobId = acctLedger.jobId;
  }
  public AcctLedger getAcctLedger () {
    return this.acctLedger;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="grantid", insertable=false, updatable=false)
  protected AcctGrant acctGrant;
  public void setAcctGrant (AcctGrant acctGrant) {
    this.acctGrant = acctGrant;
    this.grantId = acctGrant.grantId;
  }
  public AcctGrant getAcctGrant () {
    return this.acctGrant;
  }

}
