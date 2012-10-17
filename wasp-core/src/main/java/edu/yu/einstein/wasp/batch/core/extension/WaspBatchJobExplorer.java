package edu.yu.einstein.wasp.batch.core.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.batch.core.extension.dao.WaspJobExecutionDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.WaspJobInstanceDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.WaspStepExecutionDao;
import edu.yu.einstein.wasp.batch.exceptions.BatchDaoDataRetrievalException;
import edu.yu.einstein.wasp.batch.exceptions.ParameterValueRetrievalException;

public class WaspBatchJobExplorer extends SimpleJobExplorer implements JobExplorerWasp{
	
	private WaspJobInstanceDao waspJobInstanceDao;
	
	private WaspStepExecutionDao stepExecutionDao;
	
	private WaspJobExecutionDao jobExecutionDao;
	
		
	public WaspBatchJobExplorer(WaspJobInstanceDao waspJobInstanceDao,
            WaspJobExecutionDao waspJobExecutionDao,
            WaspStepExecutionDao stepExecutionDao,
            ExecutionContextDao ecDao) {
		super(waspJobInstanceDao, waspJobExecutionDao, stepExecutionDao, ecDao);
		this.jobExecutionDao = waspJobExecutionDao;
		this.waspJobInstanceDao = waspJobInstanceDao;
		this.stepExecutionDao = stepExecutionDao;
	}
	

	// getStepExecution/s methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) throws BatchDaoDataRetrievalException{
		if (exclusive == null)
			exclusive = false;
		List<StepExecution> stepExecutions = stepExecutionDao.getStepExecutions(name, parameterMap, exclusive, batchStatus, exitStatus);
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
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus){
		return stepExecutionDao.getStepExecutions(name, parameterMap, exclusive, batchStatus, exitStatus);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(Map<String, String> parameterMap,	Boolean exclusive) throws BatchDaoDataRetrievalException {
		return getStepExecution(null, parameterMap, exclusive, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(Map<String, String> parameterMap, Boolean exclusive) {
		return getStepExecutions(null, parameterMap, exclusive, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(String name, Map<String, String> parameterMap, Boolean exclusive)	throws BatchDaoDataRetrievalException {
		return getStepExecution(name, parameterMap, exclusive, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(Map<String, String> parameterMap,	Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) throws BatchDaoDataRetrievalException {
		return getStepExecution(null, parameterMap, exclusive, batchStatus, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive) {
		return getStepExecutions(name, parameterMap, exclusive, null, null);
	}

	@Override
	public List<StepExecution> getStepExecutions(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) {
		return getStepExecutions(null, parameterMap, exclusive, batchStatus, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(String name, Map<String, String> parameterMap, Boolean exclusive,	BatchStatus batchStatus) throws BatchDaoDataRetrievalException {
		return getStepExecution(name, parameterMap, exclusive, batchStatus, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(Map<String, String> parameterMap,	Boolean exclusive, BatchStatus batchStatus) throws BatchDaoDataRetrievalException {
		return getStepExecution(null, parameterMap, exclusive, batchStatus, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus) {
		return getStepExecutions(name, parameterMap, exclusive, batchStatus, null);
	}

	@Override
	public List<StepExecution> getStepExecutions(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus) {
		return getStepExecutions(null, parameterMap, exclusive, batchStatus, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepExecution getStepExecution(String name, Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException {
		return getStepExecution(name, parameterMap, exclusive, null, exitStatus);
	}

	@Override
	public StepExecution getStepExecution(Map<String, String> parameterMap,	Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException {
		return getStepExecution(null, parameterMap, exclusive, null, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) {
		return getStepExecutions(name, parameterMap, exclusive, null, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) {
		return getStepExecutions(null, parameterMap, exclusive, null, exitStatus);
	}
	
	@Override
	public List<StepExecution> getStepExecutions(String name) {
		return stepExecutionDao.getStepExecutions(name, null, null, null, null);
	}


	@Override
	public List<StepExecution> getStepExecutions() {
		return stepExecutionDao.getStepExecutions(null, null, null, null, null);
	}


	@Override
	public List<StepExecution> getStepExecutions(String name, BatchStatus batchStatus, ExitStatus exitStatus) {
		return stepExecutionDao.getStepExecutions(name, null, null, batchStatus, exitStatus);
	}


	@Override
	public List<StepExecution> getStepExecutions(BatchStatus batchStatus, ExitStatus exitStatus) {
		return stepExecutionDao.getStepExecutions(null, null, null, batchStatus, exitStatus);
	}
	

	@Override
	public List<StepExecution> getStepExecutions(String name, BatchStatus batchStatus) {
		return stepExecutionDao.getStepExecutions(name, null, null, batchStatus, null);
	}


	@Override
	public List<StepExecution> getStepExecutions(BatchStatus batchStatus) {
		return stepExecutionDao.getStepExecutions(null, null, null, batchStatus, null);
	}


	@Override
	public List<StepExecution> getStepExecutions(String name, ExitStatus exitStatus) {
		return stepExecutionDao.getStepExecutions(name, null, null, null, exitStatus);
	}


	@Override
	public List<StepExecution> getStepExecutions(ExitStatus exitStatus) {
		return stepExecutionDao.getStepExecutions(null, null, null, null, exitStatus);
	}
	
	// getJobExecution/s methods
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) throws BatchDaoDataRetrievalException{
		if (exclusive == null)
			exclusive = false;
		List<JobExecution> JobExecutions = jobExecutionDao.getJobExecutions(name, parameterMap, exclusive, batchStatus, exitStatus);
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
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus){
		return jobExecutionDao.getJobExecutions(name, parameterMap, exclusive, batchStatus, exitStatus);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(Map<String, String> parameterMap,	Boolean exclusive) throws BatchDaoDataRetrievalException {
		return getJobExecution(null, parameterMap, exclusive, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(Map<String, String> parameterMap, Boolean exclusive) {
		return getJobExecutions(null, parameterMap, exclusive, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(String name, Map<String, String> parameterMap, Boolean exclusive)	throws BatchDaoDataRetrievalException {
		return getJobExecution(name, parameterMap, exclusive, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(Map<String, String> parameterMap,	Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) throws BatchDaoDataRetrievalException {
		return getJobExecution(null, parameterMap, exclusive, batchStatus, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive) {
		return getJobExecutions(name, parameterMap, exclusive, null, null);
	}

	@Override
	public List<JobExecution> getJobExecutions(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus) {
		return getJobExecutions(null, parameterMap, exclusive, batchStatus, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(String name, Map<String, String> parameterMap, Boolean exclusive,	BatchStatus batchStatus) throws BatchDaoDataRetrievalException {
		return getJobExecution(name, parameterMap, exclusive, batchStatus, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(Map<String, String> parameterMap,	Boolean exclusive, BatchStatus batchStatus) throws BatchDaoDataRetrievalException {
		return getJobExecution(null, parameterMap, exclusive, batchStatus, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus) {
		return getJobExecutions(name, parameterMap, exclusive, batchStatus, null);
	}

	@Override
	public List<JobExecution> getJobExecutions(Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus) {
		return getJobExecutions(null, parameterMap, exclusive, batchStatus, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getJobExecution(String name, Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException {
		return getJobExecution(name, parameterMap, exclusive, null, exitStatus);
	}

	@Override
	public JobExecution getJobExecution(Map<String, String> parameterMap,	Boolean exclusive, ExitStatus exitStatus) throws BatchDaoDataRetrievalException {
		return getJobExecution(null, parameterMap, exclusive, null, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) {
		return getJobExecutions(name, parameterMap, exclusive, null, exitStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(Map<String, String> parameterMap, Boolean exclusive, ExitStatus exitStatus) {
		return getJobExecutions(null, parameterMap, exclusive, null, exitStatus);
	}
	
	
	@Override
	public List<JobExecution> getJobExecutions(String name) {
		return jobExecutionDao.getJobExecutions(name, null, null, null, null);
	}


	@Override
	public List<JobExecution> getJobExecutions() {
		return jobExecutionDao.getJobExecutions(null, null, null, null, null);
	}


	@Override
	public List<JobExecution> getJobExecutions(String name, BatchStatus batchStatus, ExitStatus exitStatus) {
		return jobExecutionDao.getJobExecutions(name, null, null, batchStatus, exitStatus);
	}


	@Override
	public List<JobExecution> getJobExecutions(BatchStatus batchStatus, ExitStatus exitStatus) {
		return jobExecutionDao.getJobExecutions(null, null, null, batchStatus, exitStatus);
	}
	

	@Override
	public List<JobExecution> getJobExecutions(String name, BatchStatus batchStatus) {
		return jobExecutionDao.getJobExecutions(name, null, null, batchStatus, null);
	}


	@Override
	public List<JobExecution> getJobExecutions(BatchStatus batchStatus) {
		return jobExecutionDao.getJobExecutions(null, null, null, batchStatus, null);
	}


	@Override
	public List<JobExecution> getJobExecutions(String name, ExitStatus exitStatus) {
		return jobExecutionDao.getJobExecutions(name, null, null, null, exitStatus);
	}


	@Override
	public List<JobExecution> getJobExecutions(ExitStatus exitStatus) {
		return jobExecutionDao.getJobExecutions(null, null, null, null, exitStatus);
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
		Map<String, JobParameter> parameters = jobExecutionDao.getJobParameters(je).getParameters();
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
		StepExecution stepExecution = null;
		if (stepExecutions == null)
			return null;
		for(StepExecution se: stepExecutions){
			if (stepExecution == null || se.getStartTime().after(stepExecution.getStartTime())){
				stepExecution = se;
			} else if (se.getStartTime().equals(stepExecution.getStartTime()) && se.getId() > stepExecution.getId()){
				stepExecution = se;
			}
		}
		return stepExecution;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobExecution getMostRecentlyStartedJobExecutionInList(List<JobExecution> jobExecutions){
		JobExecution jobExecution = null;
		if (jobExecutions == null)
			return null;
		for(JobExecution je: jobExecutions){
			if (jobExecution == null || je.getStartTime().after(jobExecution.getStartTime())){
				jobExecution = je;
			} else if (je.getStartTime().equals(jobExecution.getStartTime()) && je.getId() > jobExecution.getId()){
				jobExecution = je;
			}
		}
		return jobExecution;
	}








}
