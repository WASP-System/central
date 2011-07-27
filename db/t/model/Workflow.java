
/**
 *
 * Workflow.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflow object
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
@Table(name="workflow")
public class Workflow extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int workflowId;
  public void setWorkflowId (int workflowId) {
    this.workflowId = workflowId;
  }
  public int getWorkflowId () {
    return this.workflowId;
  }


  @Column(name="iname")
  protected String iName;
  public void setIName (String iName) {
    this.iName = iName;
  }
  public String getIName () {
    return this.iName;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="createts")
  protected Date createts;
  public void setCreatets (Date createts) {
    this.createts = createts;
  }
  public Date getCreatets () {
    return this.createts;
  }


  @Column(name="isactive")
  protected int isActive;
  public void setIsActive (int isActive) {
    this.isActive = isActive;
  }
  public int getIsActive () {
    return this.isActive;
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
  @OneToMany
   @JoinColumn(name="workflowid", insertable=false, updatable=false)
  protected List<Workflowmeta> workflowmeta;
  public List<Workflowmeta> getWorkflowmeta()  {
    return this.workflowmeta;
  }
  public void setWorkflowmeta (List<Workflowmeta> workflowmeta)  {
    this.workflowmeta = workflowmeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="workflowid", insertable=false, updatable=false)
  protected List<Job> job;
  public List<Job> getJob()  {
    return this.job;
  }
  public void setJob (List<Job> job)  {
    this.job = job;
  }



}
