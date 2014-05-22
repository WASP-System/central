
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
	 * 
	 */
	private static final long serialVersionUID = -7160637820492874262L;
	

	/**
	 * setSampleDraftMetaId(Integer sampleDraftMetaId)
	 *
	 * @param sampleDraftMetaId
	 *
	 */
	@Deprecated
	public void setSampleDraftMetaId (Integer sampleDraftMetaId) {
		setId(sampleDraftMetaId);
	}

	/**
	 * getSampleDraftMetaId()
	 *
	 * @return sampleDraftMetaId
	 *
	 */
	@Deprecated
	public Integer getSampleDraftMetaId () {
		return getId();
	}




	/** 
	 * sampleDraftId
	 *
	 */
	@Column(name="sampledraftid")
	protected Integer sampleDraftId;

	/**
	 * setSampledraftId(Integer sampleDraftId)
	 *
	 * @param sampleDraftId
	 *
	 */
	
	public void setSampleDraftId (Integer sampleDraftId) {
		this.sampleDraftId = sampleDraftId;
	}

	/**
	 * getSampledraftId()
	 *
	 * @return sampleDraftId
	 *
	 */
	public Integer getSampleDraftId () {
		return this.sampleDraftId;
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
		this.sampleDraftId = sampleDraft.getId();
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
