package edu.yu.einstein.wasp.taskMapping.illumina;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.taskMapping.WaspTaskMapping;

public class IlluminaRunQCTaskMapping extends WaspTaskMapping {
	
	public IlluminaRunQCTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink, permission);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequirementToShowLink() throws WaspException {
		return false;
	}

}
