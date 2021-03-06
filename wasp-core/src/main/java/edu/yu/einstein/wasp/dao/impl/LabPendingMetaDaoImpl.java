
/**
 *
 * LabPendingMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.LabPendingMeta;


@Transactional("entityManager")
@Repository
public class LabPendingMetaDaoImpl extends WaspMetaDaoImpl<LabPendingMeta> implements edu.yu.einstein.wasp.dao.LabPendingMetaDao {

	/**
	 * LabPendingMetaDaoImpl() Constructor
	 *
	 *
	 */
	public LabPendingMetaDaoImpl() {
		super();
		this.entityClass = LabPendingMeta.class;
	}


	/**
	 * getLabPendingMetaByLabPendingMetaId(final int labPendingMetaId)
	 *
	 * @param final int labPendingMetaId
	 *
	 * @return labPendingMeta
	 */

	@Override
	@Transactional("entityManager")
	public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", labPendingMetaId);

		List<LabPendingMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			LabPendingMeta rt = new LabPendingMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getLabPendingMetaByKLabpendingId(final String k, final int labpendingId)
	 *
	 * @param final String k, final int labpendingId
	 *
	 * @return labPendingMeta
	 */

	@Override
	@Transactional("entityManager")
	public LabPendingMeta getLabPendingMetaByKLabpendingId (final String k, final int labpendingId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("labpendingId", labpendingId);

		List<LabPendingMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			LabPendingMeta rt = new LabPendingMeta();
			return rt;
		}
		return results.get(0);
	}


}

