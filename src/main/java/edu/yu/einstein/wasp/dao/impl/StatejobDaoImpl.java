
/**
 *
 * StatejobDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Statejob Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Statejob;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StatejobDaoImpl extends WaspDaoImpl<Statejob> implements edu.yu.einstein.wasp.dao.StatejobDao {

	/**
	 * StatejobDaoImpl() Constructor
	 *
	 *
	 */
	public StatejobDaoImpl() {
		super();
		this.entityClass = Statejob.class;
	}


	/**
	 * getStatejobByStatejobId(final int statejobId)
	 *
	 * @param final int statejobId
	 *
	 * @return statejob
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Statejob getStatejobByStatejobId (final int statejobId) {
    		HashMap m = new HashMap();
		m.put("statejobId", statejobId);

		List<Statejob> results = this.findByMap(m);

		if (results.size() == 0) {
			Statejob rt = new Statejob();
			return rt;
		}
		return results.get(0);
	}



}

