/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import edu.yu.einstein.wasp.grid.GridExecutionException;

/**
 * @author calder
 *
 */
public class NoneManager implements SoftwareManager {

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.work.SoftwareManager#getConfiguration(edu.yu.einstein.wasp.grid.work.WorkUnit)
	 */
	@Override
	public String getConfiguration(WorkUnit w) throws GridExecutionException {
		return null;
	}

	@Override
	public String getConfiguredSetting(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
