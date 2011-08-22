
/**
 *
 * ResourceBarcode.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcode object
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
@Table(name="resourcebarcode")
public class ResourceBarcode extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int resourceBarcodeId;
  public void setResourceBarcodeId (int resourceBarcodeId) {
    this.resourceBarcodeId = resourceBarcodeId;
  }
  public int getResourceBarcodeId () {
    return this.resourceBarcodeId;
  }


  @Column(name="resourceid")
  protected int resourceId;
  public void setResourceId (int resourceId) {
    this.resourceId = resourceId;
  }
  public int getResourceId () {
    return this.resourceId;
  }


  @Column(name="barcodeid")
  protected int barcodeId;
  public void setBarcodeId (int barcodeId) {
    this.barcodeId = barcodeId;
  }
  public int getBarcodeId () {
    return this.barcodeId;
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

  @NotAudited
  @ManyToOne
   @JoinColumn(name="barcodeid", insertable=false, updatable=false)
  protected Barcode barcode;
  public void setBarcode (Barcode barcode) {
    this.barcode = barcode;
    this.barcodeId = barcode.barcodeId;
  }
  public Barcode getBarcode () {
    return this.barcode;
  }

}
