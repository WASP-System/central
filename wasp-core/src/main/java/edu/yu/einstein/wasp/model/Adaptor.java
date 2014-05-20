
/**
 *
 * Adaptor.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptor
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="adaptor")
public class Adaptor extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683497815076911656L;

	/**
	 * setAdaptorId(Integer adaptorId)
	 *
	 * @param adaptorId
	 *
	 */
	@Deprecated
	public void setAdaptorId (Integer adaptorId) {
		setId(adaptorId);
	}

	/**
	 * getAdaptorId()
	 *
	 * @return adaptorId
	 *
	 */
	@Deprecated
	public Integer getAdaptorId () {
		return getId();
	}




	/** 
	 * adaptorsetId
	 *
	 */
	@Column(name="adaptorsetid")
	protected Integer adaptorsetId;

	/**
	 * setAdaptorsetId(Integer adaptorsetId)
	 *
	 * @param adaptorsetId
	 *
	 */
	
	public void setAdaptorsetId (Integer adaptorsetId) {
		this.adaptorsetId = adaptorsetId;
	}

	/**
	 * getAdaptorsetId()
	 *
	 * @return adaptorsetId
	 *
	 */
	public Integer getAdaptorsetId () {
		return this.adaptorsetId;
	}




	/** 
	 * iName
	 *
	 */
	@Column(name="iname")
	protected String iName;

	/**
	 * setIName(String iName)
	 *
	 * @param iName
	 *
	 */
	
	public void setIName (String iName) {
		this.iName = iName;
	}

	/**
	 * getIName()
	 *
	 * @return iName
	 *
	 */
	public String getIName () {
		return this.iName;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
	}




	/** 
	 * sequence
	 *
	 */
	@Column(name="sequence")
	protected String sequence;

	/**
	 * setSequence(String sequence)
	 *
	 * @param sequence
	 *
	 */
	
	public void setSequence (String sequence) {
		this.sequence = sequence;
	}

	/**
	 * getSequence()
	 *
	 * @return sequence
	 *
	 */
	public String getSequence () {
		return this.sequence;
	}




	/** 
	 * barcodesequence
	 *
	 */
	@Column(name="barcodesequence")
	protected String barcodesequence;

	/**
	 * setBarcodesequence(String barcodesequence)
	 *
	 * @param barcodesequence
	 *
	 */
	
	public void setBarcodesequence (String barcodesequence) {
		this.barcodesequence = barcodesequence;
	}

	/**
	 * getBarcodesequence()
	 *
	 * @return barcodesequence
	 *
	 */
	public String getBarcodesequence () {
		return this.barcodesequence;
	}




	/** 
	 * barcodenumber
	 *
	 */
	@Column(name="barcodenumber")
	protected Integer barcodenumber;

	/**
	 * setBarcodenumber(Integer barcodenumber)
	 *
	 * @param barcodenumber
	 *
	 */
	
	public void setBarcodenumber (Integer barcodenumber) {
		this.barcodenumber = barcodenumber;
	}

	/**
	 * getBarcodenumber()
	 *
	 * @return barcodenumber
	 *
	 */
	public Integer getBarcodenumber () {
		return this.barcodenumber;
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
	 * adaptorset
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="adaptorsetid", insertable=false, updatable=false)
	protected Adaptorset adaptorset;

	/**
	 * setAdaptorset (Adaptorset adaptorset)
	 *
	 * @param adaptorset
	 *
	 */
	public void setAdaptorset (Adaptorset adaptorset) {
		this.adaptorset = adaptorset;
		this.adaptorsetId = adaptorset.getId();
	}

	/**
	 * getAdaptorset ()
	 *
	 * @return adaptorset
	 *
	 */
	
	public Adaptorset getAdaptorset () {
		return this.adaptorset;
	}


	/** 
	 * adaptorMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="adaptorid", insertable=false, updatable=false)
	protected List<AdaptorMeta> adaptorMeta;


	/** 
	 * getAdaptorMeta()
	 *
	 * @return adaptorMeta
	 *
	 */
	@JsonIgnore
	public List<AdaptorMeta> getAdaptorMeta() {
		return this.adaptorMeta;
	}


	/** 
	 * setAdaptorMeta
	 *
	 * @param adaptorMeta
	 *
	 */
	public void setAdaptorMeta (List<AdaptorMeta> adaptorMeta) {
		this.adaptorMeta = adaptorMeta;
	}



}
