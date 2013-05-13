/**
 * 
 */
package edu.yu.einstein.wasp.mps.tools;

import edu.yu.einstein.wasp.software.alignment.BAMProcessor;

/**
 * @author calder
 *
 */
public class SAMTools extends BAMProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5558053776438200566L;
	
	public SAMTools() {
		this.setSoftwareName("samtools");
	}

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
