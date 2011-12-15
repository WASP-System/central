
/**
 *
 * RunLane.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLane
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
@Table(name="runlane")
public class RunLane extends WaspModel {

	/** 
	 * runLaneId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer runLaneId;

	/**
	 * setRunLaneId(Integer runLaneId)
	 *
	 * @param runLaneId
	 *
	 */
	
	public void setRunLaneId (Integer runLaneId) {
		this.runLaneId = runLaneId;
	}

	/**
	 * getRunLaneId()
	 *
	 * @return runLaneId
	 *
	 */
	public Integer getRunLaneId () {
		return this.runLaneId;
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
	 * resourcelaneId
	 *
	 */
	@Column(name="resourcelaneid")
	protected Integer resourcelaneId;

	/**
	 * setResourcelaneId(Integer resourcelaneId)
	 *
	 * @param resourcelaneId
	 *
	 */
	
	public void setResourcelaneId (Integer resourcelaneId) {
		this.resourcelaneId = resourcelaneId;
	}

	/**
	 * getResourcelaneId()
	 *
	 * @return resourcelaneId
	 *
	 */
	public Integer getResourcelaneId () {
		return this.resourcelaneId;
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
	 * resourceLane
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcelaneid", insertable=false, updatable=false)
	protected ResourceLane resourceLane;

	/**
	 * setResourceLane (ResourceLane resourceLane)
	 *
	 * @param resourceLane
	 *
	 */
	public void setResourceLane (ResourceLane resourceLane) {
		this.resourceLane = resourceLane;
		this.resourcelaneId = resourceLane.resourceLaneId;
	}

	/**
	 * getResourceLane ()
	 *
	 * @return resourceLane
	 *
	 */
	
	public ResourceLane getResourceLane () {
		return this.resourceLane;
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
	 * runLanefile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="runlaneid", insertable=false, updatable=false)
	protected List<RunLanefile> runLanefile;


	/** 
	 * getRunLanefile()
	 *
	 * @return runLanefile
	 *
	 */
	public List<RunLanefile> getRunLanefile() {
		return this.runLanefile;
	}


	/** 
	 * setRunLanefile
	 *
	 * @param runLanefile
	 *
	 */
	public void setRunLanefile (List<RunLanefile> runLanefile) {
		this.runLanefile = runLanefile;
	}



	/** 
	 * staterunlane
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="runlaneid", insertable=false, updatable=false)
	protected List<Staterunlane> staterunlane;


	/** 
	 * getStaterunlane()
	 *
	 * @return staterunlane
	 *
	 */
	public List<Staterunlane> getStaterunlane() {
		return this.staterunlane;
	}


	/** 
	 * setStaterunlane
	 *
	 * @param staterunlane
	 *
	 */
	public void setStaterunlane (List<Staterunlane> staterunlane) {
		this.staterunlane = staterunlane;
	}



}
