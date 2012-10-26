/**
 * 
 */
package edu.yu.einstein.wasp.software;

import edu.yu.einstein.wasp.load.SoftwareLoader;

/**
 * @author calder
 *
 */
public abstract class SoftwarePackage extends SoftwareLoader {
	
	private String requestedVersion;

	/**
	 * @return the requestedVersion
	 */
	public String getRequestedVersion() {
		return requestedVersion;
	}

	/**
	 * @param requestedVersion the requestedVersion to set
	 */
	public void setRequestedVersion(String requestedVersion) {
		this.requestedVersion = requestedVersion;
	}
	
	
}
