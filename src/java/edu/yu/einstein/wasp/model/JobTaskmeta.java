
/**
 *
 * JobTaskmeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskmeta object
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
@Table(name="jobtaskmeta")
public class JobTaskmeta extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobTaskmetaId;
  public void setJobTaskmetaId (int jobTaskmetaId) {
    this.jobTaskmetaId = jobTaskmetaId;
  }
  public int getJobTaskmetaId () {
    return this.jobTaskmetaId;
  }


  @Column(name="jobtaskid")
  protected int jobtaskId;
  public void setJobtaskId (int jobtaskId) {
    this.jobtaskId = jobtaskId;
  }
  public int getJobtaskId () {
    return this.jobtaskId;
  }


  @Column(name="k")
  protected String k;
  public void setK (String k) {
    this.k = k;
  }
  public String getK () {
    return this.k;
  }


  @Column(name="v")
  protected String v;
  public void setV (String v) {
    this.v = v;
  }
  public String getV () {
    return this.v;
  }


  @Column(name="position")
  protected int position;
  public void setPosition (int position) {
    this.position = position;
  }
  public int getPosition () {
    return this.position;
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
   @JoinColumn(name="jobtaskid", insertable=false, updatable=false)
  protected JobTask jobTask;
  public void setJobTask (JobTask jobTask) {
    this.jobTask = jobTask;
    this.jobtaskId = jobTask.jobTaskId;
  }
  public JobTask getJobTask () {
    return this.jobTask;
  }

}
