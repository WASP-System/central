
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
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="samplebarcode")
public class SampleBarcode extends WaspModel {

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
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
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
