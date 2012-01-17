
/**
 *
 * Adaptorset.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorset
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="adaptorset")
public class Adaptorset extends WaspModel {

	/** 
	 * adaptorsetId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * typeSampleId
	 *
	 */
	@Column(name="typesampleid")
	protected Integer typeSampleId;

	/**
	 * setTypeSampleId(Integer typeSampleId)
	 *
	 * @param typeSampleId
	 *
	 */
	
	public void setTypeSampleId (Integer typeSampleId) {
		this.typeSampleId = typeSampleId;
	}

	/**
	 * getTypeSampleId()
	 *
	 * @return typeSampleId
	 *
	 */
	public Integer getTypeSampleId () {
		return this.typeSampleId;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive;

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
	 * typeSample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="typesampleid", insertable=false, updatable=false)
	protected TypeSample typeSample;

	/**
	 * setTypeSample (TypeSample typeSample)
	 *
	 * @param typeSample
	 *
	 */
	public void setTypeSample (TypeSample typeSample) {
		this.typeSample = typeSample;
		this.typeSampleId = typeSample.typeSampleId;
	}

	/**
	 * getTypeSample ()
	 *
	 * @return typeSample
	 *
	 */
	
	public TypeSample getTypeSample () {
		return this.typeSample;
	}


	/** 
	 * adaptorsetMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="adaptorsetid", insertable=false, updatable=false)
	protected List<AdaptorsetMeta> adaptorsetMeta;


	/** 
	 * getAdaptorsetMeta()
	 *
	 * @return adaptorsetMeta
	 *
	 */
	public List<AdaptorsetMeta> getAdaptorsetMeta() {
		return this.adaptorsetMeta;
	}


	/** 
	 * setAdaptorsetMeta
	 *
	 * @param adaptorsetMeta
	 *
	 */
	public void setAdaptorsetMeta (List<AdaptorsetMeta> adaptorsetMeta) {
		this.adaptorsetMeta = adaptorsetMeta;
	}



	/** 
	 * adaptorsetresource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="adaptorsetid", insertable=false, updatable=false)
	protected List<Adaptorsetresource> adaptorsetresource;


	/** 
	 * getAdaptorsetresource()
	 *
	 * @return adaptorsetresource
	 *
	 */
	public List<Adaptorsetresource> getAdaptorsetresource() {
		return this.adaptorsetresource;
	}


	/** 
	 * setAdaptorsetresource
	 *
	 * @param adaptorsetresource
	 *
	 */
	public void setAdaptorsetresource (List<Adaptorsetresource> adaptorsetresource) {
		this.adaptorsetresource = adaptorsetresource;
	}



	/** 
	 * adaptor
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="adaptorsetid", insertable=false, updatable=false)
	protected List<Adaptor> adaptor;


	/** 
	 * getAdaptor()
	 *
	 * @return adaptor
	 *
	 */
	public List<Adaptor> getAdaptor() {
		return this.adaptor;
	}


	/** 
	 * setAdaptor
	 *
	 * @param adaptor
	 *
	 */
	public void setAdaptor (List<Adaptor> adaptor) {
		this.adaptor = adaptor;
	}



}
