
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="acct_quoteuser")
public class AcctQuoteUser extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2132481097171369036L;

	/**
	 * setQuoteUserId(Integer quoteUserId)
	 *
	 * @param quoteUserId
	 *
	 */
	@Deprecated
	public void setQuoteUserId (Integer quoteUserId) {
		setId(quoteUserId);
	}

	/**
	 * getQuoteUserId()
	 *
	 * @return quoteUserId
	 *
	 */
	@Deprecated
	public Integer getQuoteUserId () {
		return getId();
	}




	/** 
	 * quoteId
	 *
	 */
	@Column(name="quoteid")
	protected Integer quoteId;

	/**
	 * setQuoteId(Integer quoteId)
	 *
	 * @param quoteId
	 *
	 */
	
	public void setQuoteId (Integer quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * getQuoteId()
	 *
	 * @return quoteId
	 *
	 */
	public Integer getQuoteId () {
		return this.quoteId;
	}




	/** 
	 * UserId
	 *
	 */
	@Column(name="userid")
	protected Integer userId;

	/**
	 * setUserId(Integer UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (Integer userId) {
		this.userId = userId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public Integer getUserId () {
		return this.userId;
	}




	/** 
	 * roleId
	 *
	 */
	@Column(name="roleid")
	protected Integer roleId;

	/**
	 * setRoleId(Integer roleId)
	 *
	 * @param roleId
	 *
	 */
	
	public void setRoleId (Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * getRoleId()
	 *
	 * @return roleId
	 *
	 */
	public Integer getRoleId () {
		return this.roleId;
	}




	/** 
	 * isApproved
	 *
	 */
	@Column(name="isapproved")
	protected Integer isApproved;

	/**
	 * setIsApproved(Integer isApproved)
	 *
	 * @param isApproved
	 *
	 */
	
	public void setIsApproved (Integer isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * getIsApproved()
	 *
	 * @return isApproved
	 *
	 */
	public Integer getIsApproved () {
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
		this.quoteId = acctQuote.getId();
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
	protected WUser user;

	/**
	 * setUser (WUser user)
	 *
	 * @param user
	 *
	 */
	public void setUser (WUser user) {
		this.user = user;
		this.userId = user.getId();
	}

	/**
	 * getUser ()
	 *
	 * @return user
	 *
	 */
	
	public WUser getUser () {
		return this.user;
	}


	/**
	 * role
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="roleid", insertable=false, updatable=false)
	protected WRole role;

	/**
	 * setRole (WRole role)
	 *
	 * @param role
	 *
	 */
	public void setRole (WRole role) {
		this.role = role;
		this.roleId = role.getId();
	}

	/**
	 * getRole ()
	 *
	 * @return role
	 *
	 */
	
	public WRole getRole () {
		return this.role;
	}


}
