
/**
 *
 * Workflowtask.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtask object
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
@Table(name="workflowtask")
public class Workflowtask extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int workflowtaskId;
  public void setWorkflowtaskId (int workflowtaskId) {
    this.workflowtaskId = workflowtaskId;
  }
  public int getWorkflowtaskId () {
    return this.workflowtaskId;
  }


  @Column(name="workflowid")
  protected int workflowId;
  public void setWorkflowId (int workflowId) {
    this.workflowId = workflowId;
  }
  public int getWorkflowId () {
    return this.workflowId;
  }


  @Column(name="taskid")
  protected int taskId;
  public void setTaskId (int taskId) {
    this.taskId = taskId;
  }
  public int getTaskId () {
    return this.taskId;
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



}
