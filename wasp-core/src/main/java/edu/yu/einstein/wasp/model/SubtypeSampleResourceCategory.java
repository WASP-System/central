
/**
 *
 * SubtypeSampleResourceCategory.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleResourceCategory
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
@Table(name="subtypesampleresourcecategory")
public class SubtypeSampleResourceCategory extends WaspModel {

	/** 
	 * subtypeSampleresourcecategoryId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer subtypeSampleresourcecategoryId;

	/**
	 * setSubtypeSampleresourcecategoryId(Integer subtypeSampleresourcecategoryId)
	 *
	 * @param subtypeSampleresourcecategoryId
	 *
	 */
	
	public void setSubtypeSampleresourcecategoryId (Integer subtypeSampleresourcecategoryId) {
		this.subtypeSampleresourcecategoryId = subtypeSampleresourcecategoryId;
	}

	/**
	 * getSubtypeSampleresourcecategoryId()
	 *
	 * @return subtypeSampleresourcecategoryId
	 *
	 */
	public Integer getSubtypeSampleresourcecategoryId () {
		return this.subtypeSampleresourcecategoryId;
	}




	/** 
	 * subtypeSampleId
	 *
	 */
	@Column(name="subtypesampleid")
	protected Integer subtypeSampleId;

	/**
	 * setSubtypeSampleId(Integer subtypeSampleId)
	 *
	 * @param subtypeSampleId
	 *
	 */
	
	public void setSubtypeSampleId (Integer subtypeSampleId) {
		this.subtypeSampleId = subtypeSampleId;
	}

	/**
	 * getSubtypeSampleId()
	 *
	 * @return subtypeSampleId
	 *
	 */
	public Integer getSubtypeSampleId () {
		return this.subtypeSampleId;
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
	 * subtypeSample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="subtypesampleid", insertable=false, updatable=false)
	protected SubtypeSample subtypeSample;

	/**
	 * setSubtypeSample (SubtypeSample subtypeSample)
	 *
	 * @param subtypeSample
	 *
	 */
	public void setSubtypeSample (SubtypeSample subtypeSample) {
		this.subtypeSample = subtypeSample;
		this.subtypeSampleId = subtypeSample.subtypeSampleId;
	}

	/**
	 * getSubtypeSample ()
	 *
	 * @return subtypeSample
	 *
	 */
	
	public SubtypeSample getSubtypeSample () {
		return this.subtypeSample;
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
		this.resourcecategoryId = resourceCategory.resourceCategoryId;
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


}
