
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
	 * 
	 */
	private static final long serialVersionUID = -1362264666795323811L;
	

	/**
	 * setRunCellfileId(Integer runCellfileId)
	 *
	 * @param runCellfileId
	 *
	 */
	@Deprecated
	public void setRunCellfileId (Integer runCellfileId) {
		setId(runCellfileId);
	}

	/**
	 * getRunCellfileId()
	 *
	 * @return runCellfileId
	 *
	 */
	@Deprecated
	public Integer getRunCellfileId () {
		return getId();
	}




	/** 
	 * runcellId
	 *
	 */
	@Column(name="runcellid")
	protected Integer runcellId;

	/**
	 * setRuncellId(Integer runcellId)
	 *
	 * @param runcellId
	 *
	 */
	
	public void setRuncellId (Integer runcellId) {
		this.runcellId = runcellId;
	}

	/**
	 * getRuncellId()
	 *
	 * @return runcellId
	 *
	 */
	public Integer getRuncellId () {
		return this.runcellId;
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
		this.runcellId = runCell.getId();
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
	@JoinColumn(name="filegroupid")
	protected FileGroup fileGroup;

	/**
	 * setFile (FileGroup fileGroup)
	 *
	 * @param fileGroup
	 *
	 */
	public void setFile (FileGroup fileGroup) {
		this.fileGroup = fileGroup;
	}

	/**
	 * getFile ()
	 *
	 * @return file
	 *
	 */
	
	public FileGroup getFileGroup() {
		return this.fileGroup;
	}


}
