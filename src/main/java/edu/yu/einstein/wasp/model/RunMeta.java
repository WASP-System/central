
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="runmeta")
public class RunMeta extends MetaBase {

	/** 
	 * runMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int runMetaId;

	/**
	 * setRunMetaId(int runMetaId)
	 *
	 * @param runMetaId
	 *
	 */
	
	public void setRunMetaId (int runMetaId) {
		this.runMetaId = runMetaId;
	}

	/**
	 * getRunMetaId()
	 *
	 * @return runMetaId
	 *
	 */
	public int getRunMetaId () {
		return this.runMetaId;
	}




	/** 
	 * runId
	 *
	 */
	@Column(name="runid")
	protected int runId;

	/**
	 * setRunId(int runId)
	 *
	 * @param runId
	 *
	 */
	
	public void setRunId (int runId) {
		this.runId = runId;
	}

	/**
	 * getRunId()
	 *
	 * @return runId
	 *
	 */
	public int getRunId () {
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


}
