
/**
 *
 * FileHandleMeta.java 
 * @author asmclellan
 *  
 * the FileHandleMeta
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
@Table(name="filemeta")
public class FileHandleMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8558940671046911453L;
	
	/**
	 * setFileMetaId(Integer fileMetaId)
	 *
	 * @param fileMetaId
	 *
	 */
	@Deprecated
	public void setFileHandleMetaId (Integer fileMetaId) {
		setId(fileMetaId);
	}

	/**
	 * getFileMetaId()
	 *
	 * @return fileMetaId
	 *
	 */
	@Deprecated
	public Integer getFileHandleMetaId () {
		return getId();
	}




	/** 
	 * fileId
	 *
	 */
	@Column(name="fileid")
	protected Integer fileHandleId;

	/**
	 * setFileId(Integer fileId)
	 *
	 * @param fileId
	 *
	 */
	
	public void setFileHandleId (Integer fileId) {
		this.fileHandleId = fileId;
	}

	/**
	 * getFileId()
	 *
	 * @return fileId
	 *
	 */
	public Integer getFileHandleId () {
		return this.fileHandleId;
	}




	/**
	 * file
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="fileid", insertable=false, updatable=false)
	protected FileHandle file;

	/**
	 * setFile (FileHandle file)
	 *
	 * @param file
	 *
	 */
	public void setFile (FileHandle file) {
		this.file = file;
		this.fileHandleId = file.getId();
	}

	/**
	 * getFile ()
	 *
	 * @return file
	 *
	 */
	
	public FileHandle getFile () {
		return this.file;
	}


}
