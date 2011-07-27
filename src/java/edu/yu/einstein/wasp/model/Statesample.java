
/**
 *
 * Statesample.java 
 * @author echeng (table2type.pl)
 *  
 * the Statesample object
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
@Table(name="statesample")
public class Statesample extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int statesampleId;
  public void setStatesampleId (int statesampleId) {
    this.statesampleId = statesampleId;
  }
  public int getStatesampleId () {
    return this.statesampleId;
  }


  @Column(name="stateid")
  protected int stateId;
  public void setStateId (int stateId) {
    this.stateId = stateId;
  }
  public int getStateId () {
    return this.stateId;
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
   @JoinColumn(name="stateid", insertable=false, updatable=false)
  protected State state;
  public void setState (State state) {
    this.state = state;
    this.stateId = state.stateId;
  }
  public State getState () {
    return this.state;
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
