
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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
	 * 
	 */
	private static final long serialVersionUID = -2057479265391988507L;
	
	/**
	 * setFileTypeId(Integer fileTypeId)
	 *
	 * @param fileTypeId
	 *
	 */
	@Deprecated
	public void setFileTypeId (Integer fileTypeId) {
		setId(fileTypeId);
	}

	/**
	 * getFileTypeId()
	 *
	 * @return fileTypeId
	 *
	 */
	@Deprecated
	public Integer getFileTypeId () {
		return getId();
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
	 * extension
	 *
	 */
	@Column(name="extension")
	protected String extension;

	/**
	 * setExtension(String extension)
	 *
	 * @param extension
	 *
	 */
	
	/**
	 * set a single extension or a comma delimited list if more than one (put the default extension first)
	 * @param extensions
	 */
	public void setExtensions(String extension) {
		this.extension = extension;
	}
	
	/**
	 * get a single extension or comma delimited list if more than one
	 * @param extensions
	 */
	public String getExtensions() {
		return this.extension;
	}
	
	/**
	 * get the first extension of a comma delimited list.
	 * @param extensions
	 */
	@JsonIgnore
	public String getDefaultExtension() {
		for (String extension : getExtensionSet())
			return extension; // return first
		return null;
	}
	
	
	/**
	 * Get the set of extensions (returned in the order provided in the original comma delimited list)
	 * @return
	 */
	@JsonIgnore
	public Set<String> getExtensionSet() {
		Set<String> extensions = new LinkedHashSet<>();
		for (String extension: this.extension.split(","))
			extensions.add(extension);
		return extensions;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 1;

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
	 * file
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filetypeid", insertable=false, updatable=false)
	protected List<FileHandle> file;


	/** 
	 * getFile()
	 *
	 * @return file
	 *
	 */
	@JsonIgnore
	public List<FileHandle> getFile() {
		return this.file;
	}


	/** 
	 * setFile
	 *
	 * @param file
	 *
	 */
	public void setFile (List<FileHandle> file) {
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
