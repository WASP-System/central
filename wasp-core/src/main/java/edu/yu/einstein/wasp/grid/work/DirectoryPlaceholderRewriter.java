/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;

/**
 * defines a tool to rewrite static strings in a work unit's directory names.
 * 
 * @author calder
 *
 */
public interface DirectoryPlaceholderRewriter {

	public void replaceDirectoryPlaceholders(GridTransportConnection transportConnection, WorkUnit w) throws MisconfiguredWorkUnitException;

}
