/**
 * 
 */
package edu.yu.einstein.wasp.plugin.mps;

/**
 * Enum indicating the status of a index for a particular genome build.
 * 
 * UNKNOWN     - This genome is not known to the system, therefore there is no chance that an index will be built.
 * UNBUILDABLE - This genome is known to the system; however, because of missing dependencies it will not be built.
 * BUILDING    - This genome is known, and the system indicates that the index currently being built.  
 * BUILT       - Genome index available for processing.
 * 
 * @author calder
 *
 */
public enum GenomeIndexStatus {
	
	UNKNOWN, UNBUILDABLE, BUILDING, BUILT;
	
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

	public boolean isAvailable() {
		if (this == GenomeIndexStatus.BUILDING || this == GenomeIndexStatus.BUILT)
			return true;
		return false;
	}
	
	public boolean isCurrentlyAvailable() {
		if (this == GenomeIndexStatus.BUILT)
			return true;
		return false;
	}
	
}
