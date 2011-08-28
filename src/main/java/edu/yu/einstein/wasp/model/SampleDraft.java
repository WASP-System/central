
/**
 *
 * SampleDraft.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraft object
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
@Table(name="sampledraft")
public class SampleDraft extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int sampleDraftId;
  public void setSampleDraftId (int sampleDraftId) {
    this.sampleDraftId = sampleDraftId;
  }
  public int getSampleDraftId () {
    return this.sampleDraftId;
  }


  @Column(name="typesampleid")
  protected int typeSampleId;
  public void setTypeSampleId (int typeSampleId) {
    this.typeSampleId = typeSampleId;
  }
  public int getTypeSampleId () {
    return this.typeSampleId;
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


  @Column(name="jobdraftid")
  protected Integer jobdraftId;
  public void setJobdraftId (Integer jobdraftId) {
    this.jobdraftId = jobdraftId;
  }
  public Integer getJobdraftId () {
    return this.jobdraftId;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
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
   @JoinColumn(name="typesampleid", insertable=false, updatable=false)
  protected TypeSample typeSample;
  public void setTypeSample (TypeSample typeSample) {
    this.typeSample = typeSample;
    this.typeSampleId = typeSample.typeSampleId;
  }
  
  public TypeSample getTypeSample () {
    return this.typeSample;
  }

  @NotAudited
  @ManyToOne
   @JoinColumn(name="jobdraftid", insertable=false, updatable=false)
  protected JobDraft jobDraft;
  public void setJobDraft (JobDraft jobDraft) {
    this.jobDraft = jobDraft;
    this.jobdraftId = jobDraft.jobDraftId;
  }
  
  public JobDraft getJobDraft () {
    return this.jobDraft;
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
  @OneToMany
   @JoinColumn(name="sampledraftid", insertable=false, updatable=false)
  protected List<SampleDraftMeta> sampleDraftMeta;
  public List<SampleDraftMeta> getSampleDraftMeta()  {
    return this.sampleDraftMeta;
  }
  public void setSampleDraftMeta (List<SampleDraftMeta> sampleDraftMeta)  {
    this.sampleDraftMeta = sampleDraftMeta;
  }



}
