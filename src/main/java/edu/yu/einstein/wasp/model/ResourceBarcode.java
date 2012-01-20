
/**
 *
 * ResourceBarcode.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcode
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
@Table(name="resourcebarcode")
public class ResourceBarcode extends WaspModel {

	/** 
	 * resourceBarcodeId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer resourceBarcodeId;

	/**
	 * setResourceBarcodeId(Integer resourceBarcodeId)
	 *
	 * @param resourceBarcodeId
	 *
	 */
	
	public void setResourceBarcodeId (Integer resourceBarcodeId) {
		this.resourceBarcodeId = resourceBarcodeId;
	}

	/**
	 * getResourceBarcodeId()
	 *
	 * @return resourceBarcodeId
	 *
	 */
	public Integer getResourceBarcodeId () {
		return this.resourceBarcodeId;
	}




	/** 
	 * resourceId
	 *
	 */
	@Column(name="resourceid")
	protected Integer resourceId;

	/**
	 * setResourceId(Integer resourceId)
	 *
	 * @param resourceId
	 *
	 */
	
	public void setResourceId (Integer resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * getResourceId()
	 *
	 * @return resourceId
	 *
	 */
	public Integer getResourceId () {
		return this.resourceId;
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
	 * resource
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected Resource resource;

	/**
	 * setResource (Resource resource)
	 *
	 * @param resource
	 *
	 */
	public void setResource (Resource resource) {
		this.resource = resource;
		this.resourceId = resource.resourceId;
	}

	/**
	 * getResource ()
	 *
	 * @return resource
	 *
	 */
	
	public Resource getResource () {
		return this.resource;
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
