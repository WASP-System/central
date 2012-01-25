
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

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

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
	 * typeResourceId
	 *
	 */
	@Column(name="typeresourceid")
	protected int typeResourceId;

	/**
	 * setTypeResourceId(int typeResourceId)
	 *
	 * @param typeResourceId
	 *
	 */
	
	public void setTypeResourceId (int typeResourceId) {
		this.typeResourceId = typeResourceId;
	}

	/**
	 * getTypeResourceId()
	 *
	 * @return typeResourceId
	 *
	 */
	public int getTypeResourceId () {
		return this.typeResourceId;
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
	 * typeResource
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="typeresourceid", insertable=false, updatable=false)
	protected TypeResource typeResource;

	/**
	 * setTypeResource (TypeResource typeResource)
	 *
	 * @param typeResource
	 *
	 */
	public void setTypeResource (TypeResource typeResource) {
		this.typeResource = typeResource;
		this.typeResourceId = typeResource.typeResourceId;
	}

	/**
	 * getTypeResource ()
	 *
	 * @return typeResource
	 *
	 */
	
	public TypeResource getTypeResource () {
		return this.typeResource;
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
	 * subtypeSampleResourceCategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected List<SubtypeSampleResourceCategory> subtypeSampleResourceCategory;


	/** 
	 * getSubtypeSampleResourceCategory()
	 *
	 * @return subtypeSampleResourceCategory
	 *
	 */
	public List<SubtypeSampleResourceCategory> getSubtypeSampleResourceCategory() {
		return this.subtypeSampleResourceCategory;
	}


	/** 
	 * setSubtypeSampleResourceCategory
	 *
	 * @param subtypeSampleResourceCategory
	 *
	 */
	public void setSubtypeSampleResourceCategory (List<SubtypeSampleResourceCategory> subtypeSampleResourceCategory) {
		this.subtypeSampleResourceCategory = subtypeSampleResourceCategory;
	}



}
