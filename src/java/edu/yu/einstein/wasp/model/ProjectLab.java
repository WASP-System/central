
/**
 *
 * ProjectLab.java 
 * @author echeng (table2type.pl)
 *  
 * the ProjectLab object
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
@Table(name="projectlab")
public class ProjectLab extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int projectLabId;
  public void setProjectLabId (int projectLabId) {
    this.projectLabId = projectLabId;
  }
  public int getProjectLabId () {
    return this.projectLabId;
  }


  @Column(name="projectid")
  protected int projectId;
  public void setProjectId (int projectId) {
    this.projectId = projectId;
  }
  public int getProjectId () {
    return this.projectId;
  }


  @Column(name="labid")
  protected int labId;
  public void setLabId (int labId) {
    this.labId = labId;
  }
  public int getLabId () {
    return this.labId;
  }


  @Column(name="isprimary")
  protected int isPrimary;
  public void setIsPrimary (int isPrimary) {
    this.isPrimary = isPrimary;
  }
  public int getIsPrimary () {
    return this.isPrimary;
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
   @JoinColumn(name="projectid", insertable=false, updatable=false)
  protected Project project;
  public void setProject (Project project) {
    this.project = project;
    this.projectId = project.projectId;
  }
  public Project getProject () {
    return this.project;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected Lab lab;
  public void setLab (Lab lab) {
    this.lab = lab;
    this.labId = lab.labId;
  }
  public Lab getLab () {
    return this.lab;
  }

}
