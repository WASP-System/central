
/**
 *
 * SampleSubtypeMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSubtypeMeta
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
@Table(name="samplesubtypemeta")
public class SampleSubtypeMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6911764609747570472L;
	/** 
	 * sampleMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleSubtypeMetaId;

	/**
	 * setSampleSubtypeMetaId(Integer sampleSubtypeMetaId)
	 *
	 * @param sampleSubtypeMetaId
	 *
	 */
	
	public void setSampleSubtypeMetaId (Integer sampleSubtypeMetaId) {
		this.sampleSubtypeMetaId = sampleSubtypeMetaId;
	}

	/**
	 * getSampleSubtypeMetaId()
	 *
	 * @return sampleSubtypeMetaId
	 *
	 */
	public Integer getSampleSubtypeMetaId () {
		return this.sampleSubtypeMetaId;
	}




	/** 
	 * sampleSubtypeId
	 *
	 */
	@Column(name="samplesubtypeid")
	protected Integer sampleSubtypeId;

	/**
	 * setSampleSubtypeId(Integer sampleSubtypeId)
	 *
	 * @param sampleSubtypeId
	 *
	 */
	
	public void setSampleSubtypeId (Integer sampleSubtypeId) {
		this.sampleSubtypeId = sampleSubtypeId;
	}

	/**
	 * getSampleSubtypeId()
	 *
	 * @return sampleSubtypeId
	 *
	 */
	public Integer getSampleSubtypeId () {
		return this.sampleSubtypeId;
	}




	/**
	 * sampleSubtype
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleSubtypeid", insertable=false, updatable=false)
	protected SampleSubtype sampleSubtype;

	/**
	 * setSampleSubtype (SampleSubtype sampleSubtype)
	 *
	 * @param sampleSubtype
	 *
	 */
	public void setSampleSubtype (SampleSubtype sampleSubtype) {
		this.sampleSubtype = sampleSubtype;
		this.sampleSubtypeId = sampleSubtype.sampleSubtypeId;
	}

	/**
	 * getSampleSubtype ()
	 *
	 * @return sampleSubtype
	 *
	 */
	
	public SampleSubtype getSampleSubtype () {
		return this.sampleSubtype;
	}


}
