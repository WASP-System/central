
/**
 *
 * ResourceCategory.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceCategory
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="resourcecategory")
public class ResourceCategory extends WaspModel {

	/** 
	 * resourceCategoryId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer resourceCategoryId;

	/**
	 * setResourceCategoryId(Integer resourceCategoryId)
	 *
	 * @param resourceCategoryId
	 *
	 */
	
	public void setResourceCategoryId (Integer resourceCategoryId) {
		this.resourceCategoryId = resourceCategoryId;
	}

	/**
	 * getResourceCategoryId()
	 *
	 * @return resourceCategoryId
	 *
	 */
	public Integer getResourceCategoryId () {
		return this.resourceCategoryId;
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
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
	}




	/** 
	 * resourceCategoryMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<ResourceCategoryMeta> resourceCategoryMeta;


	/** 
	 * getResourceCategoryMeta()
	 *
	 * @return resourceCategoryMeta
	 *
	 */
	public List<ResourceCategoryMeta> getResourceCategoryMeta() {
		return this.resourceCategoryMeta;
	}


	/** 
	 * setResourceCategoryMeta
	 *
	 * @param resourceCategoryMeta
	 *
	 */
	public void setResourceCategoryMeta (List<ResourceCategoryMeta> resourceCategoryMeta) {
		this.resourceCategoryMeta = resourceCategoryMeta;
	}



	/** 
	 * resource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<Resource> resource;


	/** 
	 * getResource()
	 *
	 * @return resource
	 *
	 */
	public List<Resource> getResource() {
		return this.resource;
	}


	/** 
	 * setResource
	 *
	 * @param resource
	 *
	 */
	public void setResource (List<Resource> resource) {
		this.resource = resource;
	}



	/** 
	 * typeSampleResourceCategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<TypeSampleResourceCategory> typeSampleResourceCategory;


	/** 
	 * getTypeSampleresourcecategory()
	 *
	 * @return typeSampleResourceCategory
	 *
	 */
	public List<TypeSampleResourceCategory> getTypeSampleResourceCategory() {
		return this.typeSampleResourceCategory;
	}


	/** 
	 * setTypeSampleresourcecategory
	 *
	 * @param typeSampleResourceCategory
	 *
	 */
	public void setTypeSampleResourceCategory (List<TypeSampleResourceCategory> typeSampleResourceCategory) {
		this.typeSampleResourceCategory = typeSampleResourceCategory;
	}



	/** 
	 * adaptorsetresourcecategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<AdaptorsetResourceCategory> adaptorsetresourcecategory;


	/** 
	 * getAdaptorsetResourceCategory()
	 *
	 * @return adaptorsetresourcecategory
	 *
	 */
	public List<AdaptorsetResourceCategory> getAdaptorsetResourceCategory() {
		return this.adaptorsetresourcecategory;
	}


	/** 
	 * setAdaptorsetResourceCategory
	 *
	 * @param adaptorsetresourcecategory
	 *
	 */
	public void setAdaptorsetResourceCategory (List<AdaptorsetResourceCategory> adaptorsetresourcecategory) {
		this.adaptorsetresourcecategory = adaptorsetresourcecategory;
	}



}
