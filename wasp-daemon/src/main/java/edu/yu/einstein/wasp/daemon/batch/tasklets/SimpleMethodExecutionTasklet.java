/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Simple tasklet to execute a method in a defined directory.  Does it's work in a scratch directory and
 * optionally copies results into a results directory.
 * 
 * @author calder
 *
 */
@Transactional("entityManager")
public class SimpleMethodExecutionTasklet extends WaspRemotingTasklet {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	private List<SoftwarePackage> softwareDependencies;
	
	private String method;
	
	private List<String> arguments;
	
	private String workingDirectory = WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER;
	
	private String resultsDirectory;
	
	private String hostname;
	
	private GridWorkService workService;
	

	/**
	 * default 1g
	 */
	private Integer memoryRequirements = 1;
	
	/**
	 * ExecutionMode is fixed at process
	 */
	private ExecutionMode executionMode = ExecutionMode.PROCESS; 
	
	private ProcessMode processMode = ProcessMode.SINGLE;
	
	/**
	 * this will always be true, this tasklet not designed to chain tasks together. 
	 */
	private boolean secure = true;
	
	public SimpleMethodExecutionTasklet (List<SoftwarePackage> softwareDependencies, String method, List<String> arguments) {
		
		this.softwareDependencies = softwareDependencies;
		this.method = method;
		this.arguments = arguments;
		
	}
	
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
	public void setResultsDirectory(String resultsDirectory) {
		this.resultsDirectory = resultsDirectory;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public void setMemoryRequirements(Integer memoryRequirements) {
		this.memoryRequirements = memoryRequirements;
	}
	
	public void setProcessMode(ProcessMode processMode) {
		this.processMode = processMode;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public GridResult doExecute(ChunkContext context) throws Exception {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setSoftwareDependencies(softwareDependencies);
		c.setMemoryRequirements(memoryRequirements);
		c.setMode(executionMode);
		c.setProcessMode(processMode);
		if (resultsDirectory == null)
			resultsDirectory = workingDirectory;
		c.setResultsDirectory(resultsDirectory);
		WorkUnit w = new WorkUnit(c);
		w.setSecureResults(secure);
		GridResult r;
		if (hostname != null)  {
			r = hostResolver.getGridWorkService(hostname).execute(w);
		} else {
			r = hostResolver.execute(w);
		}
		return r;
		
	}

	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
