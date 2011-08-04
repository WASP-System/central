
/**
 *
 * JobSamplemeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSamplemeta object
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
@Table(name="jobsamplemeta")
public class JobSamplemeta extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobSamplemetaId;
  public void setJobSamplemetaId (int jobSamplemetaId) {
    this.jobSamplemetaId = jobSamplemetaId;
  }
  public int getJobSamplemetaId () {
    return this.jobSamplemetaId;
  }


  @Column(name="jobsampleid")
  protected int jobsampleId;
  public void setJobsampleId (int jobsampleId) {
    this.jobsampleId = jobsampleId;
  }
  public int getJobsampleId () {
    return this.jobsampleId;
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
  @ManyToOne
   @JoinColumn(name="jobsampleid", insertable=false, updatable=false)
  protected JobSample jobSample;
  public void setJobSample (JobSample jobSample) {
    this.jobSample = jobSample;
    this.jobsampleId = jobSample.jobSampleId;
  }
  public JobSample getJobSample () {
    return this.jobSample;
  }

}
