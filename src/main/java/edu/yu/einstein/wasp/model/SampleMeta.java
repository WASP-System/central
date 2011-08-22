
/**
 *
 * SampleMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMeta object
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
@Table(name="samplemeta")
public class SampleMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int sampleMetaId;
  public void setSampleMetaId (int sampleMetaId) {
    this.sampleMetaId = sampleMetaId;
  }
  public int getSampleMetaId () {
    return this.sampleMetaId;
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
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected Sample sample;
  public void setSample (Sample sample) {
    this.sample = sample;
    this.sampleId = sample.sampleId;
  }
  
  public Sample getSample () {
    return this.sample;
  }

}
