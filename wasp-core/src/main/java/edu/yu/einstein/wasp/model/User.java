/**
 *
 * User.java 
 * @author echeng (table2type.pl)
 *  
 * the User object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
@Table(name="wuser")
public class User extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2757029352222424543L;

	@Deprecated
	public void setUserId(Integer userId) {
		setId(userId);
	}

	@Deprecated
	public Integer getUserId() {
		return getId();
	}

	@Column(name = "login")
	@NotEmpty
	protected String	login;

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return this.login;
	}

	@Column(name = "email")
	@NotEmpty
	@Email
	// @Pattern(regexp=".+@.+\\.[a-z]+")
	protected String	email;

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	@Column(name = "password")
	// @NotEmpty commneted out:
	// we cannot require editor to enter password
	// when it's someone's else's account the editor cannot know the password
	protected String	password;

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	@Column(name = "firstname")
	@NotEmpty
	protected String	firstName;

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	@Column(name = "lastname")
	@NotEmpty
	protected String	lastName;

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return this.lastName;
	}

	@Column(name = "isactive")
	protected Integer isActive = 1;

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected List<UserMeta>	userMeta;

	@JsonIgnore
	public List<UserMeta> getUserMeta() {
		return this.userMeta;
	}

	public void setUserMeta(List<UserMeta> userMeta) {
		this.userMeta = userMeta;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected List<Userrole>	userrole;

	@JsonIgnore
	public List<Userrole> getUserrole() {
		return this.userrole;
	}

	public void setUserrole(List<Userrole> userrole) {
		this.userrole = userrole;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected List<DepartmentUser>	departmentUser;

	@JsonIgnore
	public List<DepartmentUser> getDepartmentUser() {
		return this.departmentUser;
	}

	public void setDepartmentUser(List<DepartmentUser> departmentUser) {
		this.departmentUser = departmentUser;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "primaryuserid", insertable = false, updatable = false)
	protected List<Lab>	lab;

	@JsonIgnore
	public List<Lab> getLab() {
		return this.lab;
	}

	public void setLab(List<Lab> lab) {
		this.lab = lab;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected List<LabUser>	labUser;

	@JsonIgnore
	public List<LabUser> getLabUser() {
		return this.labUser;
	}

	public void setLabUser(List<LabUser> labUser) {
		this.labUser = labUser;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected List<Job>	job;

	@JsonIgnore
	public List<Job> getJob() {
		return this.job;
	}

	public void setJob(List<Job> job) {
		this.job = job;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "submitter_userid", insertable = false, updatable = false)
	protected List<Sample>	sample;

	@JsonIgnore
	public List<Sample> getSample() {
		return this.sample;
	}

	public void setSample(List<Sample> sample) {
		this.sample = sample;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected List<AcctQuote>	acctQuote;

	@JsonIgnore
	public List<AcctQuote> getAcctQuote() {
		return this.acctQuote;
	}

	public void setAcctQuote(List<AcctQuote> acctQuote) {
		this.acctQuote = acctQuote;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected List<AcctQuoteUser>	acctQuoteUser;

	@JsonIgnore
	public List<AcctQuoteUser> getAcctQuoteUser() {
		return this.acctQuoteUser;
	}

	public void setAcctQuoteUser(List<AcctQuoteUser> acctQuoteUser) {
		this.acctQuoteUser = acctQuoteUser;
	}

	@NotAudited
	@OneToMany
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected List<Run>	run;

	@JsonIgnore
	public List<Run> getRun() {
		return this.run;
	}

	public void setRun(List<Run> run) {
		this.run = run;
	}

	@Column(name = "locale", length=5)
	@NotEmpty
	protected String	locale = "en_US";

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		return this.locale;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (getId() == null)
			setId(0);
		result = prime * result + getId();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		// NV
		// UserId = UserId == null ? 0 : UserId;
		if (getId().intValue() != other.getId().intValue())
			return false;
		return true;
	}

	@Override
	public String toString() {
		String message =  "User [UserId=" + getId() + ", login=" + login + ", email=" + email + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName + ", isActive=" + isActive
				+ ", lastUpdTs=" + updated + ", lastUpdUser=";
		if (lastUpdatedByUser == null || lastUpdatedByUser.getId()==null ) 
			message += "{not set}";
		else 
			message += lastUpdatedByUser.getId();
		message += ", userMeta=" + userMeta + ", departmentUser=" + departmentUser + ", lab=" + lab + ", job=" + job + ", sample="
				+ sample + ", acctQuote=" + acctQuote + ", acctQuoteUser=" + acctQuoteUser + ", locale=" + locale + "]";
		return message;
	}
	@JsonIgnore
	public String getNameFstLst() {
		return this.firstName + " " + this.lastName;
	}
	@JsonIgnore
	public String getNameLstCmFst() {
		return this.lastName + ", " + this.firstName;
	}

}
