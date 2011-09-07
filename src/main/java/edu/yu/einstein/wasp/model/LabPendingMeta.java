
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
	protected int labPendingMetaId;

	/**
	 * setLabPendingMetaId(int labPendingMetaId)
	 *
	 * @param labPendingMetaId
	 *
	 */
	
	public void setLabPendingMetaId (int labPendingMetaId) {
		this.labPendingMetaId = labPendingMetaId;
	}

	/**
	 * getLabPendingMetaId()
	 *
	 * @return labPendingMetaId
	 *
	 */
	public int getLabPendingMetaId () {
		return this.labPendingMetaId;
	}




	/** 
	 * labpendingId
	 *
	 */
	@Column(name="labpendingid")
	protected int labpendingId;

	/**
	 * setLabpendingId(int labpendingId)
	 *
	 * @param labpendingId
	 *
	 */
	
	public void setLabpendingId (int labpendingId) {
		this.labpendingId = labpendingId;
	}

	/**
	 * getLabpendingId()
	 *
	 * @return labpendingId
	 *
	 */
	public int getLabpendingId () {
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
