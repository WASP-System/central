
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
    import javax.persistence.*;
    
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
	protected int confirmEmailAuthId;
        
        /**
        * setConfirmEmailAuthId(int confirmEmailAuthId)
        *
        * @param confirmEmailAuthId
        *
        */
        
        public void setConfirmEmailAuthId (int confirmEmailAuthId) {
            this.confirmEmailAuthId = confirmEmailAuthId;
        }
        
        /**
        * getConfirmEmailAuthId()
        *
        * @return confirmEmailAuthId
        *
        */
        public int getConfirmEmailAuthId () {
            return this.confirmEmailAuthId;
        }
        
        
        
        
        /** 
        * userpendingId
        *
        */
	@Column(name="userpendingid")
	protected Integer userpendingId;
        
        /**
        * setUserpendingId(Integer userpendingId)
        *
        * @param userpendingId
        *
        */
        
        public void setUserpendingId (Integer userpendingId) {
            this.userpendingId = userpendingId;
        }
        
        /**
        * getUserpendingId()
        *
        * @return userpendingId
        *
        */
        public Integer getUserpendingId () {
            return this.userpendingId;
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
            this.userpendingId = userPending.userPendingId;
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
