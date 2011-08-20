
/**
 *
 * UserPendingMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingMeta object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="userpendingmeta")
public class UserPendingMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int userPendingMetaId;
  public void setUserPendingMetaId (int userPendingMetaId) {
    this.userPendingMetaId = userPendingMetaId;
  }
  public int getUserPendingMetaId () {
    return this.userPendingMetaId;
  }


  @Column(name="userpendingid")
  protected int userpendingId;
  public void setUserpendingId (int userpendingId) {
    this.userpendingId = userpendingId;
  }
  public int getUserpendingId () {
    return this.userpendingId;
  }


  @NotAudited
  @ManyToOne
   @JoinColumn(name="userpendingid", insertable=false, updatable=false)
  protected UserPending userPending;
  public void setUserPending (UserPending userPending) {
    this.userPending = userPending;
    this.userpendingId = userPending.userPendingId;
  }
  
  public UserPending getUserPending () {
    return this.userPending;
  }

}
