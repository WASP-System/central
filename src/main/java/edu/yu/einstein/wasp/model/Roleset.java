
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="roleset")
public class Roleset extends WaspModel {

	/** 
	 * rolesetId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int rolesetId;

	/**
	 * setRolesetId(int rolesetId)
	 *
	 * @param rolesetId
	 *
	 */
	
	public void setRolesetId (int rolesetId) {
		this.rolesetId = rolesetId;
	}

	/**
	 * getRolesetId()
	 *
	 * @return rolesetId
	 *
	 */
	public int getRolesetId () {
		return this.rolesetId;
	}




	/** 
	 * parentroleId
	 *
	 */
	@Column(name="parentroleid")
	protected int parentroleId;

	/**
	 * setParentroleId(int parentroleId)
	 *
	 * @param parentroleId
	 *
	 */
	
	public void setParentroleId (int parentroleId) {
		this.parentroleId = parentroleId;
	}

	/**
	 * getParentroleId()
	 *
	 * @return parentroleId
	 *
	 */
	public int getParentroleId () {
		return this.parentroleId;
	}




	/** 
	 * childroleId
	 *
	 */
	@Column(name="childroleid")
	protected int childroleId;

	/**
	 * setChildroleId(int childroleId)
	 *
	 * @param childroleId
	 *
	 */
	
	public void setChildroleId (int childroleId) {
		this.childroleId = childroleId;
	}

	/**
	 * getChildroleId()
	 *
	 * @return childroleId
	 *
	 */
	public int getChildroleId () {
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
		this.parentroleId = role.roleId;
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
		this.childroleId = role.roleId;
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
