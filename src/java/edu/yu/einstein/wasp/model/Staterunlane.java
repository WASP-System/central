
/**
 *
 * Staterunlane.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterunlane object
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
@Table(name="staterunlane")
public class Staterunlane extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int staterunlaneId;
  public void setStaterunlaneId (int staterunlaneId) {
    this.staterunlaneId = staterunlaneId;
  }
  public int getStaterunlaneId () {
    return this.staterunlaneId;
  }


  @Column(name="stateid")
  protected int stateId;
  public void setStateId (int stateId) {
    this.stateId = stateId;
  }
  public int getStateId () {
    return this.stateId;
  }


  @Column(name="runlaneid")
  protected int runlaneId;
  public void setRunlaneId (int runlaneId) {
    this.runlaneId = runlaneId;
  }
  public int getRunlaneId () {
    return this.runlaneId;
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
   @JoinColumn(name="runlaneid", insertable=false, updatable=false)
  protected RunLane runLane;
  public void setRunLane (RunLane runLane) {
    this.runLane = runLane;
    this.runlaneId = runLane.runLaneId;
  }
  public RunLane getRunLane () {
    return this.runLane;
  }

}
