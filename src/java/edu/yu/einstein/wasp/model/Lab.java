
/**
 *
 * Lab.java 
 * @author echeng (table2type.pl)
 *  
 * the Lab object
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
@Table(name="lab")
public class Lab extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int labId;
  public void setLabId (int labId) {
    this.labId = labId;
  }
  public int getLabId () {
    return this.labId;
  }


  @Column(name="departmentid")
  protected int departmentId;
  public void setDepartmentId (int departmentId) {
    this.departmentId = departmentId;
  }
  public int getDepartmentId () {
    return this.departmentId;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="primaryuserid")
  protected int primaryUserId;
  public void setPrimaryUserId (int primaryUserId) {
    this.primaryUserId = primaryUserId;
  }
  public int getPrimaryUserId () {
    return this.primaryUserId;
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
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="departmentid", insertable=false, updatable=false)
  protected Department department;
  public void setDepartment (Department department) {
    this.department = department;
    this.departmentId = department.departmentId;
  }
  public Department getDepartment () {
    return this.department;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="primaryuserid", insertable=false, updatable=false)
  protected User user;
  public void setUser (User user) {
    this.user = user;
    this.primaryUserId = user.UserId;
  }
  public User getUser () {
    return this.user;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected List<Labmeta> labmeta;
  public List<Labmeta> getLabmeta()  {
    return this.labmeta;
  }
  public void setLabmeta (List<Labmeta> labmeta)  {
    this.labmeta = labmeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected List<LabUser> labUser;
  public List<LabUser> getLabUser()  {
    return this.labUser;
  }
  public void setLabUser (List<LabUser> labUser)  {
    this.labUser = labUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected List<Job> job;
  public List<Job> getJob()  {
    return this.job;
  }
  public void setJob (List<Job> job)  {
    this.job = job;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected List<Project> project;
  public List<Project> getProject()  {
    return this.project;
  }
  public void setProject (List<Project> project)  {
    this.project = project;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="submitter_labid", insertable=false, updatable=false)
  protected List<Sample> sample;
  public List<Sample> getSample()  {
    return this.sample;
  }
  public void setSample (List<Sample> sample)  {
    this.sample = sample;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected List<SampleLab> sampleLab;
  public List<SampleLab> getSampleLab()  {
    return this.sampleLab;
  }
  public void setSampleLab (List<SampleLab> sampleLab)  {
    this.sampleLab = sampleLab;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected List<AcctGrant> acctGrant;
  public List<AcctGrant> getAcctGrant()  {
    return this.acctGrant;
  }
  public void setAcctGrant (List<AcctGrant> acctGrant)  {
    this.acctGrant = acctGrant;
  }



}
