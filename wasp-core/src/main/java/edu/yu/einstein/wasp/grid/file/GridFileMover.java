/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.net.URL;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;

/**
 * Abstract superclass for GridFileMovers.  Each GridWorkService can have zero or more configured GridGileMovers
 * which the GridFileMoverService can use to determine the appropriate method for negotiating a file transfer
 * between the two hosts.
 * 
 * @author calder
 *
 */
public interface GridFileMover {
	
	public void setDataHostURL(URL dataURL);
	
	public boolean canParticipateInThirdPartyTransfer();

}
