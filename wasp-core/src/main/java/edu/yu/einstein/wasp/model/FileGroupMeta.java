
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
	 * fileMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer id;

	/**
	 * setFileMetaId(Integer fileMetaId)
	 *
	 * @param fileMetaId
	 *
	 */
	
	public void setId (Integer id) {
		this.id = id;
	}

	/**
	 * getFileMetaId()
	 *
	 * @return fileMetaId
	 *
	 */
	public Integer getId () {
		return this.id;
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
	public Integer getFileId () {
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
		this.fileGroupId = fileGroup.fileGroupId;
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
