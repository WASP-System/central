/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.software;

import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.SampleSource;

/**
 * @author asmclellan
 *
 */
public class BWAMemSoftwareComponent extends AbstractBWASoftwareComponent{

	private static final long serialVersionUID = -5244640940549905144L;

	public BWAMemSoftwareComponent() {
		super();
	}
	
	@Transactional("entityManager")
	public WorkUnit getMem(SampleSource cellLibrary, FileGroup fg, Map<String,JobParameter> jobParameters) throws ParameterValueRetrievalException {
		WorkUnit w = buildWorkUnit(fg);
		
		String alnOpts = getOptString("mem", jobParameters);
		
		String checkIndex = "if [ ! -e " + getGenomeIndexPath(getGenomeBuild(cellLibrary)) + ".bwt ]; then\n  exit 101;\nfi";
		
		
		// must be 1 or 2 read-segment input
		Integer numberOfReadSegments = fastqService.getNumberOfReadSegments(fg);
		Assert.assertTrue(numberOfReadSegments > 0 && numberOfReadSegments <= 2);

		w.setCommand(checkIndex);
		
		String command = "";
		
		if (numberOfReadSegments.equals(1)) {
			logger.trace("perfoming single end BWA mem on cell library: " + cellLibrary.getId());
			command = "bwa mem " + alnOpts  + " -R " + this.getReadGroupString(cellLibrary) + " -t ${" + WorkUnitGridConfiguration.NUMBER_OF_THREADS + "} " + 
					getGenomeIndexPath(getGenomeBuild(cellLibrary)) + " " +
					"${" + WorkUnit.INPUT_FILE + "[" + WorkUnitGridConfiguration.ZERO_TASK_ARRAY_ID + "]} " +
					"> sam.${" + WorkUnitGridConfiguration.TASK_OUTPUT_FILE + "}"; 
		
		} else {
			logger.trace("perfoming paired-end BWA mem on cell library: " + cellLibrary.getId());
			command += "FI=$((" + WorkUnitGridConfiguration.ZERO_TASK_ARRAY_ID + "*2))\n";
			command += "FI2=$((FI+1))\n";
			command += "bwa mem " + alnOpts  + " -R " + this.getReadGroupString(cellLibrary) + " -t ${" + WorkUnitGridConfiguration.NUMBER_OF_THREADS + "} " + 
					getGenomeIndexPath(getGenomeBuild(cellLibrary)) + " " +
					"${" + WorkUnit.INPUT_FILE + "[FI]} " +
					"${" + WorkUnit.INPUT_FILE + "[FI2]} " +
					"> sam.${" + WorkUnitGridConfiguration.TASK_OUTPUT_FILE + "}"; 
			// number of tasks needs to be halved!
			w.getConfiguration().setNumberOfTasks(w.getRequiredFiles().size()/2);
		}
		
		logger.debug("Will conduct bwa mem with string: " + command);
		
		w.addCommand(command);
		
		return w;
	}
	
}
