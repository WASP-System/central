
/**
 *
 * JobCell.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCell
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
@Table(name="jobcell")
public class JobCell extends WaspModel {

	/** 
	 * jobCellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int jobCellId;

	/**
	 * setJobCellId(int jobCellId)
	 *
	 * @param jobCellId
	 *
	 */
	
	public void setJobCellId (int jobCellId) {
		this.jobCellId = jobCellId;
	}

	/**
	 * getJobCellId()
	 *
	 * @return jobCellId
	 *
	 */
	public int getJobCellId () {
		return this.jobCellId;
	}




	/** 
	 * jobId
	 *
	 */
	@Column(name="jobid")
	protected int jobId;

	/**
	 * setJobId(int jobId)
	 *
	 * @param jobId
	 *
	 */
	
	public void setJobId (int jobId) {
		this.jobId = jobId;
	}

	/**
	 * getJobId()
	 *
	 * @return jobId
	 *
	 */
	public int getJobId () {
		return this.jobId;
	}




	/** 
	 * cellindex
	 *
	 */
	@Column(name="cellindex")
	protected int cellindex;

	/**
	 * setCellindex(int cellindex)
	 *
	 * @param cellindex
	 *
	 */
	
	public void setCellindex (int cellindex) {
		this.cellindex = cellindex;
	}

	/**
	 * getCellindex()
	 *
	 * @return cellindex
	 *
	 */
	public int getCellindex () {
		return this.cellindex;
	}




	/**
	 * job
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected Job job;

	/**
	 * setJob (Job job)
	 *
	 * @param job
	 *
	 */
	public void setJob (Job job) {
		this.job = job;
		this.jobId = job.jobId;
	}

	/**
	 * getJob ()
	 *
	 * @return job
	 *
	 */
	
	public Job getJob () {
		return this.job;
	}


	/** 
	 * sampleCell
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobcellid", insertable=false, updatable=false)
	protected List<SampleCell> sampleCell;


	/** 
	 * getSampleCell()
	 *
	 * @return sampleCell
	 *
	 */
	public List<SampleCell> getSampleCell() {
		return this.sampleCell;
	}


	/** 
	 * setSampleCell
	 *
	 * @param sampleCell
	 *
	 */
	public void setSampleCell (List<SampleCell> sampleCell) {
		this.sampleCell = sampleCell;
	}



}
