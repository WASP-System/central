
/**
 *
 * Task.java 
 * @author echeng (table2type.pl)
 *  
 * the Task object
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
@Table(name="task")
public class Task extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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


  @NotAudited
  @OneToMany
   @JoinColumn(name="taskid", insertable=false, updatable=false)
  protected List<State> state;
  public List<State> getState()  {
    return this.state;
  }
  public void setState (List<State> state)  {
    this.state = state;
  }



}
