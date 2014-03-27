package edu.yu.einstein.wasp.integration.endpoints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.splitter.AbstractMessageSplitter;

public abstract class WaspAbstractMessageSplitter extends AbstractMessageSplitter {
	
	@Value(value="${wasp.mode.isDemo:false}")
	protected boolean isInDemoMode;

	public WaspAbstractMessageSplitter() {
		super();
	}


}
