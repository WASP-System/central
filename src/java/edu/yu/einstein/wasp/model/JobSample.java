
/**
 *
 * JobSample.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSample object
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
@Table(name="jobsample")
public class JobSample extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobSampleId;
  public void setJobSampleId (int jobSampleId) {
    this.jobSampleId = jobSampleId;
  }
  public int getJobSampleId () {
    return this.jobSampleId;
  }


  @Column(name="jobid")
  protected int jobId;
  public void setJobId (int jobId) {
    this.jobId = jobId;
  }
  public int getJobId () {
    return this.jobId;
  }


  @Column(name="sampleid")
  protected int sampleId;
  public void setSampleId (int sampleId) {
    this.sampleId = sampleId;
  }
  public int getSampleId () {
    return this.sampleId;
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
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected Sample sample;
  public void setSample (Sample sample) {
    this.sample = sample;
    this.sampleId = sample.sampleId;
  }
  
  public Sample getSample () {
    return this.sample;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="jobsampleid", insertable=false, updatable=false)
  protected List<JobSampleMeta> jobSampleMeta;
  public List<JobSampleMeta> getJobSampleMeta()  {
    return this.jobSampleMeta;
  }
  public void setJobSampleMeta (List<JobSampleMeta> jobSampleMeta)  {
    this.jobSampleMeta = jobSampleMeta;
  }



}
