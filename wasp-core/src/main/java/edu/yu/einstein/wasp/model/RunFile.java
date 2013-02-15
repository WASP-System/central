
/**
 *
 * RunFile.java 
 * @author echeng (table2type.pl)
 *  
 * the RunFile
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
@Table(name="runfile")
public class RunFile extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7845795771076186235L;
	/** 
	 * runcellfileId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer runcellfileId;

	/**
	 * setRunlanefileId(Integer runcellfileId)
	 *
	 * @param runcellfileId
	 *
	 */
	
	public void setRunlanefileId (Integer runcellfileId) {
		this.runcellfileId = runcellfileId;
	}

	/**
	 * getRunlanefileId()
	 *
	 * @return runcellfileId
	 *
	 */
	public Integer getRunlanefileId () {
		return this.runcellfileId;
	}




	/** 
	 * runId
	 *
	 */
	@Column(name="runid")
	protected Integer runId;

	/**
	 * setRunId(Integer runId)
	 *
	 * @param runId
	 *
	 */
	
	public void setRunId (Integer runId) {
		this.runId = runId;
	}

	/**
	 * getRunId()
	 *
	 * @return runId
	 *
	 */
	public Integer getRunId () {
		return this.runId;
	}




	/** 
	 * fileId
	 *
	 */
	@Column(name="filegroupid")
	protected Integer fileGroupId;

	/**
	 * setFileGroupId(Integer fileGroupId)
	 *
	 * @param fileGroupId
	 *
	 */
	
	public void setFileGroupId (Integer fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	/**
	 * getFileGroupId()
	 *
	 * @return fileGroupId
	 *
	 */
	public Integer getFileGroupId () {
		return this.fileGroupId;
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
	 * run
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="runid", insertable=false, updatable=false)
	protected Run run;

	/**
	 * setRun (Run run)
	 *
	 * @param run
	 *
	 */
	public void setRun (Run run) {
		this.run = run;
		this.runId = run.runId;
	}

	/**
	 * getRun ()
	 *
	 * @return run
	 *
	 */
	
	public Run getRun () {
		return this.run;
	}


	/**
	 * file
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="filegroupid", insertable=false, updatable=false)
	protected FileGroup fileGroup;

	/**
	 * setFileGroup(FileGroup fileGroup)
	 *
	 * @param fileGroup
	 *
	 */
	public void setFileGroup (FileGroup fileGroup) {
		this.fileGroup = fileGroup;
		this.fileGroupId = fileGroup.fileGroupId;
	}

	/**
	 * getFileGroup()
	 *
	 * @return fileGroup
	 *
	 */
	
	public FileGroup getFileGroup() {
		return this.fileGroup;
	}


}
