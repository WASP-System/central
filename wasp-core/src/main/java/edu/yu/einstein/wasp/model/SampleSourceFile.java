
/**
 *
 * SampleSourceFile.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceFile
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
@Table(name="samplesourcefile")
public class SampleSourceFile extends WaspModel {

	/**
	 * setSampleSourceFileId(Integer sampleSourceFileId)
	 *
	 * @param sampleSourceFileId
	 *
	 */
	@Deprecated
	public void setSampleSourceFileId (Integer sampleSourceFileId) {
		setId(sampleSourceFileId);
	}

	/**
	 * getSampleSourceFileId()
	 *
	 * @return sampleSourceFileId
	 *
	 */
	@Deprecated
	public Integer getSampleSourceFileId () {
		return getId();
	}




	/** 
	 * sampleSourceId
	 *
	 */
	@Column(name="sampleSourceid")
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
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive;

	/**
	 * setIsActive(Integer isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public Integer getIsActive () {
		return this.isActive;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
	}




	/**
	 * sampleSource
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleSourceid", insertable=false, updatable=false)
	protected SampleSource sampleSource;

	/**
	 * setSampleSource (SampleSource sampleSource)
	 *
	 * @param sampleSource
	 *
	 */
	public void setSampleSource (SampleSource sampleSource) {
		this.sampleSource = sampleSource;
		this.sampleSourceId = sampleSource.getId();
	}

	/**
	 * getSampleSource ()
	 *
	 * @return sampleSource
	 *
	 */
	
	public SampleSource getSampleSource () {
		return this.sampleSource;
	}


	/**
	 * file
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="filegroupid", insertable=false, updatable=false)
	protected FileGroup filegroup;

	/**
	 * setFile (File file)
	 *
	 * @param file
	 *
	 */
	public void setFile (FileGroup filegroup) {
		this.filegroup = filegroup;
	}

	/**
	 * getFile ()
	 *
	 * @return file
	 *
	 */
	
	public FileGroup getFile () {
		return this.filegroup;
	}


}
