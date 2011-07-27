
/**
 *
 * TypeSample.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSample object
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
@Table(name="typesample")
public class TypeSample extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int typeSampleId;
  public void setTypeSampleId (int typeSampleId) {
    this.typeSampleId = typeSampleId;
  }
  public int getTypeSampleId () {
    return this.typeSampleId;
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
   @JoinColumn(name="typesampleid", insertable=false, updatable=false)
  protected List<Sample> sample;
  public List<Sample> getSample()  {
    return this.sample;
  }
  public void setSample (List<Sample> sample)  {
    this.sample = sample;
  }



}
