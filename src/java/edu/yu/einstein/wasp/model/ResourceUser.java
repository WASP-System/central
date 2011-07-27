
/**
 *
 * ResourceUser.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceUser object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import javax.persistence.*;
import java.util.*;
import org.hibernate.criterion.*;

@Entity
@Table(name="resourceuser")
public class ResourceUser extends WaspModel {


  // move to DAO
  public static ResourceUser getResourceUserByResourceUserId (Session session, int resourceUserId) throws HibernateException {
    Criteria crit = session.createCriteria(ResourceUser.class);
    List results = crit.add(Restrictions.eq("resourceUserId", resourceUserId)).list();
    if (results.size() == 0) {
      ResourceUser rt = new ResourceUser();
      return rt;
    }
    return (ResourceUser) results.get(0);
  }

  public static ResourceUser getResourceUserByResourceIdUserId (Session session, int resourceId, int UserId) throws HibernateException {
    Criteria crit = session.createCriteria(ResourceUser.class);
    List results = crit.add(Restrictions.eq("resourceId", resourceId)).add(Restrictions.eq("UserId", UserId)).list();
    if (results.size() == 0) {
      ResourceUser rt = new ResourceUser();
      return rt;
    }
    return (ResourceUser) results.get(0);
  }


  // move to DM

  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int resourceUserId;
  public void setResourceUserId (int resourceUserId) {
    this.resourceUserId = resourceUserId;
  }
  public int getResourceUserId () {
    return this.resourceUserId;
  }


  @Column(name="resourceid")
  protected int resourceId;
  public void setResourceId (int resourceId) {
    this.resourceId = resourceId;
  }
  public int getResourceId () {
    return this.resourceId;
  }


  @Column(name="userid")
  protected int UserId;
  public void setUserId (int UserId) {
    this.UserId = UserId;
  }
  public int getUserId () {
    return this.UserId;
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



  @ManyToOne
  @JoinColumn(name="resourceid", insertable=false, updatable=false)
  protected Resource resource;
  public void setResource (Resource resource) {
    this.resource = resource;
    this.resourceId = resource.resourceId;
  }
  public Resource getResource () {
    return this.resource;
  }

  @ManyToOne
  @JoinColumn(name="userid", insertable=false, updatable=false)
  protected User user;
  public void setUser (User user) {
    this.user = user;
    this.UserId = user.UserId;
  }
  public User getUser () {
    return this.user;
  }

}
