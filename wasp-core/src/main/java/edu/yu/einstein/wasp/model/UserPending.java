
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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
@Table(name="userpending")
public class UserPending extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4648418306915988328L;

	/**
	 * setUserPendingId(Integer userPendingId)
	 *
	 * @param userPendingId
	 *
	 */
	@Deprecated
	public void setUserPendingId (Integer userPendingId) {
		setId(userPendingId);
	}

	/**
	 * getUserPendingId()
	 *
	 * @return userPendingId
	 *
	 */
	@Deprecated
	public Integer getUserPendingId () {
		return getId();
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
	@Column(name="locale", length=5)
	@NotEmpty
	protected String locale = "en_US";

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
	@Column(name="status", length=10)
	protected String status = "PENDING";

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
		this.labId = lab.getId();
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
	@JsonIgnore
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
	@JsonIgnore
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
