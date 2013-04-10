/**
 * 
 */
package edu.yu.einstein.wasp.plugin.supplemental.organism;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author calder
 *
 */
public class Organism implements Comparable<Organism>{
	
	private Integer ncbiID;
	
	private Map<String,Genome> genomes = new TreeMap<String,Genome>();
	
	private Integer geneticCode;
	private Integer mitochondrialGeneticCode;
	
	private String name;
	private String alias;
	private String commonName;
	
	public Organism(Integer ncbiID) {
		this.ncbiID = ncbiID;
	}
	
	public Integer getNcbiID() {
		return ncbiID;
	}
	public void setNcbiID(Integer ncbiID) {
		this.ncbiID = ncbiID;
	}
	public Map<String,Genome> getGenomes() {
		return genomes;
	}
	public void setGenomes(Map<String,Genome> genomes) {
		this.genomes = genomes;
	}
	public Integer getGeneticCode() {
		return geneticCode;
	}
	public void setGeneticCode(String geneticCode) {
		this.geneticCode = new Integer(geneticCode);
	}
	public Integer getMitochondrialGeneticCode() {
		return mitochondrialGeneticCode;
	}
	public void setMitochondrialGeneticCode(String mitochondrialGeneticCode) {
		this.mitochondrialGeneticCode = new Integer(mitochondrialGeneticCode);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        
        Organism rhs = (Organism) obj;
        return new EqualsBuilder().
            append(ncbiID, rhs.getNcbiID()).
            isEquals();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(3, 31). 
	            append(ncbiID).
	            toHashCode();
	}

	@Override
	public int compareTo(Organism o) {
		return this.getNcbiID().compareTo(o.getNcbiID());
	}
	
	

}
