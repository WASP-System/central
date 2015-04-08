/**
 * 
 */
package edu.yu.einstein.wasp.helptag.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.helptag.software.Helptag;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 *
 */
public class BuildHpa2IndexTasklet extends WaspRemotingTasklet {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private Helptag helptag;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String hostname;
	private String remoteBuildPath;
	private String remoteFastaName;
	private String name;
	
	public BuildHpa2IndexTasklet(String hostname, String remoteBuildPath, String remoteFastaName, String name) {
		
		this.hostname = hostname;
		this.remoteBuildPath = remoteBuildPath;
		this.remoteFastaName = remoteFastaName;
		this.name = name;
		
	}
	

	@Override
	@Transactional("entityManager")
	public GridResult doExecute(ChunkContext context) throws Exception {

		GridWorkService host = hostResolver.getGridWorkService(hostname);
		
		logger.debug("setting up to build Hpa2 index for " + remoteBuildPath + " on " + hostname);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(helptag);

		c.setWorkingDirectory(remoteBuildPath);
		c.setResultsDirectory(remoteBuildPath);
		c.setSoftwareDependencies(sd);
		c.setMemoryRequirements(8);
		
		WorkUnit w = new WorkUnit(c);
		w.setCommand("if [ -e " + GenomeService.INDEX_CREATION_COMPLETED + " ]; then echo \"already begun\"; exit 0; fi");
		w.addCommand("if [ -e " + GenomeService.INDEX_CREATION_STARTED + ".tmp ]; then echo \"started but not completed\"; exit 1; fi");
		w.addCommand("if [ -e " + GenomeService.INDEX_CREATION_FAILED + " ]; then echo \"previously failed\"; exit 2; fi");
		
		w.setSecureResults(true);
		
		w.addCommand("touch " + GenomeService.INDEX_CREATION_STARTED + ".tmp");
		w.addCommand("buildHpa2Index.py -f " + remoteFastaName + " -o " + name);
		w.addCommand("rm -f " + GenomeService.INDEX_CREATION_STARTED + ".tmp");
		
		return host.execute(w);
	}

	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		super.beforeStep(stepExecution);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return super.afterStep(stepExecution);
	}


	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
