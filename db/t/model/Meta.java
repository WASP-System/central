
/**
 *
 * Meta.java 
 * @author echeng (table2type.pl)
 *  
 * the Meta object
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
@Table(name="meta")
public class Meta extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int metaId;
  public void setMetaId (int metaId) {
    this.metaId = metaId;
  }
  public int getMetaId () {
    return this.metaId;
  }


  @Column(name="property")
  protected String property;
  public void setProperty (String property) {
    this.property = property;
  }
  public String getProperty () {
    return this.property;
  }


  @Column(name="k")
  protected String k;
  public void setK (String k) {
    this.k = k;
  }
  public String getK () {
    return this.k;
  }


  @Column(name="v")
  protected String v;
  public void setV (String v) {
    this.v = v;
  }
  public String getV () {
    return this.v;
  }


  @Column(name="position")
  protected int position;
  public void setPosition (int position) {
    this.position = position;
  }
  public int getPosition () {
    return this.position;
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



}
