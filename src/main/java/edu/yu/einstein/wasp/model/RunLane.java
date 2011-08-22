
/**
 *
 * RunLane.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLane object
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
@Table(name="runlane")
public class RunLane extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int runLaneId;
  public void setRunLaneId (int runLaneId) {
    this.runLaneId = runLaneId;
  }
  public int getRunLaneId () {
    return this.runLaneId;
  }


  @Column(name="runid")
  protected int runId;
  public void setRunId (int runId) {
    this.runId = runId;
  }
  public int getRunId () {
    return this.runId;
  }


  @Column(name="resourcelaneid")
  protected int resourcelaneId;
  public void setResourcelaneId (int resourcelaneId) {
    this.resourcelaneId = resourcelaneId;
  }
  public int getResourcelaneId () {
    return this.resourcelaneId;
  }


  @Column(name="sampleid")
  protected int sampleId;
  public void setSampleId (int sampleId) {
    this.sampleId = sampleId;
  }
  public int getSampleId () {
    return this.sampleId;
  }



  @NotAudited
  @ManyToOne
   @JoinColumn(name="runid", insertable=false, updatable=false)
  protected Run run;
  public void setRun (Run run) {
    this.run = run;
    this.runId = run.runId;
  }
  public Run getRun () {
    return this.run;
  }

  @NotAudited
  @ManyToOne
   @JoinColumn(name="resourcelaneid", insertable=false, updatable=false)
  protected ResourceLane resourceLane;
  public void setResourceLane (ResourceLane resourceLane) {
    this.resourceLane = resourceLane;
    this.resourcelaneId = resourceLane.resourceLaneId;
  }
  public ResourceLane getResourceLane () {
    return this.resourceLane;
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
   @JoinColumn(name="runlaneid", insertable=false, updatable=false)
  protected List<RunLanefile> runLanefile;
  public List<RunLanefile> getRunLanefile()  {
    return this.runLanefile;
  }
  public void setRunLanefile (List<RunLanefile> runLanefile)  {
    this.runLanefile = runLanefile;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="runlaneid", insertable=false, updatable=false)
  protected List<Staterunlane> staterunlane;
  public List<Staterunlane> getStaterunlane()  {
    return this.staterunlane;
  }
  public void setStaterunlane (List<Staterunlane> staterunlane)  {
    this.staterunlane = staterunlane;
  }



}
