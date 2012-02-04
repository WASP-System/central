
/**
 *
 * SubtypeSampleMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleMeta
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="subtypesamplemeta")
public class SubtypeSampleMeta extends MetaBase {

	/** 
	 * sampleMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer subtypeSampleMetaId;

	/**
	 * setSubtypeSampleMetaId(Integer subtypeSampleMetaId)
	 *
	 * @param subtypeSampleMetaId
	 *
	 */
	
	public void setSubtypeSampleMetaId (Integer subtypeSampleMetaId) {
		this.subtypeSampleMetaId = subtypeSampleMetaId;
	}

	/**
	 * getSubtypeSampleMetaId()
	 *
	 * @return subtypeSampleMetaId
	 *
	 */
	public Integer getSubtypeSampleMetaId () {
		return this.subtypeSampleMetaId;
	}




	/** 
	 * subtypeSampleId
	 *
	 */
	@Column(name="subtypesampleid")
	protected Integer subtypeSampleId;

	/**
	 * setSubtypeSampleId(Integer subtypeSampleId)
	 *
	 * @param subtypeSampleId
	 *
	 */
	
	public void setSubtypeSampleId (Integer subtypeSampleId) {
		this.subtypeSampleId = subtypeSampleId;
	}

	/**
	 * getSubtypeSampleId()
	 *
	 * @return subtypeSampleId
	 *
	 */
	public Integer getSubtypeSampleId () {
		return this.subtypeSampleId;
	}




	/**
	 * subtypeSample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="subtypeSampleid", insertable=false, updatable=false)
	protected SubtypeSample subtypeSample;

	/**
	 * setSubtypeSample (SubtypeSample subtypeSample)
	 *
	 * @param subtypeSample
	 *
	 */
	public void setSubtypeSample (SubtypeSample subtypeSample) {
		this.subtypeSample = subtypeSample;
		this.subtypeSampleId = subtypeSample.subtypeSampleId;
	}

	/**
	 * getSubtypeSample ()
	 *
	 * @return subtypeSample
	 *
	 */
	
	public SubtypeSample getSubtypeSample () {
		return this.subtypeSample;
	}


}
