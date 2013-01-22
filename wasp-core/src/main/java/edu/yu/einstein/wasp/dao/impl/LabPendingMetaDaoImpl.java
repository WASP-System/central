
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


@Transactional
@Repository
public class LabPendingMetaDaoImpl extends WaspDaoImpl<LabPendingMeta> implements edu.yu.einstein.wasp.dao.LabPendingMetaDao {

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
	@Transactional
	public LabPendingMeta getLabPendingMetaByLabPendingMetaId (final int labPendingMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("labPendingMetaId", labPendingMetaId);

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
	@Transactional
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




	/**
	 * updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList)
	 *
	 * @param labpendingId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateByLabpendingId (final int labpendingId, final List<LabPendingMeta> metaList) {
		for (LabPendingMeta m:metaList) {
			LabPendingMeta currentMeta = getLabPendingMetaByKLabpendingId(m.getK(), labpendingId);
			if (currentMeta.getLabPendingMetaId() == null){
				// metadata value not in database yet
				m.setLabpendingId(labpendingId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
	}



}

