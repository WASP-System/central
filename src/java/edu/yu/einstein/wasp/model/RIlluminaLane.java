
/**
 *
 * RIlluminaLane.java 
 * @author echeng (table2type.pl)
 *  
 * the RIlluminaLane object
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
@Table(name="r_illumina_lane")
public class RIlluminaLane extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int laneId;
  public void setLaneId (int laneId) {
    this.laneId = laneId;
  }
  public int getLaneId () {
    return this.laneId;
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
   @JoinColumn(name="laneid", insertable=false, updatable=false)
  protected List<RIlluminaRunlanesample> rIlluminaRunlanesample;
  public List<RIlluminaRunlanesample> getRIlluminaRunlanesample()  {
    return this.rIlluminaRunlanesample;
  }
  public void setRIlluminaRunlanesample (List<RIlluminaRunlanesample> rIlluminaRunlanesample)  {
    this.rIlluminaRunlanesample = rIlluminaRunlanesample;
  }



}
