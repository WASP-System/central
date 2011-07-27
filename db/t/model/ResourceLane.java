
/**
 *
 * ResourceLane.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLane object
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
@Table(name="resourcelane")
public class ResourceLane extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int resourceLaneId;
  public void setResourceLaneId (int resourceLaneId) {
    this.resourceLaneId = resourceLaneId;
  }
  public int getResourceLaneId () {
    return this.resourceLaneId;
  }


  @Column(name="resourceid")
  protected int resourceId;
  public void setResourceId (int resourceId) {
    this.resourceId = resourceId;
  }
  public int getResourceId () {
    return this.resourceId;
  }


  @Column(name="iname")
  protected String iName;
  public void setIName (String iName) {
    this.iName = iName;
  }
  public String getIName () {
    return this.iName;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
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
  @OneToMany
   @JoinColumn(name="resourcelaneid", insertable=false, updatable=false)
  protected List<RunLane> runLane;
  public List<RunLane> getRunLane()  {
    return this.runLane;
  }
  public void setRunLane (List<RunLane> runLane)  {
    this.runLane = runLane;
  }



}
