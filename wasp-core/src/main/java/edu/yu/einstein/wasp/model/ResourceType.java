
/**
 *
 * ResourceType.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceType
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="resourcetype")
public class ResourceType extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2559677620369160301L;
	

	/**
	 * setResourceTypeId(Integer resourceTypeId)
	 *
	 * @param resourceTypeId
	 *
	 */
	@Deprecated
	public void setResourceTypeId (Integer resourceTypeId) {
		setId(resourceTypeId);
	}

	/**
	 * getResourceTypeId()
	 *
	 * @return resourceTypeId
	 *
	 */
	@Deprecated
	public Integer getResourceTypeId () {
		return getId();
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
	 * resourceCategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcetypeid", insertable=false, updatable=false)
	protected List<ResourceCategory> resourceCategory;


	/** 
	 * getResourceCategory()
	 *
	 * @return resourceCategory
	 *
	 */
	@JsonIgnore
	public List<ResourceCategory> getResourceCategory() {
		return this.resourceCategory;
	}


	/** 
	 * setResourceCategory
	 *
	 * @param resourceCategory
	 *
	 */
	public void setResourceCategory (List<ResourceCategory> resourceCategory) {
		this.resourceCategory = resourceCategory;
	}



	/** 
	 * resource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcetypeid", insertable=false, updatable=false)
	protected List<Resource> resource;


	/** 
	 * getResource()
	 *
	 * @return resource
	 *
	 */
	@JsonIgnore
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
	 * software
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcetypeid", insertable=false, updatable=false)
	protected List<Software> software;


	/** 
	 * getSoftware()
	 *
	 * @return software
	 *
	 */
	@JsonIgnore
	public List<Software> getSoftware() {
		return this.software;
	}


	/** 
	 * setSoftware
	 *
	 * @param software
	 *
	 */
	public void setSoftware (List<Software> software) {
		this.software = software;
	}



	/** 
	 * workflowResourceType
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcetypeid", insertable=false, updatable=false)
	protected List<WorkflowResourceType> workflowResourceType;


	/** 
	 * getWorkflowresourcetype()
	 *
	 * @return workflowResourceType
	 *
	 */
	@JsonIgnore
	public List<WorkflowResourceType> getWorkflowResourceType() {
		return this.workflowResourceType;
	}


	/** 
	 * setWorkflowresourcetype
	 *
	 * @param workflowResourceType
	 *
	 */
	public void setWorkflowResourceType (List<WorkflowResourceType> workflowResourceType) {
		this.workflowResourceType = workflowResourceType;
	}



}
