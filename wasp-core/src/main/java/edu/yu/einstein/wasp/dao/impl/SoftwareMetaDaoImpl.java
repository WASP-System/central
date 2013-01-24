
/**
 *
 * SoftwareMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SoftwareMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SoftwareMeta;


@Transactional
@Repository
public class SoftwareMetaDaoImpl extends WaspMetaDaoImpl<SoftwareMeta> implements edu.yu.einstein.wasp.dao.SoftwareMetaDao {

	/**
	 * SoftwareMetaDaoImpl() Constructor
	 *
	 *
	 */
	public SoftwareMetaDaoImpl() {
		super();
		this.entityClass = SoftwareMeta.class;
	}


	/**
	 * getSoftwareMetaBySoftwareMetaId(final Integer softwareMetaId)
	 *
	 * @param final Integer softwareMetaId
	 *
	 * @return softwareMeta
	 */

	@Override
	@Transactional
	public SoftwareMeta getSoftwareMetaBySoftwareMetaId (final Integer softwareMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("softwareMetaId", softwareMetaId);

		List<SoftwareMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SoftwareMeta rt = new SoftwareMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSoftwareMetaByKSoftwareId(final String k, final Integer softwareId)
	 *
	 * @param final String k, final Integer softwareId
	 *
	 * @return softwareMeta
	 */

	@Override
	@Transactional
	public SoftwareMeta getSoftwareMetaByKSoftwareId (final String k, final Integer softwareId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("softwareId", softwareId);

		List<SoftwareMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SoftwareMeta rt = new SoftwareMeta();
			return rt;
		}
		return results.get(0);
	}

}

