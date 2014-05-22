/**
 * 
 */
package edu.yu.einstein.wasp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.NotAudited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author calder
 * 
 */
@MappedSuperclass
public abstract class WaspCoreModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4136728312593640956L;
	
	/**
	 * generic logger included with every class.
	 */
	protected static Logger logger = LoggerFactory.getLogger(WaspCoreModel.class);
	
	/** 
	 * updated
	 *
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated")
	protected Date updated;
	
	/**
	 * @param updated
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	/**
	 * @return
	 */
	public Date getUpdated() {
		return this.updated;
	}
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created")
	protected Date created = new Date();

	/**
	 * setCreated(Date created)
	 *
	 * @param created
	 *
	 */
	private void setCreated(Date created) {
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
	public void doUpdate() {
		this.updated = new Date();
		if (this.created == null)
			setCreated(new Date());
		if (this.uuid == null)
			setUUID(UUID.randomUUID());
	}
	
	@NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="lastupdatebyuser")
	protected User lastUpdatedByUser;

	/**
	 * @return the lastUpdatedByUser
	 */
	@JsonIgnore
	public User getLastUpdatedByUser() {
		return lastUpdatedByUser;
	}

	/**
	 * @param lastUpdatedByUser the lastUpdatedByUser to set
	 */
	public void setLastUpdatedByUser(User lastUpdatedByUser) {
		logger.trace("Setting lastUpdatedByUser to " + lastUpdatedByUser.getId());
		this.lastUpdatedByUser = lastUpdatedByUser;
	}
	
//	// TODO: Remove, incorrect usage
//	@Column(name = "lastupduser")
//	protected Integer lastUpdUser;
//
//	public void setLastUpdUser(Integer lastUpdUser) {
//		this.lastUpdUser = lastUpdUser;
//	}
//
//	@JsonIgnore
//	public Integer getLastUpdUser() {
//		return this.lastUpdUser;
//	}

	@Column(name="uuid", length=16)
	private UUID uuid = UUID.randomUUID();
	
	/**
	 * private value for identity
	 * @return the resultID
	 */
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * @param resultID the resultID to set
	 */
	private void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public int hashCode(){
	    return new HashCodeBuilder()
	        .append(uuid)
	        .toHashCode();
	}


	@Override
	public boolean equals(final Object obj){
	    if(obj instanceof WaspCoreModel){
	        final WaspCoreModel other = (WaspCoreModel) obj;
	        return new EqualsBuilder()
	            .append(uuid, other.uuid)
	            .isEquals();
	    } else{
	        return false;
	    }
	}

}
