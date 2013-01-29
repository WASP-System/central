
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="resourcecategory")
public class ResourceCategory extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -378972557561200358L;
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
	 * resourceTypeId
	 *
	 */
	@Column(name="resourcetypeid")
	protected int resourceTypeId;

	/**
	 * setResourceTypeId(int resourceTypeId)
	 *
	 * @param resourceTypeId
	 *
	 */
	
	public void setResourceTypeId (int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	/**
	 * getResourceTypeId()
	 *
	 * @return resourceTypeId
	 *
	 */
	public int getResourceTypeId () {
		return this.resourceTypeId;
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
	 * resourceType
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcetypeid", insertable=false, updatable=false)
	protected ResourceType resourceType;

	/**
	 * setResourceType (ResourceType resourceType)
	 *
	 * @param resourceType
	 *
	 */
	public void setResourceType (ResourceType resourceType) {
		this.resourceType = resourceType;
		this.resourceTypeId = resourceType.resourceTypeId;
	}

	/**
	 * getResourceType ()
	 *
	 * @return resourceType
	 *
	 */
	
	public ResourceType getResourceType () {
		return this.resourceType;
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
	@JsonIgnore
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
	 * jobDraftresourcecategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<JobDraftresourcecategory> jobDraftresourcecategory;


	/** 
	 * getJobDraftresourcecategory()
	 *
	 * @return jobDraftresourcecategory
	 *
	 */
	@JsonIgnore
	public List<JobDraftresourcecategory> getJobDraftresourcecategory() {
		return this.jobDraftresourcecategory;
	}


	/** 
	 * setJobDraftresourcecategory
	 *
	 * @param jobDraftresourcecategory
	 *
	 */
	public void setJobDraftresourcecategory (List<JobDraftresourcecategory> jobDraftresourcecategory) {
		this.jobDraftresourcecategory = jobDraftresourcecategory;
	}

	/** 
	 * workflowresourcecategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<Workflowresourcecategory> workflowresourcecategory;


	/** 
	 * getWorkflowresourcecategory()
	 *
	 * @return workflowresourcecategory
	 *
	 */
	@JsonIgnore
	public List<Workflowresourcecategory> getWorkflowresourcecategory() {
		return this.workflowresourcecategory;
	}


	/** 
	 * setWorkflowresourcecategory
	 *
	 * @param workflowresourcecategory
	 *
	 */
	public void setWorkflowresourcecategory (List<Workflowresourcecategory> workflowresourcecategory) {
		this.workflowresourcecategory = workflowresourcecategory;
	}



	/** 
	 * adaptorsetResourceCategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<AdaptorsetResourceCategory> adaptorsetResourceCategory;


	/** 
	 * getAdaptorsetResourceCategory()
	 *
	 * @return adaptorsetResourceCategory
	 *
	 */
	@JsonIgnore
	public List<AdaptorsetResourceCategory> getAdaptorsetResourceCategory() {
		return this.adaptorsetResourceCategory;
	}


	/** 
	 * setAdaptorsetResourceCategory
	 *
	 * @param adaptorsetResourceCategory
	 *
	 */
	public void setAdaptorsetResourceCategory (List<AdaptorsetResourceCategory> adaptorsetResourceCategory) {
		this.adaptorsetResourceCategory = adaptorsetResourceCategory;
	}

	
	/** 
	 * sampleSubtypeResourceCategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<SampleSubtypeResourceCategory> sampleSubtypeResourceCategory;


	/** 
	 * getSampleSubtypeResourceCategory()
	 *
	 * @return sampleSubtypeResourceCategory
	 *
	 */
	@JsonIgnore
	public List<SampleSubtypeResourceCategory> getSampleSubtypeResourceCategory() {
		return this.sampleSubtypeResourceCategory;
	}


	/** 
	 * setSampleSubtypeResourceCategory
	 *
	 * @param sampleSubtypeResourceCategory
	 *
	 */
	public void setSampleSubtypeResourceCategory (List<SampleSubtypeResourceCategory> sampleSubtypeResourceCategory) {
		this.sampleSubtypeResourceCategory = sampleSubtypeResourceCategory;
	}



}
