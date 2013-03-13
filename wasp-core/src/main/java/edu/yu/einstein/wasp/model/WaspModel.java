/**
 *
 * WaspModel.java
 * @author echeng
 *
 * this is the base class,
 * every class in the system should extend this.
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class WaspModel extends WaspCoreModel implements Serializable {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	protected Integer id;
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	

  /**
	 * 
	 */
	private static final long serialVersionUID = -8306199017533320113L;
  
}
