
/**
 *
 * Psample.java 
 * @author echeng (table2type.pl)
 *  
 * the Psample object
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
@Table(name="psample")
public class Psample extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int psampleId;
  public void setPsampleId (int psampleId) {
    this.psampleId = psampleId;
  }
  public int getPsampleId () {
    return this.psampleId;
  }


  @Column(name="pid")
  protected int pId;
  public void setPId (int pId) {
    this.pId = pId;
  }
  public int getPId () {
    return this.pId;
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
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected P p;
  public void setP (P p) {
    this.p = p;
    this.pId = p.pId;
  }
  public P getP () {
    return this.p;
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

}
