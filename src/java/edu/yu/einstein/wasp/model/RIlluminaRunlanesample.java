
/**
 *
 * RIlluminaRunlanesample.java 
 * @author echeng (table2type.pl)
 *  
 * the RIlluminaRunlanesample object
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
@Table(name="r_illumina_runlanesample")
public class RIlluminaRunlanesample extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int runlanesampleId;
  public void setRunlanesampleId (int runlanesampleId) {
    this.runlanesampleId = runlanesampleId;
  }
  public int getRunlanesampleId () {
    return this.runlanesampleId;
  }


  @Column(name="runid")
  protected int runId;
  public void setRunId (int runId) {
    this.runId = runId;
  }
  public int getRunId () {
    return this.runId;
  }


  @Column(name="laneid")
  protected int laneId;
  public void setLaneId (int laneId) {
    this.laneId = laneId;
  }
  public int getLaneId () {
    return this.laneId;
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
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="runid", insertable=false, updatable=false)
  protected RIlluminaRun rIlluminaRun;
  public void setRIlluminaRun (RIlluminaRun rIlluminaRun) {
    this.rIlluminaRun = rIlluminaRun;
    this.runId = rIlluminaRun.runId;
  }
  public RIlluminaRun getRIlluminaRun () {
    return this.rIlluminaRun;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="laneid", insertable=false, updatable=false)
  protected RIlluminaLane rIlluminaLane;
  public void setRIlluminaLane (RIlluminaLane rIlluminaLane) {
    this.rIlluminaLane = rIlluminaLane;
    this.laneId = rIlluminaLane.laneId;
  }
  public RIlluminaLane getRIlluminaLane () {
    return this.rIlluminaLane;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
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
   @JoinColumn(name="rlsid", insertable=false, updatable=false)
  protected List<RIlluminaRlsfile> rIlluminaRlsfile;
  public List<RIlluminaRlsfile> getRIlluminaRlsfile()  {
    return this.rIlluminaRlsfile;
  }
  public void setRIlluminaRlsfile (List<RIlluminaRlsfile> rIlluminaRlsfile)  {
    this.rIlluminaRlsfile = rIlluminaRlsfile;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="rlsid", insertable=false, updatable=false)
  protected List<JobTaskRIlluminaRls> jobTaskRIlluminaRls;
  public List<JobTaskRIlluminaRls> getJobTaskRIlluminaRls()  {
    return this.jobTaskRIlluminaRls;
  }
  public void setJobTaskRIlluminaRls (List<JobTaskRIlluminaRls> jobTaskRIlluminaRls)  {
    this.jobTaskRIlluminaRls = jobTaskRIlluminaRls;
  }



}
