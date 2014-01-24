package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
import edu.yu.einstein.wasp.service.FileService;
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
@Component
public class TrimTasklet extends WaspTasklet {

	private RunService runService;
	
	@Autowired
	private SampleService sampleService;

	private int runId;
	private Run run;

	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private TrimGalore trim_galore;
	
	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;
	
	private String workingDirectory;
	private GridWorkService workService;
	private GridTransportConnection transportConnection;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public TrimTasklet() {
		// required by cglib
	}
	
	public TrimTasklet(Integer runId) {
		this.runId = runId;
	}

	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {

		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
		
		run = runService.getRunById(runId);
		
		Set<SampleSource> cellLibraries = runService.getCellLibrariesOnSuccessfulRunCells(run);

		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(trim_galore);
		sd.add(casava);
		
		WorkUnit w = new WorkUnit();
	        w.setProcessMode(ProcessMode.SINGLE);
	        w.setMode(ExecutionMode.TASK_ARRAY);
	        w.setSoftwareDependencies(sd);
		
		workService = hostResolver.getGridWorkService(w);
	        transportConnection = workService.getTransportConnection();
		
		String stageDir = transportConnection.getConfiguredSetting("illumina.data.stage");
	        if (!PropertyHelper.isSet(stageDir))
	            throw new GridException("illumina.data.stage is not defined!");
	        
	        String jobRoot = transportConnection.getConfiguredSetting("results.dir");
	        if (!PropertyHelper.isSet(jobRoot))
	                throw new GridException("results.dir is not defined!");
		
		workingDirectory = stageDir + "/" + run.getName() + "/";

	        w.setWorkingDirectory(workingDirectory);
	        
	        int tasks = 0;
	        int fnum=0;
	        
	        WorkUnit delete = new WorkUnit();
	        delete.setWorkingDirectory(stageDir + "/" + run.getName() + "/wasp/sequence" );
	        delete.setCommand("shopt -u nullglob");
	        delete.addCommand("rm -f *trimm*");
	        
	        //send command, bypassing scheduler
	        transportConnection.sendExecToRemote(delete);
	        
	        for (SampleSource cellLibrary : cellLibraries) {
	            Set<FileGroup> fgs = fileService.getFilesForCellLibraryByType(cellLibrary, fastqService.getFastqFileType());
	            if (fgs.size() != 1) {
	                String m = "cell library " + cellLibrary.getId() + ":" + cellLibrary.getUUID() + " was expected to have 1 set of FASTQ files, found " + fgs.size();
	                logger.error(m);
	                throw new WaspRuntimeException(m);
	            }
	            
	            Set<FileHandle> fastq = new TreeSet<FileHandle>(new FastqComparator(fastqService));
	            FileGroup fastqG = fgs.iterator().next();
	            fastq.addAll(fastqG.getFileHandles());
	            
	            Integer rs = fastqService.getNumberOfReadSegments(fastqG);
	            
	            Iterator<FileHandle> fhi = fastq.iterator();
	            
	            while (fhi.hasNext()) {
	                w.addRequiredFile(fhi.next());
	                
	                if (rs == 1) {
	                    w.addCommand("TASKS[" + tasks++ + "]=\"trim_galore --gzip ${" + WorkUnit.INPUT_FILE + "[" + fnum++ + "]\"");
	                } else if (rs == 2) {
	                    w.addCommand("TASKS[" + tasks++ + "]=\"trim_galore --paired --gzip ${" + WorkUnit.INPUT_FILE + "[" + fnum++ + "]${" + WorkUnit.INPUT_FILE + "[" + fnum++ + "]\"");
	                    w.addRequiredFile(fhi.next());
	                }
	            }
	            
	        }
	        
	        w.addCommand("${TASKS[" + WorkUnit.TASK_ARRAY_ID + "]}");
	        
	        w.setNumberOfTasks(tasks);

		GridResult result = workService.execute(w);
		
		logger.debug("submitted trimming of illumina output: " + result.getUuid());
		
		//place the grid result in the step context
		storeStartedResult(context, result);
		
		return RepeatStatus.CONTINUABLE;

	}

	/**
	 * @return the runService
	 */
	public RunService getRunService() {
		return runService;
	}

	/**
	 * @param runService
	 *            the runService to set
	 */
	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}

}
