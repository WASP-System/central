
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
@Table(name="jobcell")
public class JobCell extends WaspModel {

	/** 
	 * jobCellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobCellId;

	/**
	 * setJobCellId(Integer jobCellId)
	 *
	 * @param jobCellId
	 *
	 */
	
	public void setJobCellId (Integer jobCellId) {
		this.jobCellId = jobCellId;
	}

	/**
	 * getJobCellId()
	 *
	 * @return jobCellId
	 *
	 */
	public Integer getJobCellId () {
		return this.jobCellId;
	}




	/** 
	 * jobId
	 *
	 */
	@Column(name="jobid")
	protected Integer jobId;

	/**
	 * setJobId(Integer jobId)
	 *
	 * @param jobId
	 *
	 */
	
	public void setJobId (Integer jobId) {
		this.jobId = jobId;
	}

	/**
	 * getJobId()
	 *
	 * @return jobId
	 *
	 */
	public Integer getJobId () {
		return this.jobId;
	}




	/** 
	 * cellindex
	 *
	 */
	@Column(name="cellindex")
	protected Integer cellindex;

	/**
	 * setCellindex(Integer cellindex)
	 *
	 * @param cellindex
	 *
	 */
	
	public void setCellindex (Integer cellindex) {
		this.cellindex = cellindex;
	}

	/**
	 * getCellindex()
	 *
	 * @return cellindex
	 *
	 */
	public Integer getCellindex () {
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
	@JsonIgnore
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
