
/**
 *
 * AcctQuoteUser.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUser
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
@Table(name="acct_quoteuser")
public class AcctQuoteUser extends WaspModel {

	/** 
	 * quoteUserId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int quoteUserId;

	/**
	 * setQuoteUserId(int quoteUserId)
	 *
	 * @param quoteUserId
	 *
	 */
	
	public void setQuoteUserId (int quoteUserId) {
		this.quoteUserId = quoteUserId;
	}

	/**
	 * getQuoteUserId()
	 *
	 * @return quoteUserId
	 *
	 */
	public int getQuoteUserId () {
		return this.quoteUserId;
	}




	/** 
	 * quoteId
	 *
	 */
	@Column(name="quoteid")
	protected int quoteId;

	/**
	 * setQuoteId(int quoteId)
	 *
	 * @param quoteId
	 *
	 */
	
	public void setQuoteId (int quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * getQuoteId()
	 *
	 * @return quoteId
	 *
	 */
	public int getQuoteId () {
		return this.quoteId;
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
	 * isApproved
	 *
	 */
	@Column(name="isapproved")
	protected int isApproved;

	/**
	 * setIsApproved(int isApproved)
	 *
	 * @param isApproved
	 *
	 */
	
	public void setIsApproved (int isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * getIsApproved()
	 *
	 * @return isApproved
	 *
	 */
	public int getIsApproved () {
		return this.isApproved;
	}




	/** 
	 * comment
	 *
	 */
	@Column(name="comment")
	protected String comment;

	/**
	 * setComment(String comment)
	 *
	 * @param comment
	 *
	 */
	
	public void setComment (String comment) {
		this.comment = comment;
	}

	/**
	 * getComment()
	 *
	 * @return comment
	 *
	 */
	public String getComment () {
		return this.comment;
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
	 * acctQuote
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="quoteid", insertable=false, updatable=false)
	protected AcctQuote acctQuote;

	/**
	 * setAcctQuote (AcctQuote acctQuote)
	 *
	 * @param acctQuote
	 *
	 */
	public void setAcctQuote (AcctQuote acctQuote) {
		this.acctQuote = acctQuote;
		this.quoteId = acctQuote.quoteId;
	}

	/**
	 * getAcctQuote ()
	 *
	 * @return acctQuote
	 *
	 */
	
	public AcctQuote getAcctQuote () {
		return this.acctQuote;
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
