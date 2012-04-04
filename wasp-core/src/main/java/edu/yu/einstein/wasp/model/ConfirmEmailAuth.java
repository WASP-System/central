
    /**
    *
    * ConfirmEmailAuth.java 
    * @author echeng (table2type.pl)
    *  
    * the ConfirmEmailAuth
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
    @Table(name="confirmemailauth")
    public class ConfirmEmailAuth extends WaspModel {

        /** 
        * confirmEmailAuthId
        *
        */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer confirmEmailAuthId;
        
        /**
        * setConfirmEmailAuthId(Integer confirmEmailAuthId)
        *
        * @param confirmEmailAuthId
        *
        */
        
        public void setConfirmEmailAuthId (Integer confirmEmailAuthId) {
            this.confirmEmailAuthId = confirmEmailAuthId;
        }
        
        /**
        * getConfirmEmailAuthId()
        *
        * @return confirmEmailAuthId
        *
        */
        public Integer getConfirmEmailAuthId () {
            return this.confirmEmailAuthId;
        }
        
        
        
        
        /** 
        * userPendingId
        *
        */
	@Column(name="userpendingid")
	protected Integer userPendingId;
        
        /**
        * setUserpendingId(Integer userPendingId)
        *
        * @param userPendingId
        *
        */
        
        public void setUserpendingId (Integer userPendingId) {
            this.userPendingId = userPendingId;
        }
        
        /**
        * getUserpendingId()
        *
        * @return userPendingId
        *
        */
        public Integer getUserPendingId () {
            return this.userPendingId;
        }
        
        
        
        
        /** 
        * UserId
        *
        */
	@Column(name="userid")
	protected Integer UserId;
        
        /**
        * setUserId(Integer UserId)
        *
        * @param UserId
        *
        */
        
        public void setUserId (Integer UserId) {
            this.UserId = UserId;
        }
        
        /**
        * getUserId()
        *
        * @return UserId
        *
        */
        public Integer getUserId () {
            return this.UserId;
        }
        
        
        
        
        /** 
        * authcode
        *
        */
	@Column(name="authcode")
	protected String authcode;
        
        /**
        * setAuthcode(String authcode)
        *
        * @param authcode
        *
        */
        
        public void setAuthcode (String authcode) {
            this.authcode = authcode;
        }
        
        /**
        * getAuthcode()
        *
        * @return authcode
        *
        */
        public String getAuthcode () {
            return this.authcode;
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
        * userPending
        *
        */
        @NotAudited
        @ManyToOne
        @JoinColumn(name="userpendingid", insertable=false, updatable=false)
        protected UserPending userPending;
        
        /**
        * setUserPending (UserPending userPending)
        *
        * @param userPending
        *
        */
        public void setUserPending (UserPending userPending) {
            this.userPending = userPending;
            this.userPendingId = userPending.userPendingId;
        }
        
        /**
        * getUserPending ()
        *
        * @return userPending
        *
        */
        
        public UserPending getUserPending () {
            return this.userPending;
        }
        
        
}
