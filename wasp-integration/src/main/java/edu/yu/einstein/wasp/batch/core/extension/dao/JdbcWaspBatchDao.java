package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.stereotype.Repository;


@Repository
public class JdbcWaspBatchDao extends AbstractJdbcBatchMetadataDao implements WaspBatchDao {
	
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> getJobInstanceIdsByMatchingParameters(Map<String, String> parameterMap){
		List<Long> masterJobInstanceIds = new ArrayList<Long>();
		for (String key: parameterMap.keySet()){
			Query q = entityManager.createNativeQuery("select JOB_INSTANCE_ID from BATCH_JOB_PARAMS where TYPE_CD='STRING' and KEY_NAME='"+key+"' and STRING_VAL='"+parameterMap.get(key)+"';");
			List<Long> jobInstanceIds = (List<Long>) q.getResultList();
			if (jobInstanceIds != null && ! jobInstanceIds.isEmpty()){
				if (masterJobInstanceIds.isEmpty()){
					masterJobInstanceIds.addAll(jobInstanceIds);
				} else {
					masterJobInstanceIds.retainAll(jobInstanceIds);
				}
			}
		}
		return masterJobInstanceIds;
	}

}
