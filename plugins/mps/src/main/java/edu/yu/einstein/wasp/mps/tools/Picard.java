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
	
	private String softwareName = "picard";
	private String version = "1.78";

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.software.SoftwarePackage#getSoftwareVersion()
	 */
	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.software.SoftwarePackage#setSoftwareVersion(java.lang.String)
	 */
	@Override
	public void setSoftwareVersion(String softwareVersion) {
		// TODO Auto-generated method stub

	}

}
