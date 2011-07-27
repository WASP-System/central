
/**
 *
 * State.java 
 * @author echeng (table2type.pl)
 *  
 * the State object
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
@Table(name="state")
public class State extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int stateId;
  public void setStateId (int stateId) {
    this.stateId = stateId;
  }
  public int getStateId () {
    return this.stateId;
  }


  @Column(name="taskid")
  protected int taskId;
  public void setTaskId (int taskId) {
    this.taskId = taskId;
  }
  public int getTaskId () {
    return this.taskId;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="status")
  protected String status;
  public void setStatus (String status) {
    this.status = status;
  }
  public String getStatus () {
    return this.status;
  }


  @Column(name="startts")
  protected Date startts;
  public void setStartts (Date startts) {
    this.startts = startts;
  }
  public Date getStartts () {
    return this.startts;
  }


  @Column(name="endts")
  protected Date enDts;
  public void setEnDts (Date enDts) {
    this.enDts = enDts;
  }
  public Date getEnDts () {
    return this.enDts;
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
   @JoinColumn(name="taskid", insertable=false, updatable=false)
  protected Task task;
  public void setTask (Task task) {
    this.task = task;
    this.taskId = task.taskId;
  }
  public Task getTask () {
    return this.task;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="stateid", insertable=false, updatable=false)
  protected List<Statemeta> statemeta;
  public List<Statemeta> getStatemeta()  {
    return this.statemeta;
  }
  public void setStatemeta (List<Statemeta> statemeta)  {
    this.statemeta = statemeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="stateid", insertable=false, updatable=false)
  protected List<Statejob> statejob;
  public List<Statejob> getStatejob()  {
    return this.statejob;
  }
  public void setStatejob (List<Statejob> statejob)  {
    this.statejob = statejob;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="stateid", insertable=false, updatable=false)
  protected List<Statesample> statesample;
  public List<Statesample> getStatesample()  {
    return this.statesample;
  }
  public void setStatesample (List<Statesample> statesample)  {
    this.statesample = statesample;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="stateid", insertable=false, updatable=false)
  protected List<Staterun> staterun;
  public List<Staterun> getStaterun()  {
    return this.staterun;
  }
  public void setStaterun (List<Staterun> staterun)  {
    this.staterun = staterun;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="stateid", insertable=false, updatable=false)
  protected List<Staterunlane> staterunlane;
  public List<Staterunlane> getStaterunlane()  {
    return this.staterunlane;
  }
  public void setStaterunlane (List<Staterunlane> staterunlane)  {
    this.staterunlane = staterunlane;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="stateid", insertable=false, updatable=false)
  protected List<StateAChipseqArun> stateAChipseqArun;
  public List<StateAChipseqArun> getStateAChipseqArun()  {
    return this.stateAChipseqArun;
  }
  public void setStateAChipseqArun (List<StateAChipseqArun> stateAChipseqArun)  {
    this.stateAChipseqArun = stateAChipseqArun;
  }



}
