/**
 * 
 * Meta.java
 *  
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "meta")
public class Meta extends MetaBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8135307692214003015L;

	public void setMetaId(Integer metaId) {
		setId(metaId);
	}

	public Integer getMetaId() {
		return getId();
	}

}
