/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.net.URL;

/**
 * @author calder
 *
 */
public class GridFtpFileMover implements GridFileMover {
	
	private URL dataHostURL;
	
	private boolean tpt = false;
	

	@Override
	public void setDataHostURL(URL dataURL) {
		dataHostURL = dataURL;
	}
	
	@Override
	public boolean canParticipateInThirdPartyTransfer() {
		return tpt;
	}
	
	public void setCanParticipateInThirdPartyTransfer(boolean tpt) {
		this.tpt = tpt;
	}

}
