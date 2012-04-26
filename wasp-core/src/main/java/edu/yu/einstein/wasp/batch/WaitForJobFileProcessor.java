package edu.yu.einstein.wasp.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.State;

/**
 * Wait for Wait For Job File Processor
 * 
 * polls state.job.getFile() 
 * if a file matches the filenameRegex progress, 
 * otherwise throw an exception
 * 
 */

@Component
public class WaitForJobFileProcessor implements ItemProcessor {

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

		for (JobFile jobFile : state.getStatejob().get(0).getJob().getJobFile()) {

			if (jobFile.getFile().getAbsolutePath().matches(filenameRegex)) {
				return state;
			}
		}

		throw new RetryableException("Job" + state.getStatejob().get(0).getJob().getName() + " not yet at has file matching " + filenameRegex + ".");

	}
}

