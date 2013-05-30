/**
 * 
 */
package edu.yu.einstein.wasp.mps.tools;

import edu.yu.einstein.wasp.software.alignment.BAMProcessor;

/**
 * @author calder
 *
 */
public class Picard extends BAMProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2501043398244995595L;
	
	private String version = "1.78";
	
	public Picard() {
		this.setSoftwareName("picard");
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.software.SoftwarePackage#getSoftwareVersion()
	 */
	@Override
	public String getSoftwareVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.software.SoftwarePackage#setSoftwareVersion(java.lang.String)
	 */
	@Override
	public void setSoftwareVersion(String softwareVersion) {
		version = softwareVersion;

	}

}
