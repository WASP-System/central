package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * 
 * @author asmclellan
 *
 */
@Repository
public class JdbcWaspJobExecutionDao extends JdbcJobExecutionDao implements WaspJobExecutionDao, InitializingBean{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private JobInstanceDao jobInstanceDao;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void setJobInstanceDao(JobInstanceDao jobInstanceDao){
		this.jobInstanceDao = jobInstanceDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> getJobExecutionIdsByMatchingParameters(Map<String, Set<String>> parameterMap){
		Assert.notNull(parameterMap, "parameterMap must not be null");
		if (parameterMap.isEmpty())
			return null;
		final List<Long> jobExecutionIds = new ArrayList<Long>();
		String sql = "select distinct JOB_EXECUTION_ID from %PREFIX%JOB_EXECUTION_PARAMS where TYPE_CD = 'STRING'";
		int keyIndex = 1;
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		for (String key: parameterMap.keySet()){
			sql += " and (JOB_EXECUTION_ID in (select JOB_EXECUTION_ID from %PREFIX%JOB_EXECUTION_PARAMS where KEY_NAME = :key"+keyIndex+" and STRING_VAL ";
			if (parameterMap.get(key).contains("*")){
				sql += "LIKE '%' )";
			} else {
				sql += "in (";
				int valIndex =1;
				for (String val: parameterMap.get(key)){
					String compositeValIndex = keyIndex + "_" + valIndex;
					if (valIndex > 1)
						sql += ",";
					sql += " :val" + compositeValIndex;
					parameterSource.addValue("val" + compositeValIndex, val, Types.BIGINT);		
					valIndex++;
				}
				sql += " ) )";
			}
			parameterSource.addValue("key"+keyIndex, key, Types.VARCHAR);
			sql += " )";
			keyIndex++;
		}
		logger.trace("Built SQL string: " + getQuery(sql));
		for (String key: parameterSource.getValues().keySet())
			logger.trace("Parameter: " + key + "=" + parameterSource.getValues().get(key).toString());
		
		
		RowMapper<Long> mapper = new RowMapper<Long>() {
			
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				logger.trace("Mapping result for row number " + rowNum + " (jobExecutionId=" + rs.getLong(1) + ") to a Long");
				Long rowVal = rs.getLong(1);
				jobExecutionIds.add(rowVal);
				return rowVal;
			}
		};
		
		namedParameterJdbcTemplate.query(getQuery(sql), parameterSource, mapper);
		return jobExecutionIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> getJobExecutionIdsByExclusivelyMatchingParameters(Map<String, Set<String>> parameterMap){
		List<Long> jobExecutionIds = getJobExecutionIdsByMatchingParameters(parameterMap);
		List<Long> exclusiveJobExecutionIds = new ArrayList<Long>();
		for (Long id: jobExecutionIds){
			MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue("val", id, Types.BIGINT);
			String sql = "select count(JOB_EXECUTION_ID) from %PREFIX%JOB_EXECUTION_PARAMS where TYPE_CD = 'STRING' and JOB_EXECUTION_ID = :val";
			logger.trace("Built SQL string: " + getQuery(sql));
			int count = namedParameterJdbcTemplate.queryForObject(getQuery(sql), parameterSource, Integer.class);
			logger.trace("job with JOB_EXECUTION_ID=" + id.toString() + " has " + count + " parameters, parameterMap has " + parameterMap.size());
			if (count == parameterMap.size())
				exclusiveJobExecutionIds.add(id);
		}
		return exclusiveJobExecutionIds;
	}
	
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus){
		Assert.notNull(jobInstanceDao, "jobInstanceDao must not be null");
		final List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
		
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		
		
		String sql = "SELECT E.JOB_EXECUTION_ID, E.JOB_INSTANCE_ID from %PREFIX%JOB_EXECUTION E, %PREFIX%JOB_INSTANCE I where "
			+ "E.JOB_INSTANCE_ID=I.JOB_INSTANCE_ID "
			+ "and (I.JOB_NAME LIKE :name1 or I.JOB_NAME LIKE :name2)";
		
		if (name == null)
			name = "";
		parameterSource.addValue("name1", "%" + name, Types.VARCHAR);
		parameterSource.addValue("name2", name + "%", Types.VARCHAR);
		
		if (batchStatus != null){
			sql += " and STATUS = :status ";
			parameterSource.addValue("status", batchStatus.toString(), Types.VARCHAR);
		}
		if (exitStatus != null){
			sql += " and EXIT_CODE = :exitStatus ";
			parameterSource.addValue("exitStatus", exitStatus.getExitCode(), Types.VARCHAR);
		}
		if (parameterMap != null){
			if (exclusive == null)
				exclusive = false;
			List<Long> jobExecutionIds = null;
			if (exclusive){
				jobExecutionIds = getJobExecutionIdsByExclusivelyMatchingParameters(parameterMap);
			} else {
				jobExecutionIds =getJobExecutionIdsByMatchingParameters(parameterMap);
			}
			if (jobExecutionIds == null || jobExecutionIds.isEmpty())
				return jobExecutions;
			
			sql += " and E.JOB_EXECUTION_ID in ( ";
			
			int index = 1;
			for (Long id: jobExecutionIds){
				parameterSource.addValue("id"+index, id, Types.BIGINT);
				if (index > 1)
					sql += ",";
				sql += " :id" + index;
				index++;
			}
			sql += " )";
		}
		sql += " ORDER BY E.JOB_INSTANCE_ID DESC, E.JOB_EXECUTION_ID DESC";
		logger.trace("Built SQL string: " + getQuery(sql));
		for (String key: parameterSource.getValues().keySet())
			logger.trace("Parameter: " + key + "=" + parameterSource.getValues().get(key).toString());
		
		
		RowMapper<JobExecution> mapper = new RowMapper<JobExecution>() {
			
			@Override
			public JobExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
				logger.trace("Mapping result for row number " + rowNum + " (jobExecutionId=" + rs.getLong(1) + ", jobInstanceId=" + rs.getLong(2) + ") to a JobExecution");
				JobExecution jobExecution = getJobExecution(rs.getLong(1));
				if (jobExecution == null)
					throw new SQLException("Failed to map result for row number " + rowNum + "(jobExecutionId=" + rs.getLong(1) + ") to a JobExecution: ");
				jobExecution.setJobInstance(jobInstanceDao.getJobInstance(rs.getLong(2)));
				jobExecutions.add(jobExecution);
				return jobExecution;
			}
		};
		
		namedParameterJdbcTemplate.query(getQuery(sql), parameterSource, mapper);
		return jobExecutions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobName(JobExecution jobExecution){
		if (jobExecution.getJobInstance() != null)
			return jobExecution.getJobInstance().getJobName();
		return (jobInstanceDao.getJobInstance(jobExecution).getJobName());
	}

	
	
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
	}
}
