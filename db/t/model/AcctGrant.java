
/**
 *
 * AcctGrant.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrant object
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
@Table(name="acct_grant")
public class AcctGrant extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int grantId;
  public void setGrantId (int grantId) {
    this.grantId = grantId;
  }
  public int getGrantId () {
    return this.grantId;
  }


  @Column(name="labid")
  protected int labId;
  public void setLabId (int labId) {
    this.labId = labId;
  }
  public int getLabId () {
    return this.labId;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="code")
  protected String code;
  public void setCode (String code) {
    this.code = code;
  }
  public String getCode () {
    return this.code;
  }


  @Column(name="expirationdt")
  protected Date expirationdt;
  public void setExpirationdt (Date expirationdt) {
    this.expirationdt = expirationdt;
  }
  public Date getExpirationdt () {
    return this.expirationdt;
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
  @ManyToOne(cascade = CascadeType.ALL)
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
   @JoinColumn(name="grantid", insertable=false, updatable=false)
  protected List<AcctGrantjob> acctGrantjob;
  public List<AcctGrantjob> getAcctGrantjob()  {
    return this.acctGrantjob;
  }
  public void setAcctGrantjob (List<AcctGrantjob> acctGrantjob)  {
    this.acctGrantjob = acctGrantjob;
  }



}
