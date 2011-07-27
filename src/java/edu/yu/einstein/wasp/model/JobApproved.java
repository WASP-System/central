
/**
 *
 * JobApproved.java 
 * @author echeng (table2type.pl)
 *  
 * the JobApproved object
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
@Table(name="jobapproved")
public class JobApproved extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobApprovedId;
  public void setJobApprovedId (int jobApprovedId) {
    this.jobApprovedId = jobApprovedId;
  }
  public int getJobApprovedId () {
    return this.jobApprovedId;
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
