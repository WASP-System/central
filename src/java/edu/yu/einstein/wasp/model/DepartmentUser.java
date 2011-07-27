
/**
 *
 * DepartmentUser.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUser object
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
@Table(name="departmentuser")
public class DepartmentUser extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int departmentUserId;
  public void setDepartmentUserId (int departmentUserId) {
    this.departmentUserId = departmentUserId;
  }
  public int getDepartmentUserId () {
    return this.departmentUserId;
  }


  @Column(name="departmentid")
  protected int departmentId;
  public void setDepartmentId (int departmentId) {
    this.departmentId = departmentId;
  }
  public int getDepartmentId () {
    return this.departmentId;
  }


  @Column(name="userid")
  protected int UserId;
  public void setUserId (int UserId) {
    this.UserId = UserId;
  }
  public int getUserId () {
    return this.UserId;
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
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected User user;
  public void setUser (User user) {
    this.user = user;
    this.UserId = user.UserId;
  }
  public User getUser () {
    return this.user;
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

}
