
/**
 *
 * Resource.java 
 * @author echeng (table2type.pl)
 *  
 * the Resource object
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
@Table(name="resource")
public class Resource extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int resourceId;
  public void setResourceId (int resourceId) {
    this.resourceId = resourceId;
  }
  public int getResourceId () {
    return this.resourceId;
  }


  @Column(name="platform")
  protected String platform;
  public void setPlatform (String platform) {
    this.platform = platform;
  }
  public String getPlatform () {
    return this.platform;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="typeresourceid")
  protected int typeResourceId;
  public void setTypeResourceId (int typeResourceId) {
    this.typeResourceId = typeResourceId;
  }
  public int getTypeResourceId () {
    return this.typeResourceId;
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
  @ManyToOne
   @JoinColumn(name="typeresourceid", insertable=false, updatable=false)
  protected TypeResource typeResource;
  public void setTypeResource (TypeResource typeResource) {
    this.typeResource = typeResource;
    this.typeResourceId = typeResource.typeResourceId;
  }
  public TypeResource getTypeResource () {
    return this.typeResource;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="resourceid", insertable=false, updatable=false)
  protected List<ResourceMeta> resourceMeta;
  public List<ResourceMeta> getResourceMeta()  {
    return this.resourceMeta;
  }
  public void setResourceMeta (List<ResourceMeta> resourceMeta)  {
    this.resourceMeta = resourceMeta;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="resourceid", insertable=false, updatable=false)
  protected List<ResourceBarcode> resourceBarcode;
  public List<ResourceBarcode> getResourceBarcode()  {
    return this.resourceBarcode;
  }
  public void setResourceBarcode (List<ResourceBarcode> resourceBarcode)  {
    this.resourceBarcode = resourceBarcode;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="resourceid", insertable=false, updatable=false)
  protected List<ResourceLane> resourceLane;
  public List<ResourceLane> getResourceLane()  {
    return this.resourceLane;
  }
  public void setResourceLane (List<ResourceLane> resourceLane)  {
    this.resourceLane = resourceLane;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="resourceid", insertable=false, updatable=false)
  protected List<Run> run;
  public List<Run> getRun()  {
    return this.run;
  }
  public void setRun (List<Run> run)  {
    this.run = run;
  }



}
