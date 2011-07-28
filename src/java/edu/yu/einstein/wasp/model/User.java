
/**
 *
 * User.java 
 * @author echeng (table2type.pl)
 *  
 * the User object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
@Table(name="user")
public class User extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int UserId;
  public void setUserId (int UserId) {
    this.UserId = UserId;
  }
  public int getUserId () {
    return this.UserId;
  }


  @Column(name="login")
  protected String login;
  
  public void setLogin (String login) {
    this.login = login;
  }
  public String getLogin () {
    return this.login;
  }


  @Column(name="email")  
  @Pattern(regexp=".+@.+\\.[a-z]+")
  protected String email;
  public void setEmail (String email) {
    this.email = email;
  }
  public String getEmail () {
    return this.email;
  }


  @Column(name="password")  
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
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected List<Usermeta> usermeta;
  public List<Usermeta> getUsermeta()  {
    return this.usermeta;
  }
  public void setUsermeta (List<Usermeta> usermeta)  {
    this.usermeta = usermeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected List<Userrole> userrole;
  public List<Userrole> getUserrole()  {
    return this.userrole;
  }
  public void setUserrole (List<Userrole> userrole)  {
    this.userrole = userrole;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected List<DepartmentUser> departmentUser;
  public List<DepartmentUser> getDepartmentUser()  {
    return this.departmentUser;
  }
  public void setDepartmentUser (List<DepartmentUser> departmentUser)  {
    this.departmentUser = departmentUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="primaryuserid", insertable=false, updatable=false)
  protected List<Lab> lab;
  public List<Lab> getLab()  {
    return this.lab;
  }
  public void setLab (List<Lab> lab)  {
    this.lab = lab;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected List<LabUser> labUser;
  public List<LabUser> getLabUser()  {
    return this.labUser;
  }
  public void setLabUser (List<LabUser> labUser)  {
    this.labUser = labUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected List<Job> job;
  public List<Job> getJob()  {
    return this.job;
  }
  public void setJob (List<Job> job)  {
    this.job = job;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="submitter_userid", insertable=false, updatable=false)
  protected List<Sample> sample;
  public List<Sample> getSample()  {
    return this.sample;
  }
  public void setSample (List<Sample> sample)  {
    this.sample = sample;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected List<AcctQuote> acctQuote;
  public List<AcctQuote> getAcctQuote()  {
    return this.acctQuote;
  }
  public void setAcctQuote (List<AcctQuote> acctQuote)  {
    this.acctQuote = acctQuote;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected List<AcctQuoteUser> acctQuoteUser;
  public List<AcctQuoteUser> getAcctQuoteUser()  {
    return this.acctQuoteUser;
  }
  public void setAcctQuoteUser (List<AcctQuoteUser> acctQuoteUser)  {
    this.acctQuoteUser = acctQuoteUser;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected List<Run> run;
  public List<Run> getRun()  {
    return this.run;
  }
  public void setRun (List<Run> run)  {
    this.run = run;
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
  
}
