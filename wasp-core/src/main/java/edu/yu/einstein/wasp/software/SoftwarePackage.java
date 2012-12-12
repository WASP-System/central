/**
 * 
 */
package edu.yu.einstein.wasp.software;

import edu.yu.einstein.wasp.grid.work.SoftwareManager;
import edu.yu.einstein.wasp.load.SoftwareLoader;

/**
 * @author calder
 *
 */
public abstract class SoftwarePackage extends SoftwareLoader {
	
	/**
	 * A unique name for the software component. Can be overridden at the configuration level 
	 * For example: bilbo.software.casava.name=casava/gnu will override the default name of casava on bilbo. This is
	 * useful when a remote {@link SoftwareManager} uses an alternate name to specify the software package.  Using the 
	 * previous example with ModulesManager would result in "modules load casava/gnu/1.8.2". 
	 * @return
	 */
	public abstract String getName();
	
	public abstract void setName(String name);
	
	/**
	 * the version string of the software.  Should generally be set at the time the dependency is declared.
	 * It is possible to hard code this value in the Class declaration or override all values with a host wide
	 * setting (ie. bilbo.software.casava.version=1.8.2 will cause all calls to casava on bilbo to use version 1.8.2).
	 *  
	 * @return
	 */
	public abstract String getVersion();
	
	public abstract void setVersion(String version);
	
	
}
