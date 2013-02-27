/**
 * 
 * Meta.java
 *  
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer metaId;

	public void setMetaId(Integer metaId) {
		this.metaId = metaId;
	}

	public Integer getMetaId() {
		return this.metaId;
	}

}
