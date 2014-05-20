
/**
 *
 * Roleset.java 
 * @author echeng (table2type.pl)
 *  
 * the Roleset
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
@Table(name="roleset")
public class Roleset extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6496614966405537598L;
	

	/**
	 * setRolesetId(Integer rolesetId)
	 *
	 * @param rolesetId
	 *
	 */
	@Deprecated
	public void setRolesetId (Integer rolesetId) {
		setId(rolesetId);
	}

	/**
	 * getRolesetId()
	 *
	 * @return rolesetId
	 *
	 */
	@Deprecated
	public Integer getRolesetId () {
		return getId();
	}




	/** 
	 * parentroleId
	 *
	 */
	@Column(name="parentroleid")
	protected Integer parentroleId;

	/**
	 * setParentroleId(Integer parentroleId)
	 *
	 * @param parentroleId
	 *
	 */
	
	public void setParentroleId (Integer parentroleId) {
		this.parentroleId = parentroleId;
	}

	/**
	 * getParentroleId()
	 *
	 * @return parentroleId
	 *
	 */
	public Integer getParentroleId () {
		return this.parentroleId;
	}




	/** 
	 * childroleId
	 *
	 */
	@Column(name="childroleid")
	protected Integer childroleId;

	/**
	 * setChildroleId(Integer childroleId)
	 *
	 * @param childroleId
	 *
	 */
	
	public void setChildroleId (Integer childroleId) {
		this.childroleId = childroleId;
	}

	/**
	 * getChildroleId()
	 *
	 * @return childroleId
	 *
	 */
	public Integer getChildroleId () {
		return this.childroleId;
	}




	/**
	 * role
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="parentroleid", insertable=false, updatable=false)
	protected Role role;

	/**
	 * setRole (Role role)
	 *
	 * @param role
	 *
	 */
	public void setRole (Role role) {
		this.role = role;
		this.parentroleId = role.getId();
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


	/**
	 * roleVia
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="childroleid", insertable=false, updatable=false)
	protected Role roleVia;

	/**
	 * setRoleVia (Role role)
	 *
	 * @param role
	 *
	 */
	public void setRoleVia (Role role) {
		this.role = role;
		this.childroleId = role.getId();
	}

	/**
	 * getRoleVia ()
	 *
	 * @return role
	 *
	 */
	
	public Role getRoleVia () {
		return this.role;
	}


}
