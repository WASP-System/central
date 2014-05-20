
/**
 *
 * AcctWorkflowcost.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcost
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
@Table(name="acct_workflowcost")
public class AcctWorkflowcost extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9017613478598432503L;

	/**
	 * setWorkflowId(Integer workflowId)
	 *
	 * @param workflowId
	 *
	 */
	@Deprecated
	public void setWorkflowId (Integer workflowId) {
		setId(workflowId);
	}

	/**
	 * getWorkflowId()
	 *
	 * @return workflowId
	 *
	 */
	@Deprecated
	public Integer getWorkflowId () {
		return getId();
	}




	/** 
	 * basecost
	 *
	 */
	@Column(name="basecost")
	protected float basecost;

	/**
	 * setBasecost(float basecost)
	 *
	 * @param basecost
	 *
	 */
	
	public void setBasecost (float basecost) {
		this.basecost = basecost;
	}

	/**
	 * getBasecost()
	 *
	 * @return basecost
	 *
	 */
	public float getBasecost () {
		return this.basecost;
	}


	/**
	 * job
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected Job job;

	/**
	 * setJob (Job job)
	 *
	 * @param job
	 *
	 */
	public void setJob (Job job) {
		this.job = job;
		setId(job.getWorkflow().getId());
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


}
