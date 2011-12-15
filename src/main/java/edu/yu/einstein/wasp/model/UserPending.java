
/**
 *
 * UserPending.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPending
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
@Table(name="userpending")
public class UserPending extends WaspModel {

	/** 
	 * userPendingId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer userPendingId;

	/**
	 * setUserPendingId(Integer userPendingId)
	 *
	 * @param userPendingId
	 *
	 */
	
	public void setUserPendingId (Integer userPendingId) {
		this.userPendingId = userPendingId;
	}

	/**
	 * getUserPendingId()
	 *
	 * @return userPendingId
	 *
	 */
	public Integer getUserPendingId () {
		return this.userPendingId;
	}




	/** 
	 * email
	 *
	 */
	@Column(name="email")
	@NotEmpty
	@Email
	protected String email;

	/**
	 * setEmail(String email)
	 *
	 * @param email
	 *
	 */
	
	public void setEmail (String email) {
		this.email = email;
	}

	/**
	 * getEmail()
	 *
	 * @return email
	 *
	 */
	public String getEmail () {
		return this.email;
	}

	/** 
	 * Login
	 *
	 */
	@Column(name="login")
	@NotEmpty
	protected String login;

	/**
	 * setLogin(String login)
	 *
	 * @param login
	 *
	 */
	
	public void setLogin (String login) {
		this.login = login;
	}
	
	

	/**
	 * getLogin()
	 *
	 * @return login
	 *
	 */
	public String getLogin () {
		return this.login;
	}


	/** 
	 * password
	 *
	 */
	@Column(name="password")
	@NotEmpty
	protected String password;

	/**
	 * setPassword(String password)
	 *
	 * @param password
	 *
	 */
	
	public void setPassword (String password) {
		this.password = password;
	}

	/**
	 * getPassword()
	 *
	 * @return password
	 *
	 */
	public String getPassword () {
		return this.password;
	}




	/** 
	 * firstName
	 *
	 */
	@Column(name="firstname")
	@NotEmpty
	protected String firstName;

	/**
	 * setFirstName(String firstName)
	 *
	 * @param firstName
	 *
	 */
	
	public void setFirstName (String firstName) {
		this.firstName = firstName;
	}
	
	

	/**
	 * getFirstName()
	 *
	 * @return firstName
	 *
	 */
	public String getFirstName () {
		return this.firstName;
	}




	/** 
	 * lastName
	 *
	 */
	@Column(name="lastname")
	@NotEmpty
	protected String lastName;

	/**
	 * setLastName(String lastName)
	 *
	 * @param lastName
	 *
	 */
	
	public void setLastName (String lastName) {
		this.lastName = lastName;
	}

	/**
	 * getLastName()
	 *
	 * @return lastName
	 *
	 */
	public String getLastName () {
		return this.lastName;
	}




	/** 
	 * locale
	 *
	 */
	@Column(name="locale")
	@NotEmpty
	protected String locale;

	/**
	 * setLocale(String locale)
	 *
	 * @param locale
	 *
	 */
	
	public void setLocale (String locale) {
		this.locale = locale;
	}

	/**
	 * getLocale()
	 *
	 * @return locale
	 *
	 */
	public String getLocale () {
		return this.locale;
	}




	/** 
	 * labId
	 *
	 */
	@Column(name="labid")
	protected Integer labId;

	/**
	 * setLabId(Integer labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (Integer labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public Integer getLabId () {
		return this.labId;
	}




	/** 
	 * status
	 *
	 */
	@Column(name="status")
	protected String status;

	/**
	 * setStatus(String status)
	 *
	 * @param status
	 *
	 */
	
	public void setStatus (String status) {
		this.status = status;
	}

	/**
	 * getStatus()
	 *
	 * @return status
	 *
	 */
	public String getStatus () {
		return this.status;
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
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
	}




	/**
	 * lab
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected Lab lab;

	/**
	 * setLab (Lab lab)
	 *
	 * @param lab
	 *
	 */
	public void setLab (Lab lab) {
		this.lab = lab;
		this.labId = lab.labId;
	}

	/**
	 * getLab ()
	 *
	 * @return lab
	 *
	 */
	
	public Lab getLab () {
		return this.lab;
	}


	/** 
	 * userPendingMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="userpendingid", insertable=false, updatable=false)
	protected List<UserPendingMeta> userPendingMeta;


	/** 
	 * getUserPendingMeta()
	 *
	 * @return userPendingMeta
	 *
	 */
	public List<UserPendingMeta> getUserPendingMeta() {
		return this.userPendingMeta;
	}


	/** 
	 * setUserPendingMeta
	 *
	 * @param userPendingMeta
	 *
	 */
	public void setUserPendingMeta (List<UserPendingMeta> userPendingMeta) {
		this.userPendingMeta = userPendingMeta;
	}



	/** 
	 * labPending
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="userpendingid", insertable=false, updatable=false)
	protected List<LabPending> labPending;


	/** 
	 * getLabPending()
	 *
	 * @return labPending
	 *
	 */
	public List<LabPending> getLabPending() {
		return this.labPending;
	}


	/** 
	 * setLabPending
	 *
	 * @param labPending
	 *
	 */
	public void setLabPending (List<LabPending> labPending) {
		this.labPending = labPending;
	}



}
