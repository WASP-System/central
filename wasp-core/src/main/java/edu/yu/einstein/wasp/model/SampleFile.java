
/**
 *
 * SampleFile.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFile
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;

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
@Table(name="samplefile")
public class SampleFile extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3568113560161274589L;

	/**
	 * setSampleFileId(Integer sampleFileId)
	 *
	 * @param sampleFileId
	 *
	 */
	@Deprecated
	public void setSampleFileId (Integer sampleFileId) {
		setId(sampleFileId);
	}

	/**
	 * getSampleFileId()
	 *
	 * @return sampleFileId
	 *
	 */
	@Deprecated
	public Integer getSampleFileId () {
		return this.getId();
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
	 * iName
	 *
	 */
	@Column(name="iname")
	protected String iName;

	/**
	 * setIName(String iName)
	 *
	 * @param iName
	 *
	 */
	
	public void setIName (String iName) {
		this.iName = iName;
	}

	/**
	 * getIName()
	 *
	 * @return iName
	 *
	 */
	public String getIName () {
		return this.iName;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
	}




	/** 
	 * description
	 *
	 */
	@Column(name="description")
	protected String description;

	/**
	 * setDescription(String description)
	 *
	 * @param description
	 *
	 */
	
	public void setDescription (String description) {
		this.description = description;
	}

	/**
	 * getDescription()
	 *
	 * @return description
	 *
	 */
	public String getDescription () {
		return this.description;
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


	/**
	 * file
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="filegroupid")
	protected FileGroup fileGroup;

	/**
	 * setFileGroup (FileGroup fileGroup)
	 *
	 * @param fileGroup
	 *
	 */
	public void setFileGroup (FileGroup fileGroup) {
		this.fileGroup = fileGroup;
	}

	/**
	 * getFile ()
	 *
	 * @return file
	 *
	 */
	
	public FileGroup getFileGroup() {
		return this.fileGroup;
	}


}
