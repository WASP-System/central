
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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name="meta")
public class Meta extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected Integer metaId;
  public void setMetaId (Integer metaId) {
    this.metaId = metaId;
  }
  public Integer getMetaId () {
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
  protected Integer position;
  public void setPosition (Integer position) {
    this.position = position;
  }
  public Integer getPosition () {
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
  protected Integer lastUpdUser;
  public void setLastUpdUser (Integer lastUpdUser) {
    this.lastUpdUser = lastUpdUser;
  }
  public Integer getLastUpdUser () {
    return this.lastUpdUser;
  }



}
