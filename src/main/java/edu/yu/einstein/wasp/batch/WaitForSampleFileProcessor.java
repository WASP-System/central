package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.service.StateService;

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
	StateService stateService;

	String filenameRegex; 
	public void setFilenameRegex(String filenameRegex) {
		this.filenameRegex = filenameRegex; 
	}

	@Override
	public State process(Object stateId) throws Exception {	
		State state = stateService.getStateByStateId(((Integer) stateId).intValue());
		// TODO npe check

		for (SampleFile sampleFile : state.getStatesample().get(0).getSample().getSampleFile()) {

			System.out.println("YYYYY" + sampleFile.getFile().getFilelocation() + "\n");

			if (sampleFile.getFile().getFilelocation().matches(filenameRegex)) {
				return state;
			}
		}

		throw new RetryableException("Sample" + state.getStatesample().get(0).getSample().getName() + " not yet at has file matching " + filenameRegex + ".");

	}
}

