
/**
 *
 * FileTypeMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the FileTypeMeta
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
@Table(name="filetypemeta")
public class FileTypeMeta extends MetaBase {

	/** 
	 * fileTypeMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer fileTypeMetaId;

	/**
	 * setFileTypeMetaId(Integer fileTypeMetaId)
	 *
	 * @param fileTypeMetaId
	 *
	 */
	
	public void setFileTypeMetaId (Integer fileTypeMetaId) {
		this.fileTypeMetaId = fileTypeMetaId;
	}

	/**
	 * getFileTypeMetaId()
	 *
	 * @return fileTypeMetaId
	 *
	 */
	public Integer getFileTypeMetaId () {
		return this.fileTypeMetaId;
	}




	/** 
	 * fileTypeId
	 *
	 */
	@Column(name="filetypeid")
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
	 * fileType
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="filetypeid", insertable=false, updatable=false)
	protected FileType fileType;

	/**
	 * setFileType (FileType fileType)
	 *
	 * @param fileType
	 *
	 */
	public void setFileType (FileType fileType) {
		this.fileType = fileType;
		this.fileTypeId = fileType.fileTypeId;
	}

	/**
	 * getFileType ()
	 *
	 * @return fileType
	 *
	 */
	
	public FileType getFileType () {
		return this.fileType;
	}


}
