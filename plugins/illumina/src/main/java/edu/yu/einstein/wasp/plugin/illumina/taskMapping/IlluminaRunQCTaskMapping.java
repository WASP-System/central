package edu.yu.einstein.wasp.plugin.illumina.taskMapping;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.taskMapping.WaspTaskMapping;

public class IlluminaRunQCTaskMapping extends WaspTaskMapping {
	
	@Autowired
	private RunService runService;
	
	public IlluminaRunQCTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink, permission);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequirementToShowLink(Object o) throws WaspException {
		return runService.isRunsAwaitingQc();
	}

}
