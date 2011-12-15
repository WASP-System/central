
/**
 *
 * ResourceLane.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLane
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
@Table(name="resourcelane")
public class ResourceLane extends WaspModel {

	/** 
	 * resourceLaneId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer resourceLaneId;

	/**
	 * setResourceLaneId(Integer resourceLaneId)
	 *
	 * @param resourceLaneId
	 *
	 */
	
	public void setResourceLaneId (Integer resourceLaneId) {
		this.resourceLaneId = resourceLaneId;
	}

	/**
	 * getResourceLaneId()
	 *
	 * @return resourceLaneId
	 *
	 */
	public Integer getResourceLaneId () {
		return this.resourceLaneId;
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
	 * runLane
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourcelaneid", insertable=false, updatable=false)
	protected List<RunLane> runLane;


	/** 
	 * getRunLane()
	 *
	 * @return runLane
	 *
	 */
	public List<RunLane> getRunLane() {
		return this.runLane;
	}


	/** 
	 * setRunLane
	 *
	 * @param runLane
	 *
	 */
	public void setRunLane (List<RunLane> runLane) {
		this.runLane = runLane;
	}



}
