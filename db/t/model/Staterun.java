
/**
 *
 * Staterun.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterun object
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
@Table(name="staterun")
public class Staterun extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int staterunId;
  public void setStaterunId (int staterunId) {
    this.staterunId = staterunId;
  }
  public int getStaterunId () {
    return this.staterunId;
  }


  @Column(name="stateid")
  protected int stateId;
  public void setStateId (int stateId) {
    this.stateId = stateId;
  }
  public int getStateId () {
    return this.stateId;
  }


  @Column(name="runid")
  protected int runId;
  public void setRunId (int runId) {
    this.runId = runId;
  }
  public int getRunId () {
    return this.runId;
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
   @JoinColumn(name="runid", insertable=false, updatable=false)
  protected Run run;
  public void setRun (Run run) {
    this.run = run;
    this.runId = run.runId;
  }
  public Run getRun () {
    return this.run;
  }

}
