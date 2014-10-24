package edu.yu.einstein.wasp.viewpanel;

import edu.yu.einstein.wasp.interfacing.plugin.WebInterfacing;

public interface DataTabViewing extends WebInterfacing{
	
	public static enum Status{ COMPLETED, STARTED, PENDING, FAILED, UNKNOWN, NOT_APPLICABLE, INCOMPLETE }

}
