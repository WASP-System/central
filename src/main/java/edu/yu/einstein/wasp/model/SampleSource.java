
/**
 *
 * SampleSource.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSource object
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
@Table(name="samplesource")
public class SampleSource extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int sampleSourceId;
  public void setSampleSourceId (int sampleSourceId) {
    this.sampleSourceId = sampleSourceId;
  }
  public int getSampleSourceId () {
    return this.sampleSourceId;
  }


  @Column(name="sampleid")
  protected int sampleId;
  public void setSampleId (int sampleId) {
    this.sampleId = sampleId;
  }
  public int getSampleId () {
    return this.sampleId;
  }


  @Column(name="multiplexindex")
  protected int multiplexindex;
  public void setMultiplexindex (int multiplexindex) {
    this.multiplexindex = multiplexindex;
  }
  public int getMultiplexindex () {
    return this.multiplexindex;
  }


  @Column(name="source_sampleid")
  protected int sourceSampleId;
  public void setSourceSampleId (int sourceSampleId) {
    this.sourceSampleId = sourceSampleId;
  }
  public int getSourceSampleId () {
    return this.sourceSampleId;
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
  @ManyToOne
   @JoinColumn(name="source_sampleid", insertable=false, updatable=false)
  protected Sample sampleVia;
  public void setSampleVia (Sample sample) {
    this.sample = sample;
    this.sourceSampleId = sample.sampleId;
  }
  public Sample getSampleVia () {
    return this.sample;
  }

}
