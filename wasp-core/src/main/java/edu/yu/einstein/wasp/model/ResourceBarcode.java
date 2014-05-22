
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="resourcebarcode")
public class ResourceBarcode extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 487166448262644474L;
	

	/**
	 * setResourceBarcodeId(Integer resourceBarcodeId)
	 *
	 * @param resourceBarcodeId
	 *
	 */
	@Deprecated
	public void setResourceBarcodeId (Integer resourceBarcodeId) {
		setId(resourceBarcodeId);
	}

	/**
	 * getResourceBarcodeId()
	 *
	 * @return resourceBarcodeId
	 *
	 */
	@Deprecated
	public Integer getResourceBarcodeId () {
		return getId();
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
		this.resourceId = resource.getId();
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
		this.barcodeId = barcode.getId();
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
