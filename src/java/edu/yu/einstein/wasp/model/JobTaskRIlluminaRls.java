
/**
 *
 * JobTaskRIlluminaRls.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskRIlluminaRls object
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
@Table(name="jobtask_r_illumina_rls")
public class JobTaskRIlluminaRls extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobtaskRlsId;
  public void setJobtaskRlsId (int jobtaskRlsId) {
    this.jobtaskRlsId = jobtaskRlsId;
  }
  public int getJobtaskRlsId () {
    return this.jobtaskRlsId;
  }


  @Column(name="jobtaskid")
  protected int jobtaskId;
  public void setJobtaskId (int jobtaskId) {
    this.jobtaskId = jobtaskId;
  }
  public int getJobtaskId () {
    return this.jobtaskId;
  }


  @Column(name="rlsid")
  protected int rlsId;
  public void setRlsId (int rlsId) {
    this.rlsId = rlsId;
  }
  public int getRlsId () {
    return this.rlsId;
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
   @JoinColumn(name="rlsid", insertable=false, updatable=false)
  protected RIlluminaRunlanesample rIlluminaRunlanesample;
  public void setRIlluminaRunlanesample (RIlluminaRunlanesample rIlluminaRunlanesample) {
    this.rIlluminaRunlanesample = rIlluminaRunlanesample;
    this.rlsId = rIlluminaRunlanesample.runlanesampleId;
  }
  public RIlluminaRunlanesample getRIlluminaRunlanesample () {
    return this.rIlluminaRunlanesample;
  }

}
