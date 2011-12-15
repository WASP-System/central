
/**
 *
 * RunLanefile.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLanefile
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="runlanefile")
public class RunLanefile extends WaspModel {

	/** 
	 * runLanefileId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer runLanefileId;

	/**
	 * setRunLanefileId(Integer runLanefileId)
	 *
	 * @param runLanefileId
	 *
	 */
	
	public void setRunLanefileId (Integer runLanefileId) {
		this.runLanefileId = runLanefileId;
	}

	/**
	 * getRunLanefileId()
	 *
	 * @return runLanefileId
	 *
	 */
	public Integer getRunLanefileId () {
		return this.runLanefileId;
	}




	/** 
	 * runlaneId
	 *
	 */
	@Column(name="runlaneid")
	protected Integer runlaneId;

	/**
	 * setRunlaneId(Integer runlaneId)
	 *
	 * @param runlaneId
	 *
	 */
	
	public void setRunlaneId (Integer runlaneId) {
		this.runlaneId = runlaneId;
	}

	/**
	 * getRunlaneId()
	 *
	 * @return runlaneId
	 *
	 */
	public Integer getRunlaneId () {
		return this.runlaneId;
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
	 * runLane
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="runlaneid", insertable=false, updatable=false)
	protected RunLane runLane;

	/**
	 * setRunLane (RunLane runLane)
	 *
	 * @param runLane
	 *
	 */
	public void setRunLane (RunLane runLane) {
		this.runLane = runLane;
		this.runlaneId = runLane.runLaneId;
	}

	/**
	 * getRunLane ()
	 *
	 * @return runLane
	 *
	 */
	
	public RunLane getRunLane () {
		return this.runLane;
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
