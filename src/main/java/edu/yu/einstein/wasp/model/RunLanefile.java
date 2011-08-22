
/**
 *
 * RunLanefile.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLanefile object
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
@Table(name="runlanefile")
public class RunLanefile extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int runLanefileId;
  public void setRunLanefileId (int runLanefileId) {
    this.runLanefileId = runLanefileId;
  }
  public int getRunLanefileId () {
    return this.runLanefileId;
  }


  @Column(name="runlaneid")
  protected int runlaneId;
  public void setRunlaneId (int runlaneId) {
    this.runlaneId = runlaneId;
  }
  public int getRunlaneId () {
    return this.runlaneId;
  }


  @Column(name="fileid")
  protected int fileId;
  public void setFileId (int fileId) {
    this.fileId = fileId;
  }
  public int getFileId () {
    return this.fileId;
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
  @ManyToOne
   @JoinColumn(name="runlaneid", insertable=false, updatable=false)
  protected RunLane runLane;
  public void setRunLane (RunLane runLane) {
    this.runLane = runLane;
    this.runlaneId = runLane.runLaneId;
  }
  public RunLane getRunLane () {
    return this.runLane;
  }

  @NotAudited
  @ManyToOne
   @JoinColumn(name="fileid", insertable=false, updatable=false)
  protected File file;
  public void setFile (File file) {
    this.file = file;
    this.fileId = file.fileId;
  }
  public File getFile () {
    return this.file;
  }

}
