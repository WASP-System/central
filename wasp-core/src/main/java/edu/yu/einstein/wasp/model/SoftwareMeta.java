
/**
 *
 * SoftwareMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the SoftwareMeta
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
@Table(name="softwaremeta")
public class SoftwareMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8934740513727037919L;

	/**
	 * setSoftwareMetaId(Integer softwareMetaId)
	 *
	 * @param softwareMetaId
	 *
	 */
	@Deprecated
	public void setSoftwareMetaId (Integer softwareMetaId) {
		setId(softwareMetaId);
	}

	/**
	 * getSoftwareMetaId()
	 *
	 * @return softwareMetaId
	 *
	 */
	@Deprecated
	public Integer getSoftwareMetaId () {
		return getId();
	}




	/** 
	 * softwareId
	 *
	 */
	@Column(name="softwareid")
	protected Integer softwareId;

	/**
	 * setSoftwareId(Integer softwareId)
	 *
	 * @param softwareId
	 *
	 */
	
	public void setSoftwareId (Integer softwareId) {
		this.softwareId = softwareId;
	}

	/**
	 * getSoftwareId()
	 *
	 * @return softwareId
	 *
	 */
	public Integer getSoftwareId () {
		return this.softwareId;
	}




	/**
	 * software
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="softwareid", insertable=false, updatable=false)
	protected Software software;

	/**
	 * setSoftware (Software software)
	 *
	 * @param software
	 *
	 */
	public void setSoftware (Software software) {
		this.software = software;
		this.softwareId = software.getId();
	}

	/**
	 * getSoftware ()
	 *
	 * @return software
	 *
	 */
	
	public Software getSoftware () {
		return this.software;
	}


}
