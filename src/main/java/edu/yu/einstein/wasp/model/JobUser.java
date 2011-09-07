
/**
 *
 * JobUser.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUser
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
@Table(name="jobuser")
public class JobUser extends WaspModel {

	/** 
	 * jobUserId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int jobUserId;

	/**
	 * setJobUserId(int jobUserId)
	 *
	 * @param jobUserId
	 *
	 */
	
	public void setJobUserId (int jobUserId) {
		this.jobUserId = jobUserId;
	}

	/**
	 * getJobUserId()
	 *
	 * @return jobUserId
	 *
	 */
	public int getJobUserId () {
		return this.jobUserId;
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
	 * UserId
	 *
	 */
	@Column(name="userid")
	protected int UserId;

	/**
	 * setUserId(int UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (int UserId) {
		this.UserId = UserId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public int getUserId () {
		return this.UserId;
	}




	/** 
	 * roleId
	 *
	 */
	@Column(name="roleid")
	protected int roleId;

	/**
	 * setRoleId(int roleId)
	 *
	 * @param roleId
	 *
	 */
	
	public void setRoleId (int roleId) {
		this.roleId = roleId;
	}

	/**
	 * getRoleId()
	 *
	 * @return roleId
	 *
	 */
	public int getRoleId () {
		return this.roleId;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected int lastUpdUser;

	/**
	 * setLastUpdUser(int lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (int lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public int getLastUpdUser () {
		return this.lastUpdUser;
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
	 * user
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="userid", insertable=false, updatable=false)
	protected User user;

	/**
	 * setUser (User user)
	 *
	 * @param user
	 *
	 */
	public void setUser (User user) {
		this.user = user;
		this.UserId = user.UserId;
	}

	/**
	 * getUser ()
	 *
	 * @return user
	 *
	 */
	
	public User getUser () {
		return this.user;
	}


	/**
	 * role
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="roleid", insertable=false, updatable=false)
	protected Role role;

	/**
	 * setRole (Role role)
	 *
	 * @param role
	 *
	 */
	public void setRole (Role role) {
		this.role = role;
		this.roleId = role.roleId;
	}

	/**
	 * getRole ()
	 *
	 * @return role
	 *
	 */
	
	public Role getRole () {
		return this.role;
	}


}
