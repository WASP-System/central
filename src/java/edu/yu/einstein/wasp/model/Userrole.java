
/**
 *
 * Userrole.java 
 * @author echeng (table2type.pl)
 *  
 * the Userrole object
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
@Table(name="userrole")
public class Userrole extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int userroleId;
  public void setUserroleId (int userroleId) {
    this.userroleId = userroleId;
  }
  public int getUserroleId () {
    return this.userroleId;
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
   @JoinColumn(name="roleid", insertable=false, updatable=false)
  protected Role role;
  public void setRole (Role role) {
    this.role = role;
    this.roleId = role.roleId;
  }
  public Role getRole () {
    return this.role;
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

}
