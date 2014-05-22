
/**
 *
 * ResourceMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMeta
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
@Table(name="resourcemeta")
public class ResourceMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1045801990506239255L;

	/**
	 * setResourceMetaId(Integer resourceMetaId)
	 *
	 * @param resourceMetaId
	 *
	 */
	@Deprecated
	public void setResourceMetaId (Integer resourceMetaId) {
		setId(resourceMetaId);
	}

	/**
	 * getResourceMetaId()
	 *
	 * @return resourceMetaId
	 *
	 */
	@Deprecated
	public Integer getResourceMetaId () {
		return getId();
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
		this.resourceId = resource.getId();
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


}
