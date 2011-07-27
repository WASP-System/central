
/**
 *
 * JobTaskAChipseqArun.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskAChipseqArun object
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
@Table(name="jobtask_a_chipseq_arun")
public class JobTaskAChipseqArun extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobtaskArunId;
  public void setJobtaskArunId (int jobtaskArunId) {
    this.jobtaskArunId = jobtaskArunId;
  }
  public int getJobtaskArunId () {
    return this.jobtaskArunId;
  }


  @Column(name="jobtaskid")
  protected int jobtaskId;
  public void setJobtaskId (int jobtaskId) {
    this.jobtaskId = jobtaskId;
  }
  public int getJobtaskId () {
    return this.jobtaskId;
  }


  @Column(name="arunid")
  protected int arunId;
  public void setArunId (int arunId) {
    this.arunId = arunId;
  }
  public int getArunId () {
    return this.arunId;
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
   @JoinColumn(name="arunid", insertable=false, updatable=false)
  protected AChipseqArun aChipseqArun;
  public void setAChipseqArun (AChipseqArun aChipseqArun) {
    this.aChipseqArun = aChipseqArun;
    this.arunId = aChipseqArun.arunId;
  }
  public AChipseqArun getAChipseqArun () {
    return this.aChipseqArun;
  }

}
