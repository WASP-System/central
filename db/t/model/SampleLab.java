
/**
 *
 * SampleLab.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLab object
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
@Table(name="samplelab")
public class SampleLab extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int sampleLabId;
  public void setSampleLabId (int sampleLabId) {
    this.sampleLabId = sampleLabId;
  }
  public int getSampleLabId () {
    return this.sampleLabId;
  }


  @Column(name="sampleid")
  protected int sampleId;
  public void setSampleId (int sampleId) {
    this.sampleId = sampleId;
  }
  public int getSampleId () {
    return this.sampleId;
  }


  @Column(name="labid")
  protected int labId;
  public void setLabId (int labId) {
    this.labId = labId;
  }
  public int getLabId () {
    return this.labId;
  }


  @Column(name="isprimary")
  protected int isPrimary;
  public void setIsPrimary (int isPrimary) {
    this.isPrimary = isPrimary;
  }
  public int getIsPrimary () {
    return this.isPrimary;
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
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected Lab lab;
  public void setLab (Lab lab) {
    this.lab = lab;
    this.labId = lab.labId;
  }
  public Lab getLab () {
    return this.lab;
  }

}
