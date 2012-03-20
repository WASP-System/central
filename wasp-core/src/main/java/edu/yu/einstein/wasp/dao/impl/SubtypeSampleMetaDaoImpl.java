
/**
 *
 * SubtypeSampleMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SubtypeSampleMeta;
import edu.yu.einstein.wasp.model.UserPendingMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SubtypeSampleMetaDaoImpl extends WaspDaoImpl<SubtypeSampleMeta> implements edu.yu.einstein.wasp.dao.SubtypeSampleMetaDao {

	/**
	 * SubtypeSampleMetaDaoImpl() Constructor
	 *
	 *
	 */
	public SubtypeSampleMetaDaoImpl() {
		super();
		this.entityClass = SubtypeSampleMeta.class;
	}


	/**
	 * getSubtypeSampleMetaBySubtypeSampleMetaId(final int subtypeSampleMetaId)
	 *
	 * @param final int subtypeSampleMetaId
	 *
	 * @return subtypeSampleMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SubtypeSampleMeta getSubtypeSampleMetaBySubtypeSampleMetaId (final int subtypeSampleMetaId) {
    		HashMap m = new HashMap();
		m.put("subtypeSampleMetaId", subtypeSampleMetaId);

		List<SubtypeSampleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SubtypeSampleMeta rt = new SubtypeSampleMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSubtypeSampleMetaByKSubtypeSampleId(final String k, final int subtypeSampleId)
	 *
	 * @param final String k, final int subtypeSampleId
	 *
	 * @return subtypeSampleMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SubtypeSampleMeta getSubtypeSampleMetaByKSubtypeSampleId (final String k, final int subtypeSampleId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("subtypeSampleId", subtypeSampleId);

		List<SubtypeSampleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SubtypeSampleMeta rt = new SubtypeSampleMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateBySubtypeSampleId (final int subtypeSampleId, final List<SubtypeSampleMeta> metaList)
	 *
	 * @param subtypeSampleId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateBySubtypeSampleId (final int subtypeSampleId, final List<SubtypeSampleMeta> metaList) {
		for (SubtypeSampleMeta m:metaList) {
			SubtypeSampleMeta currentMeta = getSubtypeSampleMetaByKSubtypeSampleId(m.getK(), subtypeSampleId);
			if (currentMeta.getSubtypeSampleMetaId() == null){
				// metadata value not in database yet
				m.setSubtypeSampleId(subtypeSampleId);
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


	@Override
	public List<SubtypeSampleMeta> getSubtypeSamplesMetaBySubtypeSampleId (final int subtypeSampleId) {
		HashMap m = new HashMap();
		m.put("subtypeSampleId", subtypeSampleId);

		List<SubtypeSampleMeta> results = this.findByMap(m);

		return results;
	}


}

