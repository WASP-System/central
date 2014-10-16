package edu.yu.einstein.wasp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import edu.yu.einstein.wasp.service.WaspService;


public abstract class WaspServiceImpl implements WaspService {

	// generic logger included with every class.
	protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);
	
	@Value("${wasp.host.fullServletPath}")
	protected String servletPath;
	
	@Value(value="${wasp.mode.isDemo:false}")
	protected boolean isInDemoMode;
  
}

