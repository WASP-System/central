
/**
 *
 * SampleDraftMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMeta
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
@Table(name="sampledraftmeta")
public class SampleDraftMeta extends MetaBase {

	/** 
	 * sampleDraftMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleDraftMetaId;

	/**
	 * setSampleDraftMetaId(Integer sampleDraftMetaId)
	 *
	 * @param sampleDraftMetaId
	 *
	 */
	
	public void setSampleDraftMetaId (Integer sampleDraftMetaId) {
		this.sampleDraftMetaId = sampleDraftMetaId;
	}

	/**
	 * getSampleDraftMetaId()
	 *
	 * @return sampleDraftMetaId
	 *
	 */
	public Integer getSampleDraftMetaId () {
		return this.sampleDraftMetaId;
	}




	/** 
	 * sampledraftId
	 *
	 */
	@Column(name="sampledraftid")
	protected Integer sampledraftId;

	/**
	 * setSampledraftId(Integer sampledraftId)
	 *
	 * @param sampledraftId
	 *
	 */
	
	public void setSampledraftId (Integer sampledraftId) {
		this.sampledraftId = sampledraftId;
	}

	/**
	 * getSampledraftId()
	 *
	 * @return sampledraftId
	 *
	 */
	public Integer getSampledraftId () {
		return this.sampledraftId;
	}




	/**
	 * sampleDraft
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampledraftid", insertable=false, updatable=false)
	protected SampleDraft sampleDraft;

	/**
	 * setSampleDraft (SampleDraft sampleDraft)
	 *
	 * @param sampleDraft
	 *
	 */
	public void setSampleDraft (SampleDraft sampleDraft) {
		this.sampleDraft = sampleDraft;
		this.sampledraftId = sampleDraft.sampleDraftId;
	}

	/**
	 * getSampleDraft ()
	 *
	 * @return sampleDraft
	 *
	 */
	
	public SampleDraft getSampleDraft () {
		return this.sampleDraft;
	}


}
