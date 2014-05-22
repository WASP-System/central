
/**
 *
 * SampleMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMeta
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
@Table(name="samplemeta")
public class SampleMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7928191991410235394L;

	/**
	 * setSampleMetaId(Integer sampleMetaId)
	 *
	 * @param sampleMetaId
	 *
	 */
	@Deprecated
	public void setSampleMetaId (Integer sampleMetaId) {
		setId(sampleMetaId);
	}

	/**
	 * getSampleMetaId()
	 *
	 * @return sampleMetaId
	 *
	 */
	@Deprecated
	public Integer getSampleMetaId () {
		return getId();
	}




	/** 
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected Integer sampleId;

	/**
	 * setSampleId(Integer sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (Integer sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public Integer getSampleId () {
		return this.sampleId;
	}




	/**
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected Sample sample;

	/**
	 * setSample (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSample (Sample sample) {
		this.sample = sample;
		this.sampleId = sample.getId();
	}

	/**
	 * getSample ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSample () {
		return this.sample;
	}


}
