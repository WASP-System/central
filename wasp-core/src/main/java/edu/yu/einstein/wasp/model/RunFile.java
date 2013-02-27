
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
	 * setRunlanefileId(Integer runcellfileId)
	 *
	 * @param runcellfileId
	 *
	 */
	@Deprecated
	public void setRunlanefileId (Integer runcellfileId) {
		setId(runcellfileId);
	}

	/**
	 * getRunlanefileId()
	 *
	 * @return runcellfileId
	 *
	 */
	@Deprecated
	public Integer getRunlanefileId () {
		return getId();
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
		this.runId = run.getId();
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
	@JoinColumn(name="filegroupid")
	protected FileGroup fileGroup;

	/**
	 * setFileGroup(FileGroup fileGroup)
	 *
	 * @param fileGroup
	 *
	 */
	public void setFileGroup (FileGroup fileGroup) {
		this.fileGroup = fileGroup;
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
