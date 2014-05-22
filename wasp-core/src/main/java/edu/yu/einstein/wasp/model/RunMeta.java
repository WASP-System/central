
/**
 *
 * RunMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMeta
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
@Table(name="runmeta")
public class RunMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3221677859444192982L;
	

	/**
	 * setRunMetaId(Integer runMetaId)
	 *
	 * @param runMetaId
	 *
	 */
	@Deprecated
	public void setRunMetaId (Integer runMetaId) {
		setId(runMetaId);
	}

	/**
	 * getRunMetaId()
	 *
	 * @return runMetaId
	 *
	 */
	@Deprecated
	public Integer getRunMetaId () {
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


}
