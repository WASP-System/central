
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;
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

	public List<Sample> getSamplesByJobId (final int jobId) {
			
		 List<Sample> res = ( List<Sample>)getJpaTemplate().execute(new JpaCallback() {

				   public Object doInJpa(EntityManager em) throws PersistenceException {
					   
					   String sql=
						   "SELECT s.sampleId, s.name\n"+
						   "FROM jobsample js\n"+
						   "JOIN sample s ON s.sampleId = js.sampleId\n"+
						   "WHERE jobId = :jobId\n"+
						   "ORDER by s.name";
						  
					   
					   List<Sample> result=new ArrayList<Sample>();
					   
					   List<Object[]> listObj=em.createNativeQuery(sql).setParameter("jobId", jobId).getResultList();
					   
					   for(Object[] o:listObj) {
						   
						   Integer sampleId=(Integer)o[0];					  
						   String name=(String)o[1];
						   
						   Sample sample = new Sample();
						   sample.setSampleId(sampleId);
						   sample.setName(name);
						   
						   result.add(sample);
						   
					   }
					   return result;
				   }

				  });
		
				 
			return res;
			
		
		}

}

