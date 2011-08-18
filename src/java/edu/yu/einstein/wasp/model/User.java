
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
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
  @NotEmpty
  protected String login;
  
  public void setLogin (String login) {
    this.login = login;
  }
  public String getLogin () {
    return this.login;
  }


  @Column(name="email")
  @Email
  //@Pattern(regexp=".+@.+\\.[a-z]+")
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
  @JsonIgnore 
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
  @JsonIgnore 
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
  @JsonIgnore
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
  @JsonIgnore 
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
  @JsonIgnore 
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
  @JsonIgnore 
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
  @JsonIgnore 
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
  @JsonIgnore 
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
  @JsonIgnore 
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
  @JsonIgnore 
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
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + UserId;
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	User other = (User) obj;
	if (UserId != other.UserId)
		return false;
	return true;
}
@Override
public String toString() {
	return "User [UserId=" + UserId + ", login=" + login + ", email=" + email
			+ ", password=" + password + ", firstName=" + firstName
			+ ", lastName=" + lastName + ", isActive=" + isActive
			+ ", lastUpdTs=" + lastUpdTs + ", lastUpdUser=" + lastUpdUser
			+ ", usermeta=" + usermeta + ", departmentUser=" + departmentUser
			+ ", lab=" + lab + ", job=" + job + ", sample=" + sample
			+ ", acctQuote=" + acctQuote + ", acctQuoteUser=" + acctQuoteUser
			+ ", locale=" + locale + "]";
}
  
  
  
}
