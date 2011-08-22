
/**
 *
 * Department.java 
 * @author echeng (table2type.pl)
 *  
 * the Department object
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
@Table(name="department")
public class Department extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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


  @Column(name="isinternal")
  protected int isInternal;
  public void setIsInternal (int isInternal) {
    this.isInternal = isInternal;
  }
  public int getIsInternal () {
    return this.isInternal;
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
  @OneToMany
   @JoinColumn(name="departmentid", insertable=false, updatable=false)
  protected List<DepartmentUser> departmentUser;
  public List<DepartmentUser> getDepartmentUser()  {
    return this.departmentUser;
  }
  public void setDepartmentUser (List<DepartmentUser> departmentUser)  {
    this.departmentUser = departmentUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="departmentid", insertable=false, updatable=false)
  protected List<Lab> lab;
  public List<Lab> getLab()  {
    return this.lab;
  }
  public void setLab (List<Lab> lab)  {
    this.lab = lab;
  }



}
