
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
    
    import javax.persistence.Column;
import javax.persistence.Entity;
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
		 * 
		 */
		private static final long serialVersionUID = 8037977046022874756L;
	
        /**
        * setConfirmEmailAuthId(Integer confirmEmailAuthId)
        *
        * @param confirmEmailAuthId
        *
        */
		@Deprecated
        public void setConfirmEmailAuthId (Integer confirmEmailAuthId) {
            setId(confirmEmailAuthId);
        }
        
        /**
        * getConfirmEmailAuthId()
        *
        * @return confirmEmailAuthId
        *
        */
		@Deprecated
        public Integer getConfirmEmailAuthId () {
            return getId();
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
            this.userId = user.getId();
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
            this.userPendingId = userPending.getId();
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
