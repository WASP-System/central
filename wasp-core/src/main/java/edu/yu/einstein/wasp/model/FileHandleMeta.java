
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
	public void setFileMetaId (Integer fileMetaId) {
		setId(fileMetaId);
	}

	/**
	 * getFileMetaId()
	 *
	 * @return fileMetaId
	 *
	 */
	@Deprecated
	public Integer getFileMetaId () {
		return getId();
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
		this.fileId = file.getId();
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
