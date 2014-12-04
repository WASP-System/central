/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.file;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspHibernatingTasklet;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * Test for the existence of a file outside of the database. This file needs to be associated with a {@link SoftwarePackage}
 * in order for the {@link GridHostResolver} to determine where to look for the file.
 * 
 * @author calder
 *
 */
@Component
public class ExternalFileExistsTasklet extends WaspHibernatingTasklet {
	
	public ExternalFileExistsTasklet() {
		// required for AOP/CGLIB/Batch/Annotations
	}
	
	private GridHostResolver gridHostResolver;
	
	private SoftwarePackage softwarePackage;
	private String rootDirectory;
	private String subDirectory;
	private String filename;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @return the gridHostResolver
	 */
	public GridHostResolver getGridHostResolver() {
		return gridHostResolver;
	}

	/**
	 * @param gridHostResolver the gridHostResolver to set
	 */
	@Autowired
	public void setGridHostResolver(GridHostResolver gridHostResolver) {
		this.gridHostResolver = gridHostResolver;
	}
	
	public void setSubDirectory(String subDirectory) {
		this.subDirectory = subDirectory;
	}
	
	public String getSubDirectory() {
		if (subDirectory != null) {
			return "/" + subDirectory + "/";
		}
		return "/";
	}

	
	/**
	 * Tasklet to continuously look for the presence of a file on a remote host.
	 * 
	 * @param softwarePackage
	 * @param directoryId Identifying string, key of root directory on remote host.
	 * @param filename
	 */
	public ExternalFileExistsTasklet(SoftwarePackage softwarePackage, String directoryId, String filename) {
		this.softwarePackage = softwarePackage;
		this.rootDirectory = directoryId;
		this.filename = filename;
		logger.debug("root " + directoryId);
		logger.debug("filename " + filename);
	}

	
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		Long stepExecutionId = stepExecution.getId();
		if (wasWokenOnTimeout(context)){
			logger.debug("StepExecution id=" + stepExecutionId + " was woken up from hibernation after a timeout.");
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
			wasHibernationRequested = false;
			removeWokenOnTimeoutStatus(stepExecution);
		} else if (wasWokenOnMessage(stepExecution)){
			logger.debug("StepExecution id=" + stepExecutionId + " was woken up from hibernation for a message.");
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
			wasHibernationRequested = false;
			removeWokenOnMessageStatus(stepExecution);
		}
		if (!wasHibernationRequested){
			WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
			List<SoftwarePackage> software = new ArrayList<SoftwarePackage>();
			software.add(softwarePackage);
			c.setSoftwareDependencies(software);
			GridWorkService gws = gridHostResolver.getGridWorkService(c);
			GridFileService gfs = gws.getGridFileService();
			
			String directory = gws.getTransportConnection().getConfiguredSetting(rootDirectory);
			if (!PropertyHelper.isSet(directory)) {
				throw new GridAccessException("Unable to determine remote root directory");
			}
			
			String fn = directory + getSubDirectory() + filename;
			String host = gridHostResolver.getHostname(c);
			
			if (gfs.exists(fn)) {
				logger.info("Tasklet found file: " + host + ":" + fn);
				return RepeatStatus.FINISHED;
			}
			
			String e = "Tasklet did not find file: " + fn + " on host " + host;
			logger.debug(e);
			Long timeoutInterval = exponentiallyIncreaseTimeoutIntervalInContext(context);
			logger.debug("Going to request hibernation for " + timeoutInterval + " ms");
			addStatusMessagesToAbandonStepToContext(stepExecution, abandonTemplates);
		} else {
			logger.debug("Previous hibernation request made by this StepExecution (id=" + stepExecutionId + 
					") but we were still waiting for all steps to be ready. Going to retry request.");
		}
		requestHibernation(context);
		return RepeatStatus.CONTINUABLE;
	}

}
