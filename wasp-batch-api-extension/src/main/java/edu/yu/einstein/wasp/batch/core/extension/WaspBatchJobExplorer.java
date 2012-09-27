package edu.yu.einstein.wasp.batch.core.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;

import edu.yu.einstein.wasp.batch.core.extension.dao.WaspJobInstanceDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.WaspStepExecutionDao;
import edu.yu.einstein.wasp.exceptions.BatchDaoDataRetrievalException;

public class WaspBatchJobExplorer extends SimpleJobExplorer implements JobExplorerWasp{
	
	private WaspJobInstanceDao waspJobInstanceDao;
	
	private WaspStepExecutionDao stepExecutionDao;
	
		
	public WaspBatchJobExplorer(WaspJobInstanceDao waspJobInstanceDao,
            JobExecutionDao jobExecutionDao,
            WaspStepExecutionDao stepExecutionDao,
            ExecutionContextDao ecDao) {
		super(waspJobInstanceDao, jobExecutionDao, stepExecutionDao, ecDao);
		this.waspJobInstanceDao = waspJobInstanceDao;
		this.stepExecutionDao = stepExecutionDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobInstance> getJobInstancesMatchingParameters(Map<String, String> parameterMap){
		List<JobInstance> masterJobInstances = new ArrayList<JobInstance>();
		for (Long jobInstanceId: waspJobInstanceDao.getJobInstanceIdsByMatchingParameters(parameterMap)){
			masterJobInstances.add(this.getJobInstance(jobInstanceId));
		}
		return masterJobInstances;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecutionByStepNameAndParameterMap(String name, Map<String, String> parameterMap) throws BatchDaoDataRetrievalException{
		List<StepExecution> stepExecutions = stepExecutionDao.getStepExecutionsByNameAndMatchingParameters(name, parameterMap);
		if (stepExecutions.isEmpty())
			return null;
		if (stepExecutions.size() != 1)
			throw new BatchDaoDataRetrievalException("More than one StepExecution object returned with given step name and parameter map");
		return stepExecutions.get(0);
	}

}
