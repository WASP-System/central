
/**
 *
 * JobCost.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCost object
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
@Table(name="jobcost")
public class JobCost extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobCostId;
  public void setJobCostId (int jobCostId) {
    this.jobCostId = jobCostId;
  }
  public int getJobCostId () {
    return this.jobCostId;
  }


  @Column(name="jobid")
  protected int jobId;
  public void setJobId (int jobId) {
    this.jobId = jobId;
  }
  public int getJobId () {
    return this.jobId;
  }


  @Column(name="ispending")
  protected int isPending;
  public void setIsPending (int isPending) {
    this.isPending = isPending;
  }
  public int getIsPending () {
    return this.isPending;
  }


  @Column(name="isapproved")
  protected int isApproved;
  public void setIsApproved (int isApproved) {
    this.isApproved = isApproved;
  }
  public int getIsApproved () {
    return this.isApproved;
  }


  @Column(name="approveduserid")
  protected int approvedUserId;
  public void setApprovedUserId (int approvedUserId) {
    this.approvedUserId = approvedUserId;
  }
  public int getApprovedUserId () {
    return this.approvedUserId;
  }


  @Column(name="approvedts")
  protected Date approveDts;
  public void setApproveDts (Date approveDts) {
    this.approveDts = approveDts;
  }
  public Date getApproveDts () {
    return this.approveDts;
  }


  @Column(name="approvedamount")
  protected float approvedamount;
  public void setApprovedamount (float approvedamount) {
    this.approvedamount = approvedamount;
  }
  public float getApprovedamount () {
    return this.approvedamount;
  }


  @Column(name="isreceived")
  protected int isReceived;
  public void setIsReceived (int isReceived) {
    this.isReceived = isReceived;
  }
  public int getIsReceived () {
    return this.isReceived;
  }


  @Column(name="receiveduserid")
  protected int receivedUserId;
  public void setReceivedUserId (int receivedUserId) {
    this.receivedUserId = receivedUserId;
  }
  public int getReceivedUserId () {
    return this.receivedUserId;
  }


  @Column(name="receivedts")
  protected Date receiveDts;
  public void setReceiveDts (Date receiveDts) {
    this.receiveDts = receiveDts;
  }
  public Date getReceiveDts () {
    return this.receiveDts;
  }


  @Column(name="receivedmount")
  protected float receivedmount;
  public void setReceivedmount (float receivedmount) {
    this.receivedmount = receivedmount;
  }
  public float getReceivedmount () {
    return this.receivedmount;
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
  protected Job job;
  public void setJob (Job job) {
    this.job = job;
    this.jobId = job.jobId;
  }
  public Job getJob () {
    return this.job;
  }

}
