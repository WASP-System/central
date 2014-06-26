package edu.yu.einstein.wasp.plugin.mps.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute.Control.Option;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties.ReadType;
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadLengthConfiguredToBeAvailable(Workflow workflow, ResourceCategory resourceCategory, Integer readlength) throws MetadataException{
		Map<String, List<Option>> resourceOptions = workflowService.getConfiguredOptions(workflow, resourceCategory);
		for (String key : resourceOptions.keySet()){
			if (key.equals(SequenceReadProperties.READ_LENGTH_KEY)){
				for (Option option : resourceOptions.get(key))
					if (option.getValue().equals(readlength.toString()))
						return true;
				return false;
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadTypeConfiguredToBeAvailable(Workflow workflow, ResourceCategory resourceCategory, ReadType readType) throws MetadataException{
		Map<String, List<Option>> resourceOptions = workflowService.getConfiguredOptions(workflow, resourceCategory);
		for (String key : resourceOptions.keySet()){
			if (key.equals(SequenceReadProperties.READ_TYPE_KEY)){
				for (Option option : resourceOptions.get(key))
					if (option.getValue().equals(readType.toString()))
						return true;
				return false;
			}
		}
		return false;
	}


}
