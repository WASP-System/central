package edu.yu.einstein.wasp.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.State;

/**
 * Wait for Wait For Sample File Processor
 * 
 * polls state.sample.getFile() 
 * if a file matches the filenameRegex progress, 
 * otherwise throw an exception
 * 
 */

@Component
public class WaitForSampleFileProcessor implements ItemProcessor {

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

		for (SampleFile sampleFile : state.getStatesample().get(0).getSample().getSampleFile()) {


			if (sampleFile.getFile().getAbsolutePath().matches(filenameRegex)) {
				return state;
			}
		}

		throw new RetryableException("Sample" + state.getStatesample().get(0).getSample().getName() + " not yet at has file matching " + filenameRegex + ".");

	}
}

