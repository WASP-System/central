
/**
 *
 * JobSampleMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleMeta object
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
@Table(name="jobsamplemeta")
public class JobSampleMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobSampleMetaId;
  public void setJobSampleMetaId (int jobSampleMetaId) {
    this.jobSampleMetaId = jobSampleMetaId;
  }
  public int getJobSampleMetaId () {
    return this.jobSampleMetaId;
  }


  @Column(name="jobsampleid")
  protected int jobsampleId;
  public void setJobsampleId (int jobsampleId) {
    this.jobsampleId = jobsampleId;
  }
  public int getJobsampleId () {
    return this.jobsampleId;
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
