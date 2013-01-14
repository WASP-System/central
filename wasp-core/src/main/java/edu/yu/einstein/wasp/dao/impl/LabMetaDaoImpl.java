
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

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabMetaDaoImpl extends WaspMetaDaoImpl<LabMeta> implements edu.yu.einstein.wasp.dao.LabMetaDao {

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
	@SuppressWarnings("unchecked")
	@Transactional
	public LabMeta getLabMetaByLabMetaId (final int labMetaId) {
    		HashMap m = new HashMap();
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
	@SuppressWarnings("unchecked")
	@Transactional
	public LabMeta getLabMetaByKLabId (final String k, final int labId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("labId", labId);

		List<LabMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			LabMeta rt = new LabMeta();
			return rt;
		}
		return results.get(0);
	}





}

