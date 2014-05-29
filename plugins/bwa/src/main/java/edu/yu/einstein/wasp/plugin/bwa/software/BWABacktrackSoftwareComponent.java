/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.software;

import java.util.Map;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.SampleSource;

/**
 * @author calder
 *
 */
public class BWABacktrackSoftwareComponent extends AbstractBWASoftwareComponent{

	private static final long serialVersionUID = 5098513959286872753L;

	public BWABacktrackSoftwareComponent() {
		super();
	}
	
	public WorkUnit getAln(SampleSource cellLibrary, FileGroup fg, Map<String,Object> jobParameters) {
		WorkUnit w = prepareWorkUnit(fg);
		
		String alnOpts = getOptString("aln", jobParameters);

		String checkIndex = "if [ ! -e " + getGenomeIndexPath(getGenomeBuild(cellLibrary)) + ".bwt ]; then\n  exit 101;\nfi";

		w.setCommand(checkIndex);
		
		String command = "bwa aln " + alnOpts + " -t ${" + WorkUnit.NUMBER_OF_THREADS + "} " + 
				getGenomeIndexPath(getGenomeBuild(cellLibrary)) + " " +
				"${" + WorkUnit.INPUT_FILE + "[" + WorkUnit.ZERO_TASK_ARRAY_ID + "]} " +
				"> sai.${" + WorkUnit.TASK_OUTPUT_FILE + "}"; 
		
		logger.debug("Will conduct bwa aln with string: " + command);
		
		w.addCommand(command);
		
		return w;
	}
	
	public WorkUnit getSam(SampleSource cellLibrary, String scratchDirectory, String namePrefix, FileGroup fg, Map<String,Object> jobParameters) {
		WorkUnit w = prepareWorkUnit(fg);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setProcessorRequirements(1);
		w.setWorkingDirectory(scratchDirectory);
		
		Integer numberOfReadSegments = fastqService.getNumberOfReadSegments(fg);
		Assert.assertTrue(numberOfReadSegments > 0 && numberOfReadSegments <= 2);
		
		String method = "samse";
		if (numberOfReadSegments.equals(2)) 
			method = "sampe";
		
		String alnOpts = "";
		
		for (String opt : jobParameters.keySet()) {
			if (!opt.startsWith("sampe"))
				continue;
			String key = opt.replace("sampe", "");
			if (!key.equals("-n") && method.equals("samse"))
				continue;
			alnOpts += " " + key + " " + jobParameters.get(opt).toString();
		}
		
		String command = getSaiVars(namePrefix, w, method);
		
		if (method.equals("samse")) {
		
			command += "bwa " + method + " " + alnOpts + " -r " + this.getReadGroupString(cellLibrary) + " " +
					getGenomeIndexPath(getGenomeBuild(cellLibrary)) + " " +
					"${sai[" + WorkUnit.TASK_ARRAY_ID + "]} " + "${" + WorkUnit.INPUT_FILE + "[" + WorkUnit.TASK_ARRAY_ID + "]} " +
					"> sam.${" + WorkUnit.TASK_OUTPUT_FILE + "}"; 
		
			w.setNumberOfTasks(w.getRequiredFiles().size());
		} else {
			
			command += "FI=$((" + WorkUnit.TASK_ARRAY_ID + "*2))\n";
			command += "FI2=$((FI+1))\n";
			command += "bwa " + method + " " + alnOpts + " -r " + this.getReadGroupString(cellLibrary) + " " +
					getGenomeIndexPath(getGenomeBuild(cellLibrary)) + " " +
					"${sai1[" + WorkUnit.TASK_ARRAY_ID + "]} " + 
					"${sai2[" + WorkUnit.TASK_ARRAY_ID + "]} " +
					"${" + WorkUnit.INPUT_FILE + "[FI]} " +
					"${" + WorkUnit.INPUT_FILE + "[FI2]} " +
					"> sam.${" + WorkUnit.TASK_OUTPUT_FILE + "}"; 
			w.setNumberOfTasks(w.getRequiredFiles().size()/2);
		}
		
		logger.debug("Will conduct bwa aln with string: " + command);
		
		w.setCommand(command);
		
		return w;
	}
	
	private String getSaiVars(String namePrefix, WorkUnit w, String method) {
		String varStr = "";
		int lim = w.getRequiredFiles().size();
		if (method.equals("samse")) {
			for (int i = 1; i <= lim; i++) {
				varStr += "sai[" + i + "]=sai." + namePrefix + ":" + i + ".out\n";
			}
			return varStr;
		} else {
			int n = 0;
			for (int i = 1; i <= lim; i=i+2) {
				varStr += "sai1[" + n + "]=sai." + namePrefix + ":" + i + ".out\n";
				int i2 = i + 1;
				varStr += "sai2[" + n + "]=sai." + namePrefix + ":" + i2 + ".out\n";		
				n++;
			}
		}
		return varStr;
	}
	
	
	
}
