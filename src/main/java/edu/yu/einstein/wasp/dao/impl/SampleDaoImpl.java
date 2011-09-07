
/**
 *
 * SampleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Sample Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Sample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDaoImpl extends WaspDaoImpl<Sample> implements edu.yu.einstein.wasp.dao.SampleDao {

	/**
	 * SampleDaoImpl() Constructor
	 *
	 *
	 */
	public SampleDaoImpl() {
		super();
		this.entityClass = Sample.class;
	}


	/**
	 * getSampleBySampleId(final int sampleId)
	 *
	 * @param final int sampleId
	 *
	 * @return sample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Sample getSampleBySampleId (final int sampleId) {
    		HashMap m = new HashMap();
		m.put("sampleId", sampleId);

		List<Sample> results = (List<Sample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Sample rt = new Sample();
			return rt;
		}
		return (Sample) results.get(0);
	}



}

