package edu.yu.einstein.wasp.service.impl;

import org.apache.log4j.Logger;

import edu.yu.einstein.wasp.model.WaspModel;
import edu.yu.einstein.wasp.service.WaspService;


public abstract class WaspServiceImpl implements WaspService {

	// generic logger included with every class.
	protected static Logger logger = Logger.getLogger(WaspModel.class.getName());
  
}

