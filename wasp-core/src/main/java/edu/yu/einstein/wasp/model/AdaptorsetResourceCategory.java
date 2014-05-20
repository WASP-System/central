
/**
 *
 * Adaptorsetresourcecategory.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorsetresourcecategory
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
@Table(name="adaptorsetresourcecategory")
public class AdaptorsetResourceCategory extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6657776351152845874L;

	/**
	 * setAdaptorsetResourcecategoryId(Integer adaptorsetresourcecategoryId)
	 *
	 * @param adaptorsetresourcecategoryId
	 *
	 */
	@Deprecated
	public void setAdaptorsetResourcecategoryId (Integer adaptorsetresourcecategoryId) {
		setId(adaptorsetresourcecategoryId);
	}

	/**
	 * getAdaptorsetResourcecategoryId()
	 *
	 * @return adaptorsetresourcecategoryId
	 *
	 */
	@Deprecated
	public Integer getAdaptorsetResourcecategoryId () {
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
	 * resourcecategoryId
	 *
	 */
	@Column(name="resourcecategoryid")
	protected Integer resourcecategoryId;

	/**
	 * setResourcecategoryId(Integer resourcecategoryId)
	 *
	 * @param resourcecategoryId
	 *
	 */
	
	public void setResourcecategoryId (Integer resourcecategoryId) {
		this.resourcecategoryId = resourcecategoryId;
	}

	/**
	 * getResourcecategoryId()
	 *
	 * @return resourcecategoryId
	 *
	 */
	public Integer getResourcecategoryId () {
		return this.resourcecategoryId;
	}

	/**
	 * resourceCategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected ResourceCategory resourceCategory;

	/**
	 * setResourceCategory (ResourceCategory resourceCategory)
	 *
	 * @param resourceCategory
	 *
	 */
	public void setResourceCategory (ResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
		this.resourcecategoryId = resourceCategory.getId();
	}

	/**
	 * getResourceCategory ()
	 *
	 * @return resourceCategory
	 *
	 */
	
	public ResourceCategory getResourceCategory () {
		return this.resourceCategory;
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


}
