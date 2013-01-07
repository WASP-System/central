
/**
 *
 * FileType.java 
 * @author asmclellan
 *  
 * the FileType
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="filetype")
public class FileType extends WaspModel {

	/** 
	 * fileTypeId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer fileTypeId;

	/**
	 * setFileTypeId(Integer fileTypeId)
	 *
	 * @param fileTypeId
	 *
	 */
	
	public void setFileTypeId (Integer fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	/**
	 * getFileTypeId()
	 *
	 * @return fileTypeId
	 *
	 */
	public Integer getFileTypeId () {
		return this.fileTypeId;
	}




	/** 
	 * fileTypeSubtypeId
	 *
	 */
	@Column(name="filetypesubtypeid")
	protected Integer fileTypeSubtypeId;

	/**
	 * setFileTypeSubtypeId(Integer fileTypeSubtypeId)
	 *
	 * @param fileTypeSubtypeId
	 *
	 */
	
	public void setFileTypeSubtypeId (Integer fileTypeSubtypeId) {
		this.fileTypeSubtypeId = fileTypeSubtypeId;
	}

	/**
	 * getFileTypeSubtypeId()
	 *
	 * @return fileTypeSubtypeId
	 *
	 */
	public Integer getFileTypeSubtypeId () {
		return this.fileTypeSubtypeId;
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
	 * file
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filetypeid", insertable=false, updatable=false)
	protected List<File> file;


	/** 
	 * getFile()
	 *
	 * @return file
	 *
	 */
	@JsonIgnore
	public List<File> getFile() {
		return this.file;
	}


	/** 
	 * setFile
	 *
	 * @param file
	 *
	 */
	public void setFile (List<File> file) {
		this.file = file;
	}
	
	/** 
	 * filetypeMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filetypeid", insertable=false, updatable=false)
	protected List<FileTypeMeta> fileTypeMeta;


	/** 
	 * getFileTypeMeta()
	 *
	 * @return fileTypeMeta
	 *
	 */
	@JsonIgnore
	public List<FileTypeMeta> getFileTypeMeta() {
		return this.fileTypeMeta;
	}


	/** 
	 * setFileTypeMeta
	 *
	 * @param fileTypeMeta
	 *
	 */
	public void setFiletypMeta (List<FileTypeMeta> fileTypeMeta) {
		this.fileTypeMeta = fileTypeMeta;
	}

}
