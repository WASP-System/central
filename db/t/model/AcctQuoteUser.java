
/**
 *
 * AcctQuoteUser.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUser object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.*;

@Entity
@Audited
@Table(name="acct_quoteuser")
public class AcctQuoteUser extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int quoteUserId;
  public void setQuoteUserId (int quoteUserId) {
    this.quoteUserId = quoteUserId;
  }
  public int getQuoteUserId () {
    return this.quoteUserId;
  }


  @Column(name="quoteid")
  protected int quoteId;
  public void setQuoteId (int quoteId) {
    this.quoteId = quoteId;
  }
  public int getQuoteId () {
    return this.quoteId;
  }


  @Column(name="userid")
  protected int UserId;
  public void setUserId (int UserId) {
    this.UserId = UserId;
  }
  public int getUserId () {
    return this.UserId;
  }


  @Column(name="roleid")
  protected int roleId;
  public void setRoleId (int roleId) {
    this.roleId = roleId;
  }
  public int getRoleId () {
    return this.roleId;
  }


  @Column(name="isapproved")
  protected int isApproved;
  public void setIsApproved (int isApproved) {
    this.isApproved = isApproved;
  }
  public int getIsApproved () {
    return this.isApproved;
  }


  @Column(name="comment")
  protected String comment;
  public void setComment (String comment) {
    this.comment = comment;
  }
  public String getComment () {
    return this.comment;
  }


  @Column(name="lastupdts")
  protected Date lastUpdTs;
  public void setLastUpdTs (Date lastUpdTs) {
    this.lastUpdTs = lastUpdTs;
  }
  public Date getLastUpdTs () {
    return this.lastUpdTs;
  }


  @Column(name="lastupduser")
  protected int lastUpdUser;
  public void setLastUpdUser (int lastUpdUser) {
    this.lastUpdUser = lastUpdUser;
  }
  public int getLastUpdUser () {
    return this.lastUpdUser;
  }



  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="quoteid", insertable=false, updatable=false)
  protected AcctQuote acctQuote;
  public void setAcctQuote (AcctQuote acctQuote) {
    this.acctQuote = acctQuote;
    this.quoteId = acctQuote.quoteId;
  }
  public AcctQuote getAcctQuote () {
    return this.acctQuote;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="userid", insertable=false, updatable=false)
  protected User user;
  public void setUser (User user) {
    this.user = user;
    this.UserId = user.UserId;
  }
  public User getUser () {
    return this.user;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="roleid", insertable=false, updatable=false)
  protected Role role;
  public void setRole (Role role) {
    this.role = role;
    this.roleId = role.roleId;
  }
  public Role getRole () {
    return this.role;
  }

}
