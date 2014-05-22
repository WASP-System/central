
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="runcell")
public class RunCell extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3947577906733521450L;
	

	/**
	 * setRunCellId(Integer runCellId)
	 *
	 * @param runCellId
	 *
	 */
	@Deprecated
	public void setRunCellId (Integer runCellId) {
		setId(runCellId);
	}

	/**
	 * getRunCellId()
	 *
	 * @return runCellId
	 *
	 */
	@Deprecated
	public Integer getRunCellId () {
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
		this.resourcecellId = resourceCell.getId();
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
		this.sampleId = sample.getId();
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


}
