/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata;

/**
 * Enum indicating the status of a index for a particular genome build.
 * 
 * UNKNOWN     - This genome is not known to the system, therefore there is no chance that an index will be built.
 * UNBUILDABLE - This genome is known to the system; however, because of missing dependencies it will not be built.
 * BUILDBALE   - This genome is known to the system and can be built, but no actions taken yet (user should not see this status).
 * BUILDING    - This genome is known, and the system indicates that the index currently being built.  
 * BUILT       - Genome index available for processing.
 * 
 * @author calder
 *
 */
public enum GenomeIndexStatus {
	
	UNKNOWN, UNBUILDABLE, BUILDABLE, BUILDING, BUILT;
	
	public String message = "";
	
	/**
	 * Optional message describing the reason for the given status.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set an optional message describing the reason for the given status.
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * if the genome is BUILDING or BUILD, true
	 * 
	 * otherwise there is no certainty that the genome will ever be built.
	 * 
	 * @return
	 */
	public boolean isAvailable() {
		if (this == GenomeIndexStatus.BUILDING || this == GenomeIndexStatus.BUILT)
			return true;
		return false;
	}
	
	/**
	 * genome == BUILT
	 * @return
	 */
	public boolean isCurrentlyAvailable() {
		if (this == GenomeIndexStatus.BUILT)
			return true;
		return false;
	}
	
}
