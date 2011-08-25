
/**
 *
 * LabPending.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPending object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;


import javax.persistence.*;
import java.util.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="labpending")
public class LabPending extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int labPendingId;
  public void setLabPendingId (int labPendingId) {
    this.labPendingId = labPendingId;
  }
  public int getLabPendingId () {
    return this.labPendingId;
  }


  @Column(name="departmentid")
  @Range(min=1)

  protected int departmentId;
  public void setDepartmentId (int departmentId) {
    this.departmentId = departmentId;
  }
  public int getDepartmentId () {
    return this.departmentId;
  }


  @Column(name="name")
  @NotEmpty
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="primaryuserid")
  protected Integer primaryUserId;
  public void setPrimaryUserId (Integer primaryUserId) {
    this.primaryUserId = primaryUserId;
  }
  public Integer getPrimaryUserId () {
    return this.primaryUserId;
  }


  @Column(name="userpendingid")
  protected Integer userpendingId;
  public void setUserpendingId (Integer userpendingId) {
    this.userpendingId = userpendingId;
  }
  public Integer getUserpendingId () {
    return this.userpendingId;
  }


  @Column(name="status")
  protected String status;
  public void setStatus (String status) {
    this.status = status;
  }
  public String getStatus () {
    return this.status;
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
  @ManyToOne
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
  @ManyToOne
   @JoinColumn(name="userpendingid", insertable=false, updatable=false)
  protected UserPending userPending;
  public void setUserPending (UserPending userPending) {
    this.userPending = userPending;
    this.userpendingId = userPending.userPendingId;
  }
  
  public UserPending getUserPending () {
    return this.userPending;
  }

  @NotAudited
  @OneToMany
   @JoinColumn(name="labpendingid", insertable=false, updatable=false)
  protected List<LabPendingMeta> labPendingMeta;
  public List<LabPendingMeta> getLabPendingMeta()  {
    return this.labPendingMeta;
  }
  public void setLabPendingMeta (List<LabPendingMeta> labPendingMeta)  {
    this.labPendingMeta = labPendingMeta;
  }



}
