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

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class MetaBase extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6102066799409219687L;

	public MetaBase() {

	}

	public MetaBase(String k, Integer pos) {
		this.k = k;
		this.position = pos==null?-1:pos;
	}

	@Column(name = "k")
	private String k;

	public void setK(String k) {
		this.k = k;
	}

	public String getK() {
		return this.k;
	}

	@Lob
	@Column(name = "v")
	private String v;

	public void setV(String v) {
		this.v = v;
	}

	public String getV() {
		return this.v;
	}

	@Column(name = "position")
	private Integer position = new Integer(0);

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getPosition() {
		return this.position;
	}

	@Column(name="rolevisibility")
	private String roleVisibility;
	
	public void setRoleVisibility(String roleVisibility) {
		this.roleVisibility = roleVisibility;
	}

	public String getRoleVisibility() {
		return this.roleVisibility;
	}

	@Transient
	private MetaAttribute property;

	public MetaAttribute getProperty() {
		return property;
	}

	public void setProperty(MetaAttribute property) {
		this.property = property;
	}
	

	@Override
	public String toString() {
		String message = "MetaBase ["
				+ "k=" + k + ", v=" + v + ", position=" + position
				+ ", lastUpdTs=" + updated + ", lastUpdUser=";
		if (lastUpdatedByUser == null || lastUpdatedByUser.getId()==null ) 
			message += "{not set}";
		else 
			message += lastUpdatedByUser.getId();
		message += ", property=" + property + "]";
		return message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((k == null) ? 0 : k.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaBase other = (MetaBase) obj;
		if (k == null) {
			if (other.k != null)
				return false;
		} else if (!k.equals(other.k))
			return false;
		return true;
	}

	
	
}
