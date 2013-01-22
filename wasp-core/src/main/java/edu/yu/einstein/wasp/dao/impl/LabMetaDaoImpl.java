
/**
 *
 * LabMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.LabMeta;


@Transactional
@Repository
public class LabMetaDaoImpl extends WaspDaoImpl<LabMeta> implements edu.yu.einstein.wasp.dao.LabMetaDao {

	/**
	 * LabMetaDaoImpl() Constructor
	 *
	 *
	 */
	public LabMetaDaoImpl() {
		super();
		this.entityClass = LabMeta.class;
	}


	/**
	 * getLabMetaByLabMetaId(final int labMetaId)
	 *
	 * @param final int labMetaId
	 *
	 * @return labMeta
	 */

	@Override
	@Transactional
	public LabMeta getLabMetaByLabMetaId (final int labMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("labMetaId", labMetaId);

		List<LabMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			LabMeta rt = new LabMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getLabMetaByKLabId(final String k, final int labId)
	 *
	 * @param final String k, final int labId
	 *
	 * @return labMeta
	 */

	@Override
	@Transactional
	public LabMeta getLabMetaByKLabId (final String k, final int labId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("labId", labId);

		List<LabMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			LabMeta rt = new LabMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateByLabId (final int labId, final List<LabMeta> metaList)
	 *
	 * @param labId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateByLabId (final int labId, final List<LabMeta> metaList) {
		for (LabMeta m:metaList) {
			LabMeta currentMeta = getLabMetaByKLabId(m.getK(), labId);
			if (currentMeta.getLabMetaId() == null){
				// metadata value not in database yet
				m.setLabId(labId);
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

