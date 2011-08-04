
/**
 *
 * Labmeta.java 
 * @author echeng (table2type.pl)
 *  
 * the Labmeta object
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
@Table(name="labmeta")
public class Labmeta extends MetaBase {
	
	public Labmeta() {
		super();
	}
	
	public Labmeta(String k, Integer pos) {
		super(k,pos);
	}
	
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int labmetaId;
  public void setLabmetaId (int labmetaId) {
    this.labmetaId = labmetaId;
  }
  public int getLabmetaId () {
    return this.labmetaId;
  }


  @Column(name="labid")
  protected int labId;
  public void setLabId (int labId) {
    this.labId = labId;
  }
  public int getLabId () {
    return this.labId;
  }


  @NotAudited
  @ManyToOne
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected Lab lab;
  public void setLab (Lab lab) {
    this.lab = lab;
    this.labId = lab.labId;
  }
  public Lab getLab () {
    return this.lab;
  }

}
