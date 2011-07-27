
/**
 *
 * Workflowmeta.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowmeta object
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
@Table(name="workflowmeta")
public class Workflowmeta extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int workflowmetaId;
  public void setWorkflowmetaId (int workflowmetaId) {
    this.workflowmetaId = workflowmetaId;
  }
  public int getWorkflowmetaId () {
    return this.workflowmetaId;
  }


  @Column(name="workflowid")
  protected int workflowId;
  public void setWorkflowId (int workflowId) {
    this.workflowId = workflowId;
  }
  public int getWorkflowId () {
    return this.workflowId;
  }


  @Column(name="k")
  protected String k;
  public void setK (String k) {
    this.k = k;
  }
  public String getK () {
    return this.k;
  }


  @Column(name="v")
  protected String v;
  public void setV (String v) {
    this.v = v;
  }
  public String getV () {
    return this.v;
  }


  @Column(name="position")
  protected int position;
  public void setPosition (int position) {
    this.position = position;
  }
  public int getPosition () {
    return this.position;
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
   @JoinColumn(name="workflowid", insertable=false, updatable=false)
  protected Workflow workflow;
  public void setWorkflow (Workflow workflow) {
    this.workflow = workflow;
    this.workflowId = workflow.workflowId;
  }
  public Workflow getWorkflow () {
    return this.workflow;
  }

}
