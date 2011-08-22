
/**
 *
 * Roleset.java 
 * @author echeng (table2type.pl)
 *  
 * the Roleset object
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
@Table(name="roleset")
public class Roleset extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int rolesetId;
  public void setRolesetId (int rolesetId) {
    this.rolesetId = rolesetId;
  }
  public int getRolesetId () {
    return this.rolesetId;
  }


  @Column(name="parentroleid")
  protected int parentroleId;
  public void setParentroleId (int parentroleId) {
    this.parentroleId = parentroleId;
  }
  public int getParentroleId () {
    return this.parentroleId;
  }


  @Column(name="childroleid")
  protected int childroleId;
  public void setChildroleId (int childroleId) {
    this.childroleId = childroleId;
  }
  public int getChildroleId () {
    return this.childroleId;
  }



  @NotAudited
  @ManyToOne
   @JoinColumn(name="parentroleid", insertable=false, updatable=false)
  protected Role role;
  public void setRole (Role role) {
    this.role = role;
    this.parentroleId = role.roleId;
  }
  public Role getRole () {
    return this.role;
  }

  @NotAudited
  @ManyToOne
   @JoinColumn(name="childroleid", insertable=false, updatable=false)
  protected Role roleVia;
  public void setRoleVia (Role role) {
    this.role = role;
    this.childroleId = role.roleId;
  }
  public Role getRoleVia () {
    return this.role;
  }

}
