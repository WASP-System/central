
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
@Table(name="filegroupmeta")
public class FileGroupMeta extends MetaBase {

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
	public void setFileMetaId (Integer id) {
		setId(id);
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
	@Column(name="filegroupid")
	protected Integer fileGroupId;

	/**
	 * setFileId(Integer fileId)
	 *
	 * @param fileId
	 *
	 */
	
	public void setFileGroupId (Integer fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	/**
	 * getFileId()
	 *
	 * @return fileId
	 *
	 */
	public Integer getFileGroupId () {
		return this.fileGroupId;
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
	 * setFile (FileHandle file)
	 *
	 * @param file
	 *
	 */
	public void setFileGroup (FileGroup fileGroup) {
		this.filegroup = fileGroup;
		this.fileGroupId = fileGroup.getId();
	}

	/**
	 * getFile ()
	 *
	 * @return file
	 *
	 */
	
	public FileGroup getFileGroup () {
		return this.filegroup;
	}


}
