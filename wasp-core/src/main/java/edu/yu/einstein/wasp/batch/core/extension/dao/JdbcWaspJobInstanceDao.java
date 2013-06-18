package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.repository.dao.JdbcJobInstanceDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * 
 * @author asmclellan
 *
 */
@Repository
public class JdbcWaspJobInstanceDao extends JdbcJobInstanceDao implements WaspJobInstanceDao, InitializingBean{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> getJobInstanceIdsByMatchingParameters(Map<String, Set<String>> parameterMap){
		Assert.notNull(parameterMap, "parameterMap must not be null");
		if (parameterMap.isEmpty())
			return null;
		final List<Long> jobInstanceIds = new ArrayList<Long>();
		String sql = "select distinct JOB_INSTANCE_ID from %PREFIX%JOB_PARAMS where TYPE_CD = 'STRING'";
		int keyIndex = 1;
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		for (String key: parameterMap.keySet()){
			sql += " and (JOB_INSTANCE_ID in (select JOB_INSTANCE_ID from %PREFIX%JOB_PARAMS where KEY_NAME = :key"+keyIndex+" and STRING_VAL ";
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
					parameterSource.addValue("val" + compositeValIndex, val);		
					valIndex++;
				}
				sql += " ) )";
			}
			parameterSource.addValue("key"+keyIndex, key);
			sql += " )";
			keyIndex++;
		}
		logger.trace("Built SQL string: " + getQuery(sql));
		for (String key: parameterSource.getValues().keySet())
			logger.trace("Parameter: " + key + "=" + parameterSource.getValues().get(key).toString());
		
		
		RowMapper<Long> mapper = new RowMapper<Long>() {
			
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				logger.trace("Mapping result for row number " + rowNum + " (jobInstanceId=" + rs.getLong(1) + ") to a Long");
				Long rowVal = rs.getLong(1);
				jobInstanceIds.add(rowVal);
				return rowVal;
			}
		};
		
		getJdbcTemplate().query(getQuery(sql), mapper, parameterSource);
		return jobInstanceIds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> getJobInstanceIdsByExclusivelyMatchingParameters(Map<String, Set<String>> parameterMap){
		List<Long> jobInstanceIds = getJobInstanceIdsByMatchingParameters(parameterMap);
		List<Long> exclusiveJobInstanceIds = new ArrayList<Long>();
		for (Long id: jobInstanceIds){
			MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue("val", id);
			String sql = "select count(JOB_INSTANCE_ID) from %PREFIX%JOB_PARAMS where TYPE_CD = 'STRING' and JOB_INSTANCE_ID = :val";
			logger.trace("Built SQL string: " + getQuery(sql));
			int count = getJdbcTemplate().queryForInt(getQuery(sql), parameterSource);
			logger.trace("job with JOB_INSTANCE_ID=" + id.toString() + " has " + count + " parameters, parameterMap has " + parameterMap.size());
			if (count == parameterMap.size())
				exclusiveJobInstanceIds.add(id);
		}
		return exclusiveJobInstanceIds;
	}
	

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
}
