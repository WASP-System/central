
/**
 *
 * Workflowtasksource.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtasksource object
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
@Table(name="workflowtasksource")
public class Workflowtasksource extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int workflowtasksourceId;
  public void setWorkflowtasksourceId (int workflowtasksourceId) {
    this.workflowtasksourceId = workflowtasksourceId;
  }
  public int getWorkflowtasksourceId () {
    return this.workflowtasksourceId;
  }


  @Column(name="workflowtaskid")
  protected int workflowtaskId;
  public void setWorkflowtaskId (int workflowtaskId) {
    this.workflowtaskId = workflowtaskId;
  }
  public int getWorkflowtaskId () {
    return this.workflowtaskId;
  }


  @Column(name="sourceworkflowtaskid")
  protected int sourceworkflowtaskId;
  public void setSourceworkflowtaskId (int sourceworkflowtaskId) {
    this.sourceworkflowtaskId = sourceworkflowtaskId;
  }
  public int getSourceworkflowtaskId () {
    return this.sourceworkflowtaskId;
  }



}
