
/**
 *
 * FileHandle.java 
 * @author echeng (table2type.pl)
 *  
 * the FileHandle
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="file")
public class FileHandle extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1239304568632087021L;
	
	public FileHandle() {
		
	}

	/**
	 * setFileId(Integer fileId)
	 *
	 * @param fileId
	 *
	 */
	@Deprecated
	public void setFileId (Integer fileId) {
		setId(fileId);
	}

	/**
	 * getFileId()
	 *
	 * @return fileId
	 *
	 */
	@Deprecated
	public Integer getFileId () {
		return getId();
	}
	
	/** 
	 * FileHandleMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="fileid", insertable=false, updatable=false)
	protected List<FileHandleMeta> fileHandleMeta;


	/** 
	 * getFileMeta()
	 *
	 * @return FileHandleMeta
	 *
	 */
	@JsonIgnore
	public List<FileHandleMeta> getFileHandleMeta() {
		return this.fileHandleMeta;
	}


	/** 
	 * setFileMeta
	 *
	 * @param fileMeta
	 *
	 */
	public void setFileMeta (List<FileHandleMeta> fileHandleMeta) {
		this.fileHandleMeta = fileHandleMeta;
	}


	@Transient
	private transient String transientName;
	
	public String getTransientName() {
		return this.transientName;
	}
	
	public void setTransientName(String transientName) {
		this.transientName = transientName;
	}
	
	/** 
	 * s
	 *
	 */
	@Column(name="file_uri", length=2048)
	protected String fileURI;

	/**
	 * setFileURI(String fileURI) 
	 * 
	 * When the file is known to the current system (i.e. represents an actual file on a configured work host,
	 * this is set to a file URL (e.g. file://remote.host.fqdn.net/path/to/file) where the path to file is configured
	 * as the remote host would expect.  The path may either be absolute from the root or absolute from the wasp user's home 
	 * directory. When the file represents a resource located on a remote host, the value is a URN defining an authority and a mechanism
	 * for resolving the file.  When URN-based files are used, the file should be resolved, then tested for a local
	 * temporary copy, then downloaded if necessary.
	 *
	 * @param fileURI
	 *
	 */
	
	public void setFileURI(URI fileURI) {
		this.fileURI = fileURI.toString();
	}

	/**
	 * getFileURI() 
	 * 
	 * When the file is known to the current system (i.e. represents an actual file on a configured work host,
	 * this is set to a file URL (e.g. file://remote.host.fqdn.net/path/to/file) where the path to file is configured
	 * as the remote host would expect.  The path may either be absolute from the root or absolute from the wasp user's home 
	 * directory. When the file represents a resource located on a remote host, the value is a URN defining an authority and a mechanism
	 * for resolving the file.  When URN-based files are used, the file should be resolved, then tested for a local
	 * temporary copy, then downloaded if necessary.
	 *
	 * @return fileURI
	 *
	 */
	public URI getFileURI () {
		if (this.fileURI == null)
			return null;
		return URI.create(this.fileURI);
	}
	
	@Column(name="file_name")
	protected String fileName;
	
	/**
	 * Get the string representation of the original name of this file
	 * This represents how the user sees the file name, not how remote systems see the name.
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * Set the string representation of the original name of this file.
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@ManyToOne
	@JoinColumn(name="filetypeid")
	protected FileType fileType;
	
	/**
	 * @return the fileType
	 */
	public FileType getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	/** 
	 * sizek
	 *
	 */
	@Column(name="sizek")
	protected Integer sizek;

	/**
	 * setSizek(Integer sizek)
	 *
	 * @param sizek
	 *
	 */
	
	public void setSizek (Integer sizek) {
		this.sizek = sizek;
	}

	/**
	 * getSizek()
	 *
	 * @return sizek
	 *
	 */
	public Integer getSizek () {
		return this.sizek;
	}
	
	@Column(name="archived")
	protected boolean archived = false;
	
	/**
	 * @param archived
	 */
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	
	/**
	 * @return
	 */
	public boolean isArchived() {
		return this.archived;
	}
	
	@Column(name="deleted")
	protected boolean deleted = false;
	
	/**
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * @return
	 */
	public boolean isDeleted() {
		return this.deleted;
	}

	/** 
	 * md5hash
	 *
	 */
	@Column(name="md5hash")
	protected String md5hash;

	/**
	 * setMd5hash(String md5hash)
	 *
	 * @param md5hash
	 *
	 */
	
	public void setMd5hash (String md5hash) {
		this.md5hash = md5hash;
	}

	/**
	 * getMd5hash()
	 *
	 * @return md5hash
	 *
	 */
	public String getMd5hash () {
		return this.md5hash;
	}

	@ManyToMany(mappedBy="fileHandles")
	protected Set<FileGroup> fileGroup;

	/**
	 * @return the fileGroup
	 */
	public Set<FileGroup> getFileGroup() {
		return fileGroup;
	}

	/**
	 * @param fileGroup the fileGroup to set
	 */
	public void setFileGroup(Set<FileGroup> fileGroup) {
		this.fileGroup = fileGroup;
	}
	
	

}
