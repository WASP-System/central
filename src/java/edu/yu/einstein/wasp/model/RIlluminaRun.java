
/**
 *
 * RIlluminaRun.java 
 * @author echeng (table2type.pl)
 *  
 * the RIlluminaRun object
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
@Table(name="r_illumina_run")
public class RIlluminaRun extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int runId;
  public void setRunId (int runId) {
    this.runId = runId;
  }
  public int getRunId () {
    return this.runId;
  }


  @Column(name="resourceid")
  protected int resourceId;
  public void setResourceId (int resourceId) {
    this.resourceId = resourceId;
  }
  public int getResourceId () {
    return this.resourceId;
  }


  @Column(name="userid")
  protected int UserId;
  public void setUserId (int UserId) {
    this.UserId = UserId;
  }
  public int getUserId () {
    return this.UserId;
  }


  @Column(name="startts")
  protected Date startts;
  public void setStartts (Date startts) {
    this.startts = startts;
  }
  public Date getStartts () {
    return this.startts;
  }


  @Column(name="endts")
  protected Date enDts;
  public void setEnDts (Date enDts) {
    this.enDts = enDts;
  }
  public Date getEnDts () {
    return this.enDts;
  }


  @Column(name="status")
  protected String status;
  public void setStatus (String status) {
    this.status = status;
  }
  public String getStatus () {
    return this.status;
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
   @JoinColumn(name="resourceid", insertable=false, updatable=false)
  protected Resource resource;
  public void setResource (Resource resource) {
    this.resource = resource;
    this.resourceId = resource.resourceId;
  }
  public Resource getResource () {
    return this.resource;
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
  @OneToMany
   @JoinColumn(name="runid", insertable=false, updatable=false)
  protected List<RIlluminaRunlanesample> rIlluminaRunlanesample;
  public List<RIlluminaRunlanesample> getRIlluminaRunlanesample()  {
    return this.rIlluminaRunlanesample;
  }
  public void setRIlluminaRunlanesample (List<RIlluminaRunlanesample> rIlluminaRunlanesample)  {
    this.rIlluminaRunlanesample = rIlluminaRunlanesample;
  }



}
