/**
 * 
 */
package edu.yu.einstein.wasp.mps.illumina;

import edu.yu.einstein.wasp.grid.work.SoftwareComponent;

/**
 * @author calder
 *
 */
public class CasavaSoftwareComponent implements SoftwareComponent {

	private String version = "1.8.2"; // hard coded as this is likely the final version.
	private String name = "casava"; 
	
	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.work.SoftwareComponent#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
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
