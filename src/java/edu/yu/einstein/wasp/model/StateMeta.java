
/**
 *
 * StateMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the StateMeta object
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
@Table(name="statemeta")
public class StateMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int stateMetaId;
  public void setStateMetaId (int stateMetaId) {
    this.stateMetaId = stateMetaId;
  }
  public int getStateMetaId () {
    return this.stateMetaId;
  }


  @Column(name="stateid")
  protected int stateId;
  public void setStateId (int stateId) {
    this.stateId = stateId;
  }
  public int getStateId () {
    return this.stateId;
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

}
