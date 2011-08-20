
/**
 *
 * WorkflowMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowMeta object
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
@Table(name="workflowmeta")
public class WorkflowMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int workflowMetaId;
  public void setWorkflowMetaId (int workflowMetaId) {
    this.workflowMetaId = workflowMetaId;
  }
  public int getWorkflowMetaId () {
    return this.workflowMetaId;
  }


  @Column(name="workflowid")
  protected int workflowId;
  public void setWorkflowId (int workflowId) {
    this.workflowId = workflowId;
  }
  public int getWorkflowId () {
    return this.workflowId;
  }

  @NotAudited
  @ManyToOne
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
