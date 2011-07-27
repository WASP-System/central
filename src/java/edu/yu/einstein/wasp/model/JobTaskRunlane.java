
/**
 *
 * JobTaskRunlane.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskRunlane object
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
@Table(name="jobtask_runlane")
public class JobTaskRunlane extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobtaskrunlaneId;
  public void setJobtaskrunlaneId (int jobtaskrunlaneId) {
    this.jobtaskrunlaneId = jobtaskrunlaneId;
  }
  public int getJobtaskrunlaneId () {
    return this.jobtaskrunlaneId;
  }


  @Column(name="jobtaskid")
  protected int jobtaskId;
  public void setJobtaskId (int jobtaskId) {
    this.jobtaskId = jobtaskId;
  }
  public int getJobtaskId () {
    return this.jobtaskId;
  }


  @Column(name="runlaneid")
  protected int runlaneId;
  public void setRunlaneId (int runlaneId) {
    this.runlaneId = runlaneId;
  }
  public int getRunlaneId () {
    return this.runlaneId;
  }



  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="jobtaskid", insertable=false, updatable=false)
  protected JobTask jobTask;
  public void setJobTask (JobTask jobTask) {
    this.jobTask = jobTask;
    this.jobtaskId = jobTask.jobTaskId;
  }
  public JobTask getJobTask () {
    return this.jobTask;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="runlaneid", insertable=false, updatable=false)
  protected RunLane runLane;
  public void setRunLane (RunLane runLane) {
    this.runLane = runLane;
    this.runlaneId = runLane.runLaneId;
  }
  public RunLane getRunLane () {
    return this.runLane;
  }

}
