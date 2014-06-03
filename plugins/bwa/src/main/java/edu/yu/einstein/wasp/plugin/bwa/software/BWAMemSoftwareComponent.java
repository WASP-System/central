/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.software;

import java.util.Map;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;

import edu.yu.einstein.wasp.grid.work.WorkUnit;
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
	
	public WorkUnit getMem(SampleSource cellLibrary, FileGroup fg, Map<String,Object> jobParameters) throws ParameterValueRetrievalException {
		WorkUnit w = prepareWorkUnit(fg);
		
		String alnOpts = getOptString("mem", jobParameters);
		
		String checkIndex = "if [ ! -e " + getGenomeIndexPath(getGenomeBuild(cellLibrary)) + ".bwt ]; then\n  exit 101;\nfi";

		w.setCommand(checkIndex);
		
		String command = "bwa mem " + alnOpts  + " -R " + this.getReadGroupString(cellLibrary) + " -t ${" + WorkUnit.NUMBER_OF_THREADS + "} " + 
				getGenomeIndexPath(getGenomeBuild(cellLibrary)) + " " +
				"${" + WorkUnit.INPUT_FILE + "[" + WorkUnit.ZERO_TASK_ARRAY_ID + "]} " +
				"> sam.${" + WorkUnit.TASK_OUTPUT_FILE + "}"; 
		
		logger.debug("Will conduct bwa mem with string: " + command);
		
		w.addCommand(command);
		
		return w;
	}
	
}
