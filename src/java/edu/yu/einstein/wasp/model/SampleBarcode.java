
/**
 *
 * SampleBarcode.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcode object
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
@Table(name="samplebarcode")
public class SampleBarcode extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int sampleBarcode;
  public void setSampleBarcode (int sampleBarcode) {
    this.sampleBarcode = sampleBarcode;
  }
  public int getSampleBarcode () {
    return this.sampleBarcode;
  }


  @Column(name="sampleid")
  protected int sampleId;
  public void setSampleId (int sampleId) {
    this.sampleId = sampleId;
  }
  public int getSampleId () {
    return this.sampleId;
  }


  @Column(name="barcodeid")
  protected int barcodeId;
  public void setBarcodeId (int barcodeId) {
    this.barcodeId = barcodeId;
  }
  public int getBarcodeId () {
    return this.barcodeId;
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
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected Sample sample;
  public void setSample (Sample sample) {
    this.sample = sample;
    this.sampleId = sample.sampleId;
  }
  public Sample getSample () {
    return this.sample;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
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
