/**
 * 
 */
package edu.yu.einstein.wasp.mps.bwa;

import edu.yu.einstein.wasp.software.alignment.ReferenceBasedAligner;

/**
 * @author calder
 *
 */
public class BWASoftwareComponent extends ReferenceBasedAligner {

	private String version = "0.6.2"; // hard coded as this is likely the final version.
	private String name = "bwa"; 
	
	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.work.SoftwareComponent#getName()
	 */
	@Override
	public String getSoftwareName() {
		return name;
	}
	
	@Override
	public void setSoftwareName(String name) {
		this.name = name;
		
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

}
