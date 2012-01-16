
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SubtypeSampleMeta;

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
	 * updateBySubtypeSampleId (final string area, final int subtypeSampleId, final List<SubtypeSampleMeta> metaList)
	 *
	 * @param subtypeSampleId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateBySubtypeSampleId (final String area, final int subtypeSampleId, final List<SubtypeSampleMeta> metaList) {
		entityManager.createNativeQuery("delete from subtypeSamplemeta where subtypeSampleId=:subtypeSampleId and k like :area").setParameter("subtypeSampleId", subtypeSampleId).setParameter("area", area + ".%").executeUpdate();

		for (SubtypeSampleMeta m:metaList) {
			m.setSubtypeSampleId(subtypeSampleId);
			entityManager.persist(m);
		}
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
		entityManager.createNativeQuery("delete from subtypeSamplemeta where subtypeSampleId=:subtypeSampleId").setParameter("subtypeSampleId", subtypeSampleId).executeUpdate();

		for (SubtypeSampleMeta m:metaList) {
			m.setSubtypeSampleId(subtypeSampleId);
			entityManager.persist(m);
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

