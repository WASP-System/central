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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class MetaBase extends WaspModel {

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

	@Column(name = "v")
	private String v;

	public void setV(String v) {
		this.v = v;
	}

	public String getV() {
		return this.v;
	}

	@Column(name = "position")
	private Integer position;

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getPosition() {
		return this.position;
	}

	@Column(name = "lastupdts")
	private Date lastUpdTs;

	public void setLastUpdTs(Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	public Date getLastUpdTs() {
		return this.lastUpdTs;
	}

	@Column(name = "lastupduser")
	private Integer lastUpdUser;

	public void setLastUpdUser(Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Integer getLastUpdUser() {
		return this.lastUpdUser;
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
		return "MetaBase ["
				+ "k=" + k + ", v=" + v + ", position=" + position
				+ ", lastUpdTs=" + lastUpdTs + ", lastUpdUser=" + lastUpdUser
				+ ", property=" + property + "]";
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
