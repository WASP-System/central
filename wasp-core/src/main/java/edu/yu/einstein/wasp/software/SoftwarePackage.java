/**
 * 
 */
package edu.yu.einstein.wasp.software;

import edu.yu.einstein.wasp.grid.work.SoftwareManager;
import edu.yu.einstein.wasp.model.Software;

/**
 * @author calder
 *
 */
public abstract class SoftwarePackage extends Software {
	
	private static final long serialVersionUID = 522863647514139874L;
	
	private String softwareVersion;

	/**
	 * A unique name for the software component. Can be overridden at the configuration level 
	 * For example: bilbo.software.casava.name=casava/gnu will override the default name of casava on bilbo. This is
	 * useful when a remote {@link SoftwareManager} uses an alternate name to specify the software package.  Using the 
	 * previous example with ModulesManager would result in "modules load casava/gnu/1.8.2".
	 * 
	 * This method returns the same value as this.getIName().
	 * 
	 * @return
	 */
	public String getSoftwareName() {
		return this.getIName();
	}
	
	public void setSoftwareName(String name) {
		this.setIName(name);
	}
	
	/**
	 * the version string of the software.  Should generally be set at the time the dependency is declared.
	 * It is possible to hard code this value in the Class declaration or override all values with a host wide
	 * setting (ie. bilbo.software.casava.version=1.8.2 will cause all calls to casava on bilbo to use version 1.8.2).
	 *  
	 * @return
	 */
	public String getSoftwareVersion(){
		return softwareVersion;
	}
	
	public void setSoftwareVersion(String softwareVersion){
		this.softwareVersion = softwareVersion;
	}
	
	
}
