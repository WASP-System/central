/**
 *
 * WaspModel.java
 * @author echeng
 *
 * this is the base class,
 * every class in the system should extend this.
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.WordUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.exception.ModelCopyException;

@MappedSuperclass
public abstract class WaspModel extends WaspCoreModel implements Serializable {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_pk")
	protected Integer id;
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	/** 
	 * lastUpdTs
	 *
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs(Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}
	
	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs() {
		return this.lastUpdTs;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created")
	protected Date created;

	/**
	 * setCreated(Date created)
	 *
	 * @param created
	 *
	 */
	public void setCreated (Date created) {
		this.created = created;
	}

	/**
	 * @return created date
	 */
	public Date getCreated() {
		return created;
	}
	
	@PreUpdate
	@PrePersist
	public void doUpdateTime() {
		lastUpdTs = new Date();
		if (created == null) {
			created = new Date();
		}
	}
	
	@ManyToOne
	@JoinColumn(name="lastupdatebyuser", insertable=false, updatable=false)
	private User lastUpdatedByUser;

	/**
	 * @return the lastUpdatedByUser
	 */
	public User getLastUpdatedByUser() {
		return lastUpdatedByUser;
	}

	/**
	 * @param lastUpdatedByUser the lastUpdatedByUser to set
	 */
	public void setLastUpdatedByUser(User lastUpdatedByUser) {
		this.lastUpdatedByUser = lastUpdatedByUser;
	}
	
	// TODO: Remove, incorrect usage
	@Column(name = "lastupduser")
	protected Integer lastUpdUser;

	public void setLastUpdUser(Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	@JsonIgnore
	public Integer getLastUpdUser() {
		return this.lastUpdUser;
	}
	

  /**
	 * 
	 */
	private static final long serialVersionUID = -8306199017533320113L;
  
}
