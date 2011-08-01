/**
 *
 * Usermeta.java 
 * @author echeng (table2type.pl)
 *  
 * the Usermeta object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.CascadeType;
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
@Table(name = "usermeta")
public class Usermeta extends MetaBase {
	
	public Usermeta() {
		super();
	}
	
	public Usermeta(String k, Integer pos) {
		super(k,pos);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int usermetaId;

	public void setUsermetaId(int usermetaId) {
		this.usermetaId = usermetaId;
	}

	public int getUsermetaId() {
		return this.usermetaId;
	}

	@Column(name = "userid")
	protected int UserId;

	public void setUserId(int UserId) {
		this.UserId = UserId;
	}

	public int getUserId() {
		return this.UserId;
	}

	@NotAudited
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected User user;

	public void setUser(User user) {
		this.user = user;
		this.UserId = user.UserId;
	}

	public User getUser() {
		return this.user;
	}


	@Override
	public String toString() {
		return "Usermeta [usermetaId=" + usermetaId + ", UserId=" + UserId+" "
				+super.toString();
	}

}
