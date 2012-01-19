
/**
 *
 * TypeSampleCategoryResourceCategory.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategoryResourceCategory
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
@Table(name="typesamplecategoryresourcecategory")
public class TypeSampleCategoryResourceCategory extends WaspModel {

	/** 
	 * typeSamplecategoryresourcecategoryId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer typeSamplecategoryresourcecategoryId;

	/**
	 * setTypeSamplecategoryresourcecategoryId(Integer typeSamplecategoryresourcecategoryId)
	 *
	 * @param typeSamplecategoryresourcecategoryId
	 *
	 */
	
	public void setTypeSamplecategoryresourcecategoryId (Integer typeSamplecategoryresourcecategoryId) {
		this.typeSamplecategoryresourcecategoryId = typeSamplecategoryresourcecategoryId;
	}

	/**
	 * getTypeSamplecategoryresourcecategoryId()
	 *
	 * @return typeSamplecategoryresourcecategoryId
	 *
	 */
	public Integer getTypeSamplecategoryresourcecategoryId () {
		return this.typeSamplecategoryresourcecategoryId;
	}




	/** 
	 * typeSamplecategoryId
	 *
	 */
	@Column(name="typesamplecategoryid")
	protected Integer typeSamplecategoryId;

	/**
	 * setTypeSamplecategoryId(Integer typeSamplecategoryId)
	 *
	 * @param typeSamplecategoryId
	 *
	 */
	
	public void setTypeSamplecategoryId (Integer typeSamplecategoryId) {
		this.typeSamplecategoryId = typeSamplecategoryId;
	}

	/**
	 * getTypeSamplecategoryId()
	 *
	 * @return typeSamplecategoryId
	 *
	 */
	public Integer getTypeSamplecategoryId () {
		return this.typeSamplecategoryId;
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
	 * typeSampleCategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="typesamplecategoryid", insertable=false, updatable=false)
	protected TypeSampleCategory typeSampleCategory;

	/**
	 * setTypeSamplecategory (TypeSampleCategory typeSampleCategory)
	 *
	 * @param typeSampleCategory
	 *
	 */
	public void setTypeSampleCategory (TypeSampleCategory typeSampleCategory) {
		this.typeSampleCategory = typeSampleCategory;
		this.typeSamplecategoryId = typeSampleCategory.typeSamplecategoryId;
	}

	/**
	 * getTypeSamplecategory ()
	 *
	 * @return typeSampleCategory
	 *
	 */
	
	public TypeSampleCategory getTypeSampleCategory () {
		return this.typeSampleCategory;
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
