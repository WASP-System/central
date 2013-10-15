package edu.yu.einstein.wasp.batch.core.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;

import edu.yu.einstein.wasp.batch.core.extension.dao.WaspJobExecutionDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.WaspStepExecutionDao;
import edu.yu.einstein.wasp.exception.BatchDaoDataRetrievalException;
import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;

/**
 * 
 * @author asmclellan
 *
 */
public class WaspBatchJobExplorer extends SimpleJobExplorer implements JobExplorerWasp{
	
	private WaspStepExecutionDao stepExecutionDao;
	
	private WaspJobExecutionDao jobExecutionDao;
	
		
	public WaspBatchJobExplorer(JobInstanceDao jobInstanceDao,
            WaspJobExecutionDao waspJobExecutionDao,
            WaspStepExecutionDao stepExecutionDao,
            ExecutionContextDao ecDao) {
		super(jobInstanceDao, waspJobExecutionDao, stepExecutionDao, ecDao);
		this.jobExecutionDao = waspJobExecutionDao;
		this.stepExecutionDao = stepExecutionDao;
	}
	

	// getStepExecution/s methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException{
		if (exclusive == null)
			exclusive = false;
		List<StepExecution> stepExecutions = stepExecutionDao.getStepExecutions(name, parameterMap, exclusive, exitStatus);
		if (stepExecutions == null || stepExecutions.isEmpty())
			return null;
		if (stepExecutions.size() != 1)
			throw new BatchDaoDataRetrievalException("More than one StepExecution object returned with given step name and parameter map");
		return stepExecutions.get(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus){
		return stepExecutionDao.getStepExecutions(name, parameterMap, exclusive, exitStatus);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(Map<String, Set<String>> parameterMap, Boolean exclusive) throws BatchDaoDataRetrievalException {
		return getStepExecution(null, parameterMap, exclusive, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(Map<String, Set<String>> parameterMap, Boolean exclusive) {
		return getStepExecutions(null, parameterMap, exclusive, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(String name, Map<String, Set<String>> parameterMap, Boolean exclusive)	throws BatchDaoDataRetrievalException {
		return getStepExecution(name, parameterMap, exclusive, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive) {
		return getStepExecutions(name, parameterMap, exclusive, null);
	}


	@Override
	public StepExecution getStepExecution(Map<String, Set<String>> parameterMap,	Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException {
		return getStepExecution(null, parameterMap, exclusive, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus) {
		return getStepExecutions(null, parameterMap, exclusive, exitStatus);
	}
	
	@Override
	public List<StepExecution> getStepExecutions(String name) {
		return stepExecutionDao.getStepExecutions(name, null, null, null);
	}


	@Override
	public List<StepExecution> getStepExecutions() {
		return stepExecutionDao.getStepExecutions(null, null, null, null);
	}

	@Override
	public List<StepExecution> getStepExecutions(String name, ExitStatus exitStatus) {
		return stepExecutionDao.getStepExecutions(name, null, null, exitStatus);
	}


	@Override
	public List<StepExecution> getStepExecutions(ExitStatus exitStatus) {
		return stepExecutionDao.getStepExecutions(null, null, null, exitStatus);
	}
	
	// getJobExecution/s methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException{
		if (exclusive == null)
			exclusive = false;
		List<JobExecution> JobExecutions = jobExecutionDao.getJobExecutions(name, parameterMap, exclusive, exitStatus);
		if (JobExecutions.isEmpty())
			return null;
		if (JobExecutions.size() != 1)
			throw new BatchDaoDataRetrievalException("More than one JobExecution object returned with given step name and parameter map");
		return JobExecutions.get(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus){
		return jobExecutionDao.getJobExecutions(name, parameterMap, exclusive, exitStatus);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(Map<String, Set<String>> parameterMap,	Boolean exclusive) throws BatchDaoDataRetrievalException {
		return getJobExecution(null, parameterMap, exclusive, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(Map<String, Set<String>> parameterMap, Boolean exclusive) {
		return getJobExecutions(null, parameterMap, exclusive, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(String name, Map<String, Set<String>> parameterMap, Boolean exclusive)	throws BatchDaoDataRetrievalException {
		return getJobExecution(name, parameterMap, exclusive, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive) {
		return getJobExecutions(name, parameterMap, exclusive, null);
	}

	@Override
	public JobExecution getJobExecution(Map<String, Set<String>> parameterMap,	Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException {
		return getJobExecution(null, parameterMap, exclusive, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus) {
		return getJobExecutions(null, parameterMap, exclusive, exitStatus);
	}
	
	
	@Override
	public List<JobExecution> getJobExecutions(String name) {
		return jobExecutionDao.getJobExecutions(name, null, null, null);
	}


	@Override
	public List<JobExecution> getJobExecutions() {
		return jobExecutionDao.getJobExecutions(null, null, null, null);
	}




	@Override
	public List<JobExecution> getJobExecutions(String name, ExitStatus exitStatus) {
		return jobExecutionDao.getJobExecutions(name, null, null, exitStatus);
	}


	@Override
	public List<JobExecution> getJobExecutions(ExitStatus exitStatus) {
		return jobExecutionDao.getJobExecutions(null, null, null, exitStatus);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutionsMatchingParameters(Map<String, Set<String>> parameterMap){
		List<JobExecution> masterJobExecutions = new ArrayList<JobExecution>();
		for (Long jobExecutionId: jobExecutionDao.getJobExecutionIdsByMatchingParameters(parameterMap)){
			masterJobExecutions.add(this.getJobExecution(jobExecutionId));
		}
		return masterJobExecutions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobParameterValueByKey(StepExecution se, String key) throws ParameterValueRetrievalException{
		Map<String, JobParameter> parameters = stepExecutionDao.getJobParameters(se).getParameters();
		if (parameters == null)
			throw new ParameterValueRetrievalException("Parameter value returned was null for key '" + key + "'");
		try{
			JobParameter parameter = parameters.get(key);
			if (parameter == null)
				throw new ParameterValueRetrievalException("Parameter value returned was null for key '" + key + "'");
			return (String) parameter.getValue();
		} catch(ClassCastException e){
			throw new ParameterValueRetrievalException("Cannot cast parameter value obtained with key '" + key + "' to 'String'");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobParameterValueByKey(JobExecution je, String key) throws ParameterValueRetrievalException{
		Map<String, JobParameter> parameters = je.getJobParameters().getParameters();
		if (parameters == null)
			throw new ParameterValueRetrievalException("Parameter value returned was null for key '" + key + "'");
		try{
			JobParameter parameter = parameters.get(key);
			if (parameter == null)
				throw new ParameterValueRetrievalException("Parameter value returned was null for key '" + key + "'");
			return (String) parameter.getValue();
		} catch(ClassCastException e){
			throw new ParameterValueRetrievalException("Cannot cast parameter value obtained with key '" + key + "' to 'String'");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getMostRecentlyStartedStepExecutionInList(List<StepExecution> stepExecutions){
		if (stepExecutions == null || stepExecutions.isEmpty())
			return null;
		return stepExecutions.get(0); // first in list should be most recent
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getMostRecentlyStartedJobExecutionInList(List<JobExecution> jobExecutions){
		if (jobExecutions == null || jobExecutions.isEmpty())
			return null;
		return jobExecutions.get(0);  // first in list should be most recent
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobName(JobExecution jobExecution) {
		return jobExecutionDao.getJobName(jobExecution);
	}

	






}
