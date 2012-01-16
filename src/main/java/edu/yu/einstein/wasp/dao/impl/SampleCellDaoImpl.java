
/**
 *
 * SampleCellDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleCell Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleCell;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleCellDaoImpl extends WaspDaoImpl<SampleCell> implements edu.yu.einstein.wasp.dao.SampleCellDao {

	/**
	 * SampleCellDaoImpl() Constructor
	 *
	 *
	 */
	public SampleCellDaoImpl() {
		super();
		this.entityClass = SampleCell.class;
	}


	/**
	 * getSampleCellBySampleCellId(final int sampleCellId)
	 *
	 * @param final int sampleCellId
	 *
	 * @return sampleCell
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleCell getSampleCellBySampleCellId (final int sampleCellId) {
    		HashMap m = new HashMap();
		m.put("sampleCellId", sampleCellId);

		List<SampleCell> results = (List<SampleCell>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleCell rt = new SampleCell();
			return rt;
		}
		return (SampleCell) results.get(0);
	}



	/**
	 * getSampleCellByJobcellIdLibraryindex(final int jobcellId, final int libraryindex)
	 *
	 * @param final int jobcellId, final int libraryindex
	 *
	 * @return sampleCell
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleCell getSampleCellByJobcellIdLibraryindex (final int jobcellId, final int libraryindex) {
    		HashMap m = new HashMap();
		m.put("jobcellId", jobcellId);
		m.put("libraryindex", libraryindex);

		List<SampleCell> results = (List<SampleCell>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleCell rt = new SampleCell();
			return rt;
		}
		return (SampleCell) results.get(0);
	}



}

