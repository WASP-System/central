
/**
 *
 * Role.java 
 * @author echeng (table2type.pl)
 *  
 * the Role object
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
@Table(name="role")
public class Role extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int roleId;
  public void setRoleId (int roleId) {
    this.roleId = roleId;
  }
  public int getRoleId () {
    return this.roleId;
  }


  @Column(name="rolename")
  protected String roleName;
  public void setRoleName (String roleName) {
    this.roleName = roleName;
  }
  public String getRoleName () {
    return this.roleName;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="domain")
  protected String domain;
  public void setDomain (String domain) {
    this.domain = domain;
  }
  public String getDomain () {
    return this.domain;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="roleid", insertable=false, updatable=false)
  protected List<Userrole> userrole;
  public List<Userrole> getUserrole()  {
    return this.userrole;
  }
  public void setUserrole (List<Userrole> userrole)  {
    this.userrole = userrole;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="roleid", insertable=false, updatable=false)
  protected List<LabUser> labUser;
  public List<LabUser> getLabUser()  {
    return this.labUser;
  }
  public void setLabUser (List<LabUser> labUser)  {
    this.labUser = labUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="roleid", insertable=false, updatable=false)
  protected List<JobUser> jobUser;
  public List<JobUser> getJobUser()  {
    return this.jobUser;
  }
  public void setJobUser (List<JobUser> jobUser)  {
    this.jobUser = jobUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="roleid", insertable=false, updatable=false)
  protected List<AcctQuoteUser> acctQuoteUser;
  public List<AcctQuoteUser> getAcctQuoteUser()  {
    return this.acctQuoteUser;
  }
  public void setAcctQuoteUser (List<AcctQuoteUser> acctQuoteUser)  {
    this.acctQuoteUser = acctQuoteUser;
  }



}
