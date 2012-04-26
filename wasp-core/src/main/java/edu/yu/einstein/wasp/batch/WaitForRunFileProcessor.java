package edu.yu.einstein.wasp.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.RunFile;
import edu.yu.einstein.wasp.model.State;

/**
 * Wait for Wait For Run File Processor
 * 
 * polls state.run.getFile() 
 * if a file matches the filenameRegex progress, 
 * otherwise throw an exception
 * 
 */

@Component
public class WaitForRunFileProcessor implements ItemProcessor {

	@Autowired
	StateDao stateDao;

	String filenameRegex; 
	public void setFilenameRegex(String filenameRegex) {
		this.filenameRegex = filenameRegex; 
	}

	@Override
	public State process(Object stateId) throws Exception {	
		State state = stateDao.getStateByStateId(((Integer) stateId).intValue());
		// TODO npe check

		for (RunFile runFile : state.getStaterun().get(0).getRun().getRunFile()) {

			if (runFile.getFile().getAbsolutePath().matches(filenameRegex)) {
				return state;
			}
		}

		throw new RetryableException("Run" + state.getStaterun().get(0).getRun().getName() + " not yet at has file matching " + filenameRegex + ".");

	}
}

