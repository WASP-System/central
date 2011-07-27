
/**
 *
 * P.java 
 * @author echeng (table2type.pl)
 *  
 * the P object
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
@Table(name="p")
public class P extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int pId;
  public void setPId (int pId) {
    this.pId = pId;
  }
  public int getPId () {
    return this.pId;
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
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected List<Pmeta> pmeta;
  public List<Pmeta> getPmeta()  {
    return this.pmeta;
  }
  public void setPmeta (List<Pmeta> pmeta)  {
    this.pmeta = pmeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected List<Pjob> pjob;
  public List<Pjob> getPjob()  {
    return this.pjob;
  }
  public void setPjob (List<Pjob> pjob)  {
    this.pjob = pjob;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected List<Psample> psample;
  public List<Psample> getPsample()  {
    return this.psample;
  }
  public void setPsample (List<Psample> psample)  {
    this.psample = psample;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected List<Prun> prun;
  public List<Prun> getPrun()  {
    return this.prun;
  }
  public void setPrun (List<Prun> prun)  {
    this.prun = prun;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected List<Prunlane> prunlane;
  public List<Prunlane> getPrunlane()  {
    return this.prunlane;
  }
  public void setPrunlane (List<Prunlane> prunlane)  {
    this.prunlane = prunlane;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected List<PAChipseqArun> pAChipseqArun;
  public List<PAChipseqArun> getPAChipseqArun()  {
    return this.pAChipseqArun;
  }
  public void setPAChipseqArun (List<PAChipseqArun> pAChipseqArun)  {
    this.pAChipseqArun = pAChipseqArun;
  }



}
