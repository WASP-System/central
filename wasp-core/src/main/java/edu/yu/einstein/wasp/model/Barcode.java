
/**
 *
 * Barcode.java 
 * @author echeng (table2type.pl)
 *  
 * the Barcode
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="barcode")
public class Barcode extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4750981400438838305L;

	/**
	 * setBarcodeId(Integer barcodeId)
	 *
	 * @param barcodeId
	 *
	 */
	@Deprecated
	public void setBarcodeId (Integer barcodeId) {
		setId(barcodeId);
	}

	/**
	 * getBarcodeId()
	 *
	 * @return barcodeId
	 *
	 */
	@Deprecated
	public Integer getBarcodeId () {
		return getId();
	}




	/** 
	 * barcode
	 *
	 */
	@Column(name="barcode")
	protected String barcode;

	/**
	 * setBarcode(String barcode)
	 *
	 * @param barcode
	 *
	 */
	
	public void setBarcode (String barcode) {
		this.barcode = barcode;
	}

	/**
	 * getBarcode()
	 *
	 * @return barcode
	 *
	 */
	public String getBarcode () {
		return this.barcode;
	}




	/** 
	 * barcodefor
	 *
	 */
	@Column(name="barcodefor")
	protected String barcodefor;

	/**
	 * setBarcodefor(String barcodefor)
	 *
	 * @param barcodefor
	 *
	 */
	
	public void setBarcodefor (String barcodefor) {
		this.barcodefor = barcodefor;
	}

	/**
	 * getBarcodefor()
	 *
	 * @return barcodefor
	 *
	 */
	public String getBarcodefor () {
		return this.barcodefor;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 1;

	/**
	 * setIsActive(Integer isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public Integer getIsActive () {
		return this.isActive;
	}




	
	/** 
	 * resourceBarcode
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="barcodeid", insertable=false, updatable=false)
	protected List<ResourceBarcode> resourceBarcode;


	/** 
	 * getResourceBarcode()
	 *
	 * @return resourceBarcode
	 *
	 */
	@JsonIgnore
	public List<ResourceBarcode> getResourceBarcode() {
		return this.resourceBarcode;
	}


	/** 
	 * setResourceBarcode
	 *
	 * @param resourceBarcode
	 *
	 */
	public void setResourceBarcode (List<ResourceBarcode> resourceBarcode) {
		this.resourceBarcode = resourceBarcode;
	}



	/** 
	 * sampleBarcode
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="barcodeid", insertable=false, updatable=false)
	protected List<SampleBarcode> sampleBarcode;


	/** 
	 * getSampleBarcode()
	 *
	 * @return sampleBarcode
	 *
	 */
	@JsonIgnore
	public List<SampleBarcode> getSampleBarcode() {
		return this.sampleBarcode;
	}


	/** 
	 * setSampleBarcode
	 *
	 * @param sampleBarcode
	 *
	 */
	public void setSampleBarcode (List<SampleBarcode> sampleBarcode) {
		this.sampleBarcode = sampleBarcode;
	}



}
