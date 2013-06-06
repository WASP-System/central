/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.software;

import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 *
 */
public class FastQC extends SoftwarePackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7075104587205964069L;

	/**
	 * 
	 */
	public FastQC() {
		// TODO Auto-generated constructor stub
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setSoftwareVersion(String softwareVersion) {
		// TODO Auto-generated method stub

	}
	
	public WorkUnit getFastQC(FileGroup fileGroup) {
		
		return null;
	}
	
	public String parseOutput(GridResult result) {
		return null;
	}

}
