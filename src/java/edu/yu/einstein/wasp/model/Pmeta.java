
/**
 *
 * Pmeta.java 
 * @author echeng (table2type.pl)
 *  
 * the Pmeta object
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
@Table(name="pmeta")
public class Pmeta extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int pmetaId;
  public void setPmetaId (int pmetaId) {
    this.pmetaId = pmetaId;
  }
  public int getPmetaId () {
    return this.pmetaId;
  }


  @Column(name="pid")
  protected int pId;
  public void setPId (int pId) {
    this.pId = pId;
  }
  public int getPId () {
    return this.pId;
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



  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected P p;
  public void setP (P p) {
    this.p = p;
    this.pId = p.pId;
  }
  public P getP () {
    return this.p;
  }

}
