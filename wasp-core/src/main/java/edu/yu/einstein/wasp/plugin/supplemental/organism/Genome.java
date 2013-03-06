/**
 * 
 */
package edu.yu.einstein.wasp.plugin.supplemental.organism;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author calder
 *
 */
public class Genome implements Comparable<Genome> {
	
	private Organism organism;
	private String name;
	private String description;
	private String alias;
	
	private Set<Build> builds = new TreeSet<Build>();
	
	
	public Genome(String name) {
		this.name = name;
	}
	
	public Organism getOrganism() {
		return organism;
	}
	public void setOrganism(Organism organism) {
		this.organism = organism;
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
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the builds
	 */
	public Set<Build> getBuilds() {
		return builds;
	}

	/**
	 * @param builds the builds to set
	 */
	public void setBuilds(Set<Build> builds) {
		this.builds = builds;
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
            append(name, rhs.getNcbiID()).
            isEquals();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(3, 31). 
	            append(name).
	            toHashCode();
	}

	@Override
	public int compareTo(Genome arg0) {
		return this.getName().compareTo(arg0.getName());
	}
	
	

}
