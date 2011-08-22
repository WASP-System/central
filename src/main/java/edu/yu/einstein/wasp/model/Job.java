
/**
 *
 * Job.java 
 * @author echeng (table2type.pl)
 *  
 * the Job object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="job")
public class Job extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobId;
  public void setJobId (int jobId) {
    this.jobId = jobId;
  }
  public int getJobId () {
    return this.jobId;
  }


  @Column(name="labid")
  protected int labId;
  public void setLabId (int labId) {
    this.labId = labId;
  }
  public int getLabId () {
    return this.labId;
  }


  @Column(name="userid")
  protected int UserId;
  public void setUserId (int UserId) {
    this.UserId = UserId;
  }
  public int getUserId () {
    return this.UserId;
  }


  @Column(name="workflowid")
  protected int workflowId;
  public void setWorkflowId (int workflowId) {
    this.workflowId = workflowId;
  }
  public int getWorkflowId () {
    return this.workflowId;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="createts")
  protected Date createts;
  public void setCreatets (Date createts) {
    this.createts = createts;
  }
  public Date getCreatets () {
    return this.createts;
  }


  @Column(name="viewablebylab")
  protected int viewablebylab;
  public void setViewablebylab (int viewablebylab) {
    this.viewablebylab = viewablebylab;
  }
  public int getViewablebylab () {
    return this.viewablebylab;
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
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected Lab lab;
  public void setLab (Lab lab) {
    this.lab = lab;
    this.labId = lab.labId;
  }
  
  public Lab getLab () {
    return this.lab;
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
  @ManyToOne
   @JoinColumn(name="workflowid", insertable=false, updatable=false)
  protected Workflow workflow;
  public void setWorkflow (Workflow workflow) {
    this.workflow = workflow;
    this.workflowId = workflow.workflowId;
  }
  
  public Workflow getWorkflow () {
    return this.workflow;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<JobMeta> jobMeta;
  public List<JobMeta> getJobMeta()  {
    return this.jobMeta;
  }
  public void setJobMeta (List<JobMeta> jobMeta)  {
    this.jobMeta = jobMeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<JobUser> jobUser;
  public List<JobUser> getJobUser()  {
    return this.jobUser;
  }
  public void setJobUser (List<JobUser> jobUser)  {
    this.jobUser = jobUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="submitter_jobid", insertable=false, updatable=false)
  protected List<Sample> sample;
  public List<Sample> getSample()  {
    return this.sample;
  }
  public void setSample (List<Sample> sample)  {
    this.sample = sample;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<JobSample> jobSample;
  public List<JobSample> getJobSample()  {
    return this.jobSample;
  }
  public void setJobSample (List<JobSample> jobSample)  {
    this.jobSample = jobSample;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="workflowid", insertable=false, updatable=false)
  protected List<AcctWorkflowcost> acctWorkflowcost;
  public List<AcctWorkflowcost> getAcctWorkflowcost()  {
    return this.acctWorkflowcost;
  }
  public void setAcctWorkflowcost (List<AcctWorkflowcost> acctWorkflowcost)  {
    this.acctWorkflowcost = acctWorkflowcost;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<AcctQuote> acctQuote;
  public List<AcctQuote> getAcctQuote()  {
    return this.acctQuote;
  }
  public void setAcctQuote (List<AcctQuote> acctQuote)  {
    this.acctQuote = acctQuote;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<AcctJobquotecurrent> acctJobquotecurrent;
  public List<AcctJobquotecurrent> getAcctJobquotecurrent()  {
    return this.acctJobquotecurrent;
  }
  public void setAcctJobquotecurrent (List<AcctJobquotecurrent> acctJobquotecurrent)  {
    this.acctJobquotecurrent = acctJobquotecurrent;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<AcctInvoice> acctInvoice;
  public List<AcctInvoice> getAcctInvoice()  {
    return this.acctInvoice;
  }
  public void setAcctInvoice (List<AcctInvoice> acctInvoice)  {
    this.acctInvoice = acctInvoice;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<AcctLedger> acctLedger;
  public List<AcctLedger> getAcctLedger()  {
    return this.acctLedger;
  }
  public void setAcctLedger (List<AcctLedger> acctLedger)  {
    this.acctLedger = acctLedger;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<JobFile> jobFile;
  public List<JobFile> getJobFile()  {
    return this.jobFile;
  }
  public void setJobFile (List<JobFile> jobFile)  {
    this.jobFile = jobFile;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected List<Statejob> statejob;
  public List<Statejob> getStatejob()  {
    return this.statejob;
  }
  public void setStatejob (List<Statejob> statejob)  {
    this.statejob = statejob;
  }



}
