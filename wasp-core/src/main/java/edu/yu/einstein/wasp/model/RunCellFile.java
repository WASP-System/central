
/**
 *
 * RunCellFile.java 
 * @author echeng (table2type.pl)
 *  
 * the RunCellFile
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
@Table(name="runcellfile")
public class RunCellFile extends WaspModel {

	/** 
	 * runCellfileId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer runCellfileId;

	/**
	 * setRunCellfileId(Integer runCellfileId)
	 *
	 * @param runCellfileId
	 *
	 */
	
	public void setRunCellfileId (Integer runCellfileId) {
		this.runCellfileId = runCellfileId;
	}

	/**
	 * getRunCellfileId()
	 *
	 * @return runCellfileId
	 *
	 */
	public Integer getRunCellfileId () {
		return this.runCellfileId;
	}




	/** 
	 * runcellId
	 *
	 */
	@Column(name="runcellid")
	protected Integer runcellId;

	/**
	 * setRunlaneId(Integer runcellId)
	 *
	 * @param runcellId
	 *
	 */
	
	public void setRunlaneId (Integer runcellId) {
		this.runcellId = runcellId;
	}

	/**
	 * getRunlaneId()
	 *
	 * @return runcellId
	 *
	 */
	public Integer getRunlaneId () {
		return this.runcellId;
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
	 * runCell
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="runcellid", insertable=false, updatable=false)
	protected RunCell runCell;

	/**
	 * setRunCell (RunCell runCell)
	 *
	 * @param runCell
	 *
	 */
	public void setRunCell (RunCell runCell) {
		this.runCell = runCell;
		this.runcellId = runCell.runCellId;
	}

	/**
	 * getRunCell ()
	 *
	 * @return runCell
	 *
	 */
	
	public RunCell getRunCell () {
		return this.runCell;
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
