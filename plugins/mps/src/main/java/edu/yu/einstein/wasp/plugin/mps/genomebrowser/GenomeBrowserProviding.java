package edu.yu.einstein.wasp.plugin.mps.genomebrowser;

import edu.yu.einstein.wasp.interfacing.plugin.WaspPluginI;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.viewpanel.Action;

public interface GenomeBrowserProviding extends WaspPluginI {
	
	public Action getAction(FileGroup fileGroup);
}
