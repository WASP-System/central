
/**
 *
 * StateAChipseqArun.java 
 * @author echeng (table2type.pl)
 *  
 * the StateAChipseqArun object
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
@Table(name="state_a_chipseq_arun")
public class StateAChipseqArun extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int stateArunId;
  public void setStateArunId (int stateArunId) {
    this.stateArunId = stateArunId;
  }
  public int getStateArunId () {
    return this.stateArunId;
  }


  @Column(name="stateid")
  protected int stateId;
  public void setStateId (int stateId) {
    this.stateId = stateId;
  }
  public int getStateId () {
    return this.stateId;
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
  @ManyToOne
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
  @ManyToOne
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
