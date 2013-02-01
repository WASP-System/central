
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
	 * sampleSourceFileId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleSourceFileId;

	/**
	 * setSampleSourceFileId(Integer sampleSourceFileId)
	 *
	 * @param sampleSourceFileId
	 *
	 */
	
	public void setSampleSourceFileId (Integer sampleSourceFileId) {
		this.sampleSourceFileId = sampleSourceFileId;
	}

	/**
	 * getSampleSourceFileId()
	 *
	 * @return sampleSourceFileId
	 *
	 */
	public Integer getSampleSourceFileId () {
		return this.sampleSourceFileId;
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
	 * fileId
	 *
	 */
	@Column(name="fileid")
	protected Integer fileId;

	/**
	 * setFileId(Integer fileId)
	 *
	 * @param fileId
	 *
	 */
	
	public void setFileId (Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * getFileId()
	 *
	 * @return fileId
	 *
	 */
	public Integer getFileId () {
		return this.fileId;
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
		this.sampleSourceId = sampleSource.sampleSourceId;
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
	@JoinColumn(name="fileid", insertable=false, updatable=false)
	protected File file;

	/**
	 * setFile (File file)
	 *
	 * @param file
	 *
	 */
	public void setFile (File file) {
		this.file = file;
		this.fileId = file.fileId;
	}

	/**
	 * getFile ()
	 *
	 * @return file
	 *
	 */
	
	public File getFile () {
		return this.file;
	}


}
