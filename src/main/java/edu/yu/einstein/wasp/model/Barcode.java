
/**
 *
 * Barcode.java 
 * @author echeng (table2type.pl)
 *  
 * the Barcode object
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
@Table(name="barcode")
public class Barcode extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int barcodeId;
  public void setBarcodeId (int barcodeId) {
    this.barcodeId = barcodeId;
  }
  public int getBarcodeId () {
    return this.barcodeId;
  }


  @Column(name="barcode")
  protected String barcode;
  public void setBarcode (String barcode) {
    this.barcode = barcode;
  }
  public String getBarcode () {
    return this.barcode;
  }


  @Column(name="barcodefor")
  protected String barcodefor;
  public void setBarcodefor (String barcodefor) {
    this.barcodefor = barcodefor;
  }
  public String getBarcodefor () {
    return this.barcodefor;
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
  @OneToMany
   @JoinColumn(name="barcodeid", insertable=false, updatable=false)
  protected List<ResourceBarcode> resourceBarcode;
  public List<ResourceBarcode> getResourceBarcode()  {
    return this.resourceBarcode;
  }
  public void setResourceBarcode (List<ResourceBarcode> resourceBarcode)  {
    this.resourceBarcode = resourceBarcode;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="barcodeid", insertable=false, updatable=false)
  protected List<SampleBarcode> sampleBarcode;
  public List<SampleBarcode> getSampleBarcode()  {
    return this.sampleBarcode;
  }
  public void setSampleBarcode (List<SampleBarcode> sampleBarcode)  {
    this.sampleBarcode = sampleBarcode;
  }



}
