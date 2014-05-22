
/**
 *
 * ResourceCategoryMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategoryMeta
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
@Table(name="resourcecategorymeta")
public class ResourceCategoryMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 499820160323772514L;
	

	/**
	 * setResourceCategoryMetaId(Integer resourceCategoryMetaId)
	 *
	 * @param resourceCategoryMetaId
	 *
	 */
	@Deprecated
	public void setResourceCategoryMetaId (Integer resourceCategoryMetaId) {
		setId(resourceCategoryMetaId);
	}

	/**
	 * getResourceCategoryMetaId()
	 *
	 * @return resourceCategoryMetaId
	 *
	 */
	@Deprecated
	public Integer getResourceCategoryMetaId () {
		return getId();
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


}
