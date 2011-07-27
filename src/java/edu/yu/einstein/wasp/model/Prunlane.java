
/**
 *
 * Prunlane.java 
 * @author echeng (table2type.pl)
 *  
 * the Prunlane object
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
@Table(name="prunlane")
public class Prunlane extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int prunlaneId;
  public void setPrunlaneId (int prunlaneId) {
    this.prunlaneId = prunlaneId;
  }
  public int getPrunlaneId () {
    return this.prunlaneId;
  }


  @Column(name="pid")
  protected int pId;
  public void setPId (int pId) {
    this.pId = pId;
  }
  public int getPId () {
    return this.pId;
  }


  @Column(name="runlaneid")
  protected int runlaneId;
  public void setRunlaneId (int runlaneId) {
    this.runlaneId = runlaneId;
  }
  public int getRunlaneId () {
    return this.runlaneId;
  }



  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected P p;
  public void setP (P p) {
    this.p = p;
    this.pId = p.pId;
  }
  public P getP () {
    return this.p;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="runlaneid", insertable=false, updatable=false)
  protected RunLane runLane;
  public void setRunLane (RunLane runLane) {
    this.runLane = runLane;
    this.runlaneId = runLane.runLaneId;
  }
  public RunLane getRunLane () {
    return this.runLane;
  }

}
