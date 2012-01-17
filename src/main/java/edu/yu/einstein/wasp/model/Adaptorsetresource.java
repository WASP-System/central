
/**
 *
 * Adaptorsetresource.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorsetresource
 *
 *
 */

package edu.yu.einstein.wasp.model;

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
@Table(name="adaptorsetresource")
public class Adaptorsetresource extends WaspModel {

	/** 
	 * adaptorsetresourceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer adaptorsetresourceId;

	/**
	 * setAdaptorsetresourceId(Integer adaptorsetresourceId)
	 *
	 * @param adaptorsetresourceId
	 *
	 */
	
	public void setAdaptorsetresourceId (Integer adaptorsetresourceId) {
		this.adaptorsetresourceId = adaptorsetresourceId;
	}

	/**
	 * getAdaptorsetresourceId()
	 *
	 * @return adaptorsetresourceId
	 *
	 */
	public Integer getAdaptorsetresourceId () {
		return this.adaptorsetresourceId;
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
		this.adaptorsetId = adaptorset.adaptorsetId;
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


}
