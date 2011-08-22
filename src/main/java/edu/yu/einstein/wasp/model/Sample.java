
/**
 *
 * Sample.java 
 * @author echeng (table2type.pl)
 *  
 * the Sample object
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
@Table(name="sample")
public class Sample extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int sampleId;
  public void setSampleId (int sampleId) {
    this.sampleId = sampleId;
  }
  public int getSampleId () {
    return this.sampleId;
  }


  @Column(name="typesampleid")
  protected int typeSampleId;
  public void setTypeSampleId (int typeSampleId) {
    this.typeSampleId = typeSampleId;
  }
  public int getTypeSampleId () {
    return this.typeSampleId;
  }


  @Column(name="submitter_labid")
  protected int submitterLabId;
  public void setSubmitterLabId (int submitterLabId) {
    this.submitterLabId = submitterLabId;
  }
  public int getSubmitterLabId () {
    return this.submitterLabId;
  }


  @Column(name="submitter_userid")
  protected int submitterUserId;
  public void setSubmitterUserId (int submitterUserId) {
    this.submitterUserId = submitterUserId;
  }
  public int getSubmitterUserId () {
    return this.submitterUserId;
  }


  @Column(name="submitter_jobid")
  protected Integer submitterJobId;
  public void setSubmitterJobId (Integer submitterJobId) {
    this.submitterJobId = submitterJobId;
  }
  public Integer getSubmitterJobId () {
    return this.submitterJobId;
  }


  @Column(name="isreceived")
  protected int isReceived;
  public void setIsReceived (int isReceived) {
    this.isReceived = isReceived;
  }
  public int getIsReceived () {
    return this.isReceived;
  }


  @Column(name="receiver_userid")
  protected Integer receiverUserId;
  public void setReceiverUserId (Integer receiverUserId) {
    this.receiverUserId = receiverUserId;
  }
  public Integer getReceiverUserId () {
    return this.receiverUserId;
  }


  @Column(name="receivedts")
  protected Date receiveDts;
  public void setReceiveDts (Date receiveDts) {
    this.receiveDts = receiveDts;
  }
  public Date getReceiveDts () {
    return this.receiveDts;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="isgood")
  protected Integer isGood;
  public void setIsGood (Integer isGood) {
    this.isGood = isGood;
  }
  public Integer getIsGood () {
    return this.isGood;
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
   @JoinColumn(name="submitter_jobid", insertable=false, updatable=false)
  protected Job job;
  public void setJob (Job job) {
    this.job = job;
    this.submitterJobId = job.jobId;
  }
  public Job getJob () {
    return this.job;
  }

  @NotAudited
  @ManyToOne
   @JoinColumn(name="submitter_labid", insertable=false, updatable=false)
  protected Lab lab;
  public void setLab (Lab lab) {
    this.lab = lab;
    this.submitterLabId = lab.labId;
  }
  public Lab getLab () {
    return this.lab;
  }

  @NotAudited
  @ManyToOne
   @JoinColumn(name="submitter_userid", insertable=false, updatable=false)
  protected User user;
  public void setUser (User user) {
    this.user = user;
    this.submitterUserId = user.UserId;
  }
  public User getUser () {
    return this.user;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<SampleMeta> sampleMeta;
  public List<SampleMeta> getSampleMeta()  {
    return this.sampleMeta;
  }
  public void setSampleMeta (List<SampleMeta> sampleMeta)  {
    this.sampleMeta = sampleMeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<SampleSource> sampleSource;
  public List<SampleSource> getSampleSource()  {
    return this.sampleSource;
  }
  public void setSampleSource (List<SampleSource> sampleSource)  {
    this.sampleSource = sampleSource;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="source_sampleid", insertable=false, updatable=false)
  protected List<SampleSource> sampleSourceViaSourceSampleId;
  public List<SampleSource> getSampleSourceViaSourceSampleId()  {
    return this.sampleSourceViaSourceSampleId;
  }
  public void setSampleSourceViaSourceSampleId (List<SampleSource> sampleSource)  {
    this.sampleSourceViaSourceSampleId = sampleSource;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<SampleBarcode> sampleBarcode;
  public List<SampleBarcode> getSampleBarcode()  {
    return this.sampleBarcode;
  }
  public void setSampleBarcode (List<SampleBarcode> sampleBarcode)  {
    this.sampleBarcode = sampleBarcode;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<SampleLab> sampleLab;
  public List<SampleLab> getSampleLab()  {
    return this.sampleLab;
  }
  public void setSampleLab (List<SampleLab> sampleLab)  {
    this.sampleLab = sampleLab;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<JobSample> jobSample;
  public List<JobSample> getJobSample()  {
    return this.jobSample;
  }
  public void setJobSample (List<JobSample> jobSample)  {
    this.jobSample = jobSample;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<SampleFile> sampleFile;
  public List<SampleFile> getSampleFile()  {
    return this.sampleFile;
  }
  public void setSampleFile (List<SampleFile> sampleFile)  {
    this.sampleFile = sampleFile;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<Run> run;
  public List<Run> getRun()  {
    return this.run;
  }
  public void setRun (List<Run> run)  {
    this.run = run;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<RunLane> runLane;
  public List<RunLane> getRunLane()  {
    return this.runLane;
  }
  public void setRunLane (List<RunLane> runLane)  {
    this.runLane = runLane;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected List<Statesample> statesample;
  public List<Statesample> getStatesample()  {
    return this.statesample;
  }
  public void setStatesample (List<Statesample> statesample)  {
    this.statesample = statesample;
  }



}
