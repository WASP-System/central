
/**
 *
 * AcctGrantjobDraftDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjobDraft Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctGrantjobDraft;


@Transactional("entityManager")
@Repository
public class AcctGrantjobDraftDaoImpl extends WaspDaoImpl<AcctGrantjobDraft> implements edu.yu.einstein.wasp.dao.AcctGrantjobDraftDao {

	/**
	 * AcctGrantjobDaoImpl() Constructor
	 *
	 *
	 */
	public AcctGrantjobDraftDaoImpl() {
		super();
		this.entityClass = AcctGrantjobDraft.class;
	}


	/**
	 * getAcctGrantjobByJobDraftId(final int jobDraftId)
	 *
	 * @param final int jobDraftId
	 *
	 * @return acctGrantjob
	 */

	@Override
	@Transactional("entityManager")
	public AcctGrantjobDraft getAcctGrantjobByJobDraftId (final int jobDraftId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", jobDraftId);

		List<AcctGrantjobDraft> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctGrantjobDraft rt = new AcctGrantjobDraft();
			return rt;
		}
		return results.get(0);
	}



}

