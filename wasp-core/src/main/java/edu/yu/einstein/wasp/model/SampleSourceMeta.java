
/**
 *
 * SampleSourceMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceMeta
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
@Table(name="samplesourcemeta")
public class SampleSourceMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4403841226706030596L;

	/**
	 * setSampleSourceMetaId(Integer sampleSourceMetaId)
	 *
	 * @param sampleSourceMetaId
	 *
	 */
	@Deprecated
	public void setSampleSourceMetaId (Integer sampleSourceMetaId) {
		setId(sampleSourceMetaId);
	}

	/**
	 * getSampleSourceMetaId()
	 *
	 * @return sampleSourceMetaId
	 *
	 */
	@Deprecated
	public Integer getSampleSourceMetaId () {
		return getId();
	}




	/** 
	 * sampleSourceId
	 *
	 */
	@Column(name="samplesourceid")
	protected Integer sampleSourceId;

	/**
	 * setSampleSourceId(Integer sampleSourceId)
	 *
	 * @param sampleSourceId
	 *
	 */
	
	public void setSampleSourceId (Integer sampleSourceId) {
		this.sampleSourceId = sampleSourceId;
	}

	/**
	 * getSampleSourceId()
	 *
	 * @return sampleSourceId
	 *
	 */
	public Integer getSampleSourceId () {
		return this.sampleSourceId;
	}




	/**
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="samplesourceid", insertable=false, updatable=false)
	protected SampleSource sampleSource;

	/**
	 * setSample (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSample (SampleSource sampleSource) {
		this.sampleSource = sampleSource;
		this.sampleSourceId = sampleSource.getId();
	}

	/**
	 * getSample ()
	 *
	 * @return sample
	 *
	 */
	
	public SampleSource getSampleSource () {
		return this.sampleSource;
	}


}
