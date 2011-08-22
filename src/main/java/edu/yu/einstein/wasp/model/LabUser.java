
/**
 *
 * LabUser.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUser object
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
@Table(name="labuser")
public class LabUser extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int labUserId;
  public void setLabUserId (int labUserId) {
    this.labUserId = labUserId;
  }
  public int getLabUserId () {
    return this.labUserId;
  }


  @Column(name="labid")
  protected int labId;
  public void setLabId (int labId) {
    this.labId = labId;
  }
  public int getLabId () {
    return this.labId;
  }


  @Column(name="userid")
  protected int UserId;
  public void setUserId (int UserId) {
    this.UserId = UserId;
  }
  public int getUserId () {
    return this.UserId;
  }


  @Column(name="roleid")
  protected int roleId;
  public void setRoleId (int roleId) {
    this.roleId = roleId;
  }
  public int getRoleId () {
    return this.roleId;
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
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected Lab lab;
  public void setLab (Lab lab) {
    this.lab = lab;
    this.labId = lab.labId;
  }
  public Lab getLab () {
    return this.lab;
  }

  @NotAudited
  @ManyToOne
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
  @ManyToOne
   @JoinColumn(name="roleid", insertable=false, updatable=false)
  protected Role role;
  public void setRole (Role role) {
    this.role = role;
    this.roleId = role.roleId;
  }
  public Role getRole () {
    return this.role;
  }

}
