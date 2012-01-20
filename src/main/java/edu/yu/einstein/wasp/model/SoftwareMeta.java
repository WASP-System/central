
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="softwaremeta")
public class SoftwareMeta extends MetaBase {

	/** 
	 * softwareMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer softwareMetaId;

	/**
	 * setSoftwareMetaId(Integer softwareMetaId)
	 *
	 * @param softwareMetaId
	 *
	 */
	
	public void setSoftwareMetaId (Integer softwareMetaId) {
		this.softwareMetaId = softwareMetaId;
	}

	/**
	 * getSoftwareMetaId()
	 *
	 * @return softwareMetaId
	 *
	 */
	public Integer getSoftwareMetaId () {
		return this.softwareMetaId;
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
		this.softwareId = software.softwareId;
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
