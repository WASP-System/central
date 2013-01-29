
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
@Table(name="samplesourcemeta")
public class SampleSourceMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4403841226706030596L;
	/** 
	 * sampleSourceMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleSourceMetaId;

	/**
	 * setSampleSourceMetaId(Integer sampleSourceMetaId)
	 *
	 * @param sampleSourceMetaId
	 *
	 */
	
	public void setSampleSourceMetaId (Integer sampleSourceMetaId) {
		this.sampleSourceMetaId = sampleSourceMetaId;
	}

	/**
	 * getSampleSourceMetaId()
	 *
	 * @return sampleSourceMetaId
	 *
	 */
	public Integer getSampleSourceMetaId () {
		return this.sampleSourceMetaId;
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
		this.sampleSourceId = sampleSource.sampleSourceId;
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
