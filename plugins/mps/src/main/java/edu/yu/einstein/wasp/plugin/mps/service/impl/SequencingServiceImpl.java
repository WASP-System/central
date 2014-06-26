package edu.yu.einstein.wasp.plugin.mps.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.plugin.mps.service.SequencingService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

/**
 * 
 * @author asmclellan
 *
 */
@Service
@Transactional("entityManager")
public class SequencingServiceImpl extends WaspServiceImpl implements SequencingService {
	
	public Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	WorkflowService workflowService;

	public SequencingServiceImpl() {
		
	}
	
/*	public boolean isReadLengthAvailable(Workflow workflow, ResourceCategory resourceCategory, int readlength) throws MetadataException{
		Map<String, List<Option>> resourceOptions = workflowService.getConfiguredOptions(workflow, resourceCategory);
		for (String key : resourceOptions.keySet()){
			if (key.equals(Seque))
		}
		
	}*/
	
	
	
	

}
