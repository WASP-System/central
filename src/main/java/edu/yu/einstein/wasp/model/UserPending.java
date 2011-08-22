
/**
 *
 * UserPending.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPending object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="userpending")
public class UserPending extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int userPendingId;
  public void setUserPendingId (int userPendingId) {
    this.userPendingId = userPendingId;
  }
  public int getUserPendingId () {
    return this.userPendingId;
  }


  @Column(name="email")
  @NotEmpty
  @Email
  protected String email;
  public void setEmail (String email) {
    this.email = email;
  }
  public String getEmail () {
    return this.email;
  }


  @Column(name="password")
  @NotEmpty
  protected String password;
  public void setPassword (String password) {
    this.password = password;
  }
  public String getPassword () {
    return this.password;
  }


  @Column(name="firstname")
  @NotEmpty
  protected String firstName;
  public void setFirstName (String firstName) {
    this.firstName = firstName;
  }
  public String getFirstName () {
    return this.firstName;
  }


  @Column(name="lastname")
  @NotEmpty
  protected String lastName;
  public void setLastName (String lastName) {
    this.lastName = lastName;
  }
  public String getLastName () {
    return this.lastName;
  }


  @Column(name="locale")
  @NotEmpty
  protected String locale;
  public void setLocale (String locale) {
    this.locale = locale;
  }
  public String getLocale () {
    return this.locale;
  }


  @Column(name="labid")
  protected Integer labId;
  public void setLabId (Integer labId) {
    this.labId = labId;
  }
  public Integer getLabId () {
    return this.labId;
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
  @OneToMany
   @JoinColumn(name="userpendingid", insertable=false, updatable=false)
  protected List<UserPendingMeta> userPendingMeta;
  public List<UserPendingMeta> getUserPendingMeta()  {
    return this.userPendingMeta;
  }
  public void setUserPendingMeta (List<UserPendingMeta> userPendingMeta)  {
    this.userPendingMeta = userPendingMeta;
  }



}
