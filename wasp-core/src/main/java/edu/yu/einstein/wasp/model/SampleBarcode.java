
/**
 *
 * SampleBarcode.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcode
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="samplebarcode")
public class SampleBarcode extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7468259363636944918L;
	/** 
	 * sampleBarcode
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleBarcode;

	/**
	 * setSampleBarcode(Integer sampleBarcode)
	 *
	 * @param sampleBarcode
	 *
	 */
	
	public void setSampleBarcode (Integer sampleBarcode) {
		this.sampleBarcode = sampleBarcode;
	}

	/**
	 * getSampleBarcode()
	 *
	 * @return sampleBarcode
	 *
	 */
	public Integer getSampleBarcode () {
		return this.sampleBarcode;
	}




	/** 
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected Integer sampleId;

	/**
	 * setSampleId(Integer sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (Integer sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public Integer getSampleId () {
		return this.sampleId;
	}




	/** 
	 * barcodeId
	 *
	 */
	@Column(name="barcodeid")
	protected Integer barcodeId;

	/**
	 * setBarcodeId(Integer barcodeId)
	 *
	 * @param barcodeId
	 *
	 */
	
	public void setBarcodeId (Integer barcodeId) {
		this.barcodeId = barcodeId;
	}

	/**
	 * getBarcodeId()
	 *
	 * @return barcodeId
	 *
	 */
	public Integer getBarcodeId () {
		return this.barcodeId;
	}

	/**
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected Sample sample;

	/**
	 * setSample (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSample (Sample sample) {
		this.sample = sample;
		this.sampleId = sample.sampleId;
	}

	/**
	 * getSample ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSample () {
		return this.sample;
	}


	/**
	 * barcode
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="barcodeid", insertable=false, updatable=false)
	protected Barcode barcode;

	/**
	 * setBarcode (Barcode barcode)
	 *
	 * @param barcode
	 *
	 */
	public void setBarcode (Barcode barcode) {
		this.barcode = barcode;
		this.barcodeId = barcode.barcodeId;
	}

	/**
	 * getBarcode ()
	 *
	 * @return barcode
	 *
	 */
	
	public Barcode getBarcode () {
		return this.barcode;
	}


}
