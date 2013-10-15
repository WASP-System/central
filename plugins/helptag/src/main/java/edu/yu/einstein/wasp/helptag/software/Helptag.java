/**
 * Created by Wasp System Eclipse Plugin
 * @author AJ
 */
package edu.yu.einstein.wasp.helptag.software;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
// Un-comment the following if using the plugin service
// import org.springframework.beans.factory.annotation.Autowired;
// import package edu.yu.einstein.wasp.helptag.service. HelptagService;




/**
 * @author
 */
public class Helptag extends SoftwarePackage{

	// Un-comment the following if using the plugin service
	//@Autowired
	//HelptagService  helptagService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	FileType bamFileType;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6995954039217461596L;

	public static final String HELPTAG_AREA = "helptag";
	
	public static final String OUTPUT_FOLDER = "helptagResults";
	

	/**
	 * 
	 */
	public Helptag() {
		setSoftwareVersion("0.0.1"); // TODO: Set this value. This default may also be overridden in wasp.site.properties
	}

	public WorkUnit getHelptag(Integer filegroupId) {
		WorkUnit w = new WorkUnit();
		
		// require fastqc
		List<SoftwarePackage> software = new ArrayList<SoftwarePackage>();
		software.add(this);
		w.setSoftwareDependencies(software);
		
		// require 1GB memory
		w.setMemoryRequirements(1);
		
		// require a single thread, execution mode PROCESS
		// indicates this is a vanilla exectuion.
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMode(ExecutionMode.PROCESS);
		
		// set working directory to scratch
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		// we aren't actually going to retain any files, so we will set the output
		// directory to the scratch directory.  Also set "secure results" to
		// false to indicate that we don't care about the output.
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setSecureResults(false);
		
		// add the files to the work unit
		// files will be represented as bash variables in the work unit and
		// are sorted using the FastqComparator.  For single samples (what we have here)
		// this ensures that they will be in groups of read segments.
		// e.g.
		// s1.R1.001
		// s1.R2.001
		// s1.R1.002
		// s1.R2.002
		//we'll just use the forward reads for fastq_screen
		FileGroup fg = fileService.getFileGroupById(filegroupId);
		List<FileHandle> files = new ArrayList<FileHandle>(fg.getFileHandles());
		//Collections.sort(files, new FastqComparator(fastqService));
		w.setRequiredFiles(files);
		
		// set the command
		String cmd = getCommand(fg);
		w.setCommand(cmd);
		
		return w;
	}

	/**
	 * Set the helptag command. 
	 * 
	 * @param fileGroup
	 * @return String
	 */
	private String getCommand(FileGroup fileGroup) {
		
		String command = "";

		String optStr = "--base 27 --sort ";
		
		command += "mkdir " + OUTPUT_FOLDER + "\n";
		command += "/home/epigenscripts/bin/helptag_script.pl ${" + WorkUnit.INPUT_FILE + "[@]} " + optStr + " --outdir " + OUTPUT_FOLDER + "\n";
		return command;
	}

}
