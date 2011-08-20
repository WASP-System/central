
/**
 *
 * ResourceMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMeta object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="resourcemeta")
public class ResourceMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int resourceMetaId;
  public void setResourceMetaId (int resourceMetaId) {
    this.resourceMetaId = resourceMetaId;
  }
  public int getResourceMetaId () {
    return this.resourceMetaId;
  }


  @Column(name="resourceid")
  protected int resourceId;
  public void setResourceId (int resourceId) {
    this.resourceId = resourceId;
  }
  public int getResourceId () {
    return this.resourceId;
  }




  @NotAudited
  @ManyToOne
   @JoinColumn(name="resourceid", insertable=false, updatable=false)
  protected Resource resource;
  public void setResource (Resource resource) {
    this.resource = resource;
    this.resourceId = resource.resourceId;
  }
  
  public Resource getResource () {
    return this.resource;
  }

}
