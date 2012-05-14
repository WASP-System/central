
/**
 *
 * RunCell.java 
 * @author echeng (table2type.pl)
 *  
 * the RunCell
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="runcell")
public class RunCell extends WaspModel {

	/** 
	 * runCellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer runCellId;

	/**
	 * setRunCellId(Integer runCellId)
	 *
	 * @param runCellId
	 *
	 */
	
	public void setRunCellId (Integer runCellId) {
		this.runCellId = runCellId;
	}

	/**
	 * getRunCellId()
	 *
	 * @return runCellId
	 *
	 */
	public Integer getRunCellId () {
		return this.runCellId;
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
	 * resourcecellId
	 *
	 */
	@Column(name="resourcecellid")
	protected Integer resourcecellId;

	/**
	 * setResourceCellId(Integer resourcecellId)
	 *
	 * @param resourcecellId
	 *
	 */
	
	public void setResourceCellId (Integer resourcecellId) {
		this.resourcecellId = resourcecellId;
	}

	/**
	 * getResourceCellId()
	 *
	 * @return resourcecellId
	 *
	 */
	public Integer getResourceCellId () {
		return this.resourcecellId;
	}




	/** 
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected Integer sampleId;

	/**
	 * setSampleId(Integer sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (Integer sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public Integer getSampleId () {
		return this.sampleId;
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
	 * resourceCell
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcecellid", insertable=false, updatable=false)
	protected ResourceCell resourceCell;

	/**
	 * setResourceCell (ResourceCell resourceCell)
	 *
	 * @param resourceCell
	 *
	 */
	public void setResourceCell (ResourceCell resourceCell) {
		this.resourceCell = resourceCell;
		this.resourcecellId = resourceCell.resourceCellId;
	}

	/**
	 * getResourceCell ()
	 *
	 * @return resourceCell
	 *
	 */
	
	public ResourceCell getResourceCell () {
		return this.resourceCell;
	}


	/**
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected Sample sample;

	/**
	 * setSample (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSample (Sample sample) {
		this.sample = sample;
		this.sampleId = sample.sampleId;
	}

	/**
	 * getSample ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSample () {
		return this.sample;
	}


	/** 
	 * runCellFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="runcellid", insertable=false, updatable=false)
	protected List<RunCellFile> runCellFile;


	/** 
	 * getRunCellfile()
	 *
	 * @return runCellFile
	 *
	 */
	@JsonIgnore
	public List<RunCellFile> getRunCellFile() {
		return this.runCellFile;
	}


	/** 
	 * setRunCellfile
	 *
	 * @param runCellFile
	 *
	 */
	public void setRunCellFile (List<RunCellFile> runCellFile) {
		this.runCellFile = runCellFile;
	}



	/** 
	 * stateRunCell
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="runcellid", insertable=false, updatable=false)
	protected List<StateRunCell> stateRunCell;


	/** 
	 * getStateruncell()
	 *
	 * @return stateRunCell
	 *
	 */
	@JsonIgnore
	public List<StateRunCell> getStateRunCell() {
		return this.stateRunCell;
	}


	/** 
	 * setStateruncell
	 *
	 * @param stateRunCell
	 *
	 */
	public void setStateRunCell (List<StateRunCell> stateRunCell) {
		this.stateRunCell = stateRunCell;
	}



}
