
/**
 *
 * JobTask.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTask object
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
@Table(name="jobtask")
public class JobTask extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobTaskId;
  public void setJobTaskId (int jobTaskId) {
    this.jobTaskId = jobTaskId;
  }
  public int getJobTaskId () {
    return this.jobTaskId;
  }


  @Column(name="jobid")
  protected int jobId;
  public void setJobId (int jobId) {
    this.jobId = jobId;
  }
  public int getJobId () {
    return this.jobId;
  }


  @Column(name="taskid")
  protected int taskId;
  public void setTaskId (int taskId) {
    this.taskId = taskId;
  }
  public int getTaskId () {
    return this.taskId;
  }


  @Column(name="iname")
  protected String iName;
  public void setIName (String iName) {
    this.iName = iName;
  }
  public String getIName () {
    return this.iName;
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

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="taskid", insertable=false, updatable=false)
  protected Task task;
  public void setTask (Task task) {
    this.task = task;
    this.taskId = task.taskId;
  }
  public Task getTask () {
    return this.task;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="jobtaskid", insertable=false, updatable=false)
  protected List<JobTaskmeta> jobTaskmeta;
  public List<JobTaskmeta> getJobTaskmeta()  {
    return this.jobTaskmeta;
  }
  public void setJobTaskmeta (List<JobTaskmeta> jobTaskmeta)  {
    this.jobTaskmeta = jobTaskmeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobtaskid", insertable=false, updatable=false)
  protected List<JobTaskRunlane> jobTaskRunlane;
  public List<JobTaskRunlane> getJobTaskRunlane()  {
    return this.jobTaskRunlane;
  }
  public void setJobTaskRunlane (List<JobTaskRunlane> jobTaskRunlane)  {
    this.jobTaskRunlane = jobTaskRunlane;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="jobtaskid", insertable=false, updatable=false)
  protected List<JobTaskAChipseqArun> jobTaskAChipseqArun;
  public List<JobTaskAChipseqArun> getJobTaskAChipseqArun()  {
    return this.jobTaskAChipseqArun;
  }
  public void setJobTaskAChipseqArun (List<JobTaskAChipseqArun> jobTaskAChipseqArun)  {
    this.jobTaskAChipseqArun = jobTaskAChipseqArun;
  }



}
