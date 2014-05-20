
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
	 * setSampleSubtypeMetaId(Integer sampleSubtypeMetaId)
	 *
	 * @param sampleSubtypeMetaId
	 *
	 */
	@Deprecated
	public void setSampleSubtypeMetaId (Integer sampleSubtypeMetaId) {
		setId(sampleSubtypeMetaId);
	}

	/**
	 * getSampleSubtypeMetaId()
	 *
	 * @return sampleSubtypeMetaId
	 *
	 */
	@Deprecated
	public Integer getSampleSubtypeMetaId () {
		return getId();
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
		this.sampleSubtypeId = sampleSubtype.getId();
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
