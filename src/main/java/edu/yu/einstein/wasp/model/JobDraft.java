
/**
 *
 * JobDraft.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraft object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="jobdraft")
public class JobDraft extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobDraftId;
  public void setJobDraftId (int jobDraftId) {
    this.jobDraftId = jobDraftId;
  }
  public int getJobDraftId () {
    return this.jobDraftId;
  }


  @Column(name="labid")
  @Range(min=1)
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
  @Range(min=1)
  protected int workflowId;
  public void setWorkflowId (int workflowId) {
    this.workflowId = workflowId;
  }
  public int getWorkflowId () {
    return this.workflowId;
  }


  @Column(name="name")
  @NotEmpty
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


  @Column(name="status")
  protected String status;
  public void setStatus (String status) {
    this.status = status;
  }
  public String getStatus () {
    return this.status;
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
   @JoinColumn(name="jobdraftid", insertable=false, updatable=false)
  protected List<JobDraftMeta> jobDraftMeta;
  public List<JobDraftMeta> getJobDraftMeta()  {
    return this.jobDraftMeta;
  }
  public void setJobDraftMeta (List<JobDraftMeta> jobDraftMeta)  {
    this.jobDraftMeta = jobDraftMeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobdraftid", insertable=false, updatable=false)
  protected List<SampleDraft> sampleDraft;
  public List<SampleDraft> getSampleDraft()  {
    return this.sampleDraft;
  }
  public void setSampleDraft (List<SampleDraft> sampleDraft)  {
    this.sampleDraft = sampleDraft;
  }



}
