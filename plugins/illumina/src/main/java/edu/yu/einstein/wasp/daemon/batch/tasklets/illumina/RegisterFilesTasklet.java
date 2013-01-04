package edu.yu.einstein.wasp.daemon.batch.tasklets.illumina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.mps.illumina.IlluminaSequenceRunProcessor;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * 
 * 
 * @author calder
 * 
 */
public class RegisterFilesTasklet extends WaspTasklet {

	@Autowired
	private RunService runService;
	
	@Autowired
	private SampleService sampleService;

	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private IlluminaSequenceRunProcessor casava;
	
	private int runId;
	private Run run;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public RegisterFilesTasklet() {
		// required by cglib
	}
	
	public RegisterFilesTasklet(int runId) {
		this.runId = runId;
	}

	@Override
	@Transactional("entityManager")
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		
		run = runService.getRunById(runId);

		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);
		
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		// set casava as software dependency to ensure we get sent to the correct host
		w.setSoftwareDependencies(sd);
		// save the GridWorkService so we can send many jobs there
		GridWorkService gws = hostResolver.getGridWorkService(w);
		
		String stageDir = gws.getTransportService().getConfiguredSetting("illumina.data.stage");
		if (!PropertyHelper.isSet(stageDir))
			throw new GridException("illumina.data.stage is not defined!");
		
		w.setWorkingDirectory(stageDir + "/" + run.getName());
		w.setResultsDirectory(stageDir + "/" + run.getName() + "/wasp/");
		
		List<Sample> samples = sampleService.getLibrariesOnCell(run.getPlatformUnit());
		
		w.setCommand("mkdir -p wasp && cd wasp");
		
		for (Sample s : samples) {
			int sid = s.getSampleId();
			w.addCommand("if [ -e ../Project_WASP/Sample_ " + sid + "/" + sid + "_*_001.fastq.gz ]; then \n" +
					"  ln -s ../Project_WASP/Sample_" + sid + "/" + sid + "_*fastq.gz .\nfi");
		}
		
		// list available files for registration
		w.addCommand("ls -1");
		
		// method to send commands without wrapping them in a submission script.
		// will return when complete (also gives access to stdout), only do this 
		//when the work is expected to be very short running.
		GridResult r = w.getConnection().sendExecToRemote(w);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(r.getStdOutStream()));
		
		String line = br.readLine();
		
		while (line != null) {
			
			Matcher l = Pattern.compile("^(.*?)_(.*?)_L(.*?)_R(.*?)_(.*?).fastq.gz$").matcher(line);
			
			if (! l.find()) {
				logger.error("unable to parse file name: " + line);
				throw new edu.yu.einstein.wasp.exception.SampleException("Unexpected sample file name format");
			}
			
			Integer sampleId = new Integer(l.group(1));
			String barcode = l.group(2);
			Integer lane = new Integer(l.group(3));
			Integer read = new Integer(l.group(4));
			Integer fileNum = new Integer(l.group(5)); 
			
			line = br.readLine();
		}
		

		
		
		return RepeatStatus.CONTINUABLE;

	}

	
}
