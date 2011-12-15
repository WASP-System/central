
/**
 *
 * LabPendingMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMeta
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
@Table(name="labpendingmeta")
public class LabPendingMeta extends MetaBase {

	/** 
	 * labPendingMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer labPendingMetaId;

	/**
	 * setLabPendingMetaId(Integer labPendingMetaId)
	 *
	 * @param labPendingMetaId
	 *
	 */
	
	public void setLabPendingMetaId (Integer labPendingMetaId) {
		this.labPendingMetaId = labPendingMetaId;
	}

	/**
	 * getLabPendingMetaId()
	 *
	 * @return labPendingMetaId
	 *
	 */
	public Integer getLabPendingMetaId () {
		return this.labPendingMetaId;
	}




	/** 
	 * labpendingId
	 *
	 */
	@Column(name="labpendingid")
	protected Integer labpendingId;

	/**
	 * setLabpendingId(Integer labpendingId)
	 *
	 * @param labpendingId
	 *
	 */
	
	public void setLabpendingId (Integer labpendingId) {
		this.labpendingId = labpendingId;
	}

	/**
	 * getLabpendingId()
	 *
	 * @return labpendingId
	 *
	 */
	public Integer getLabpendingId () {
		return this.labpendingId;
	}




	/**
	 * labPending
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="labpendingid", insertable=false, updatable=false)
	protected LabPending labPending;

	/**
	 * setLabPending (LabPending labPending)
	 *
	 * @param labPending
	 *
	 */
	public void setLabPending (LabPending labPending) {
		this.labPending = labPending;
		this.labpendingId = labPending.labPendingId;
	}

	/**
	 * getLabPending ()
	 *
	 * @return labPending
	 *
	 */
	
	public LabPending getLabPending () {
		return this.labPending;
	}


}
