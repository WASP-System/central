
/**
 *
 * TypeResource.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResource object
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
@Table(name="typeresource")
public class TypeResource extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int typeResourceId;
  public void setTypeResourceId (int typeResourceId) {
    this.typeResourceId = typeResourceId;
  }
  public int getTypeResourceId () {
    return this.typeResourceId;
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
  @OneToMany
   @JoinColumn(name="typeresourceid", insertable=false, updatable=false)
  protected List<Resource> resource;
  public List<Resource> getResource()  {
    return this.resource;
  }
  public void setResource (List<Resource> resource)  {
    this.resource = resource;
  }



}
