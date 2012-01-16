
/**
 *
 * SampleDraftCellDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftCell Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleDraftCell;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDraftCellDaoImpl extends WaspDaoImpl<SampleDraftCell> implements edu.yu.einstein.wasp.dao.SampleDraftCellDao {

	/**
	 * SampleDraftCellDaoImpl() Constructor
	 *
	 *
	 */
	public SampleDraftCellDaoImpl() {
		super();
		this.entityClass = SampleDraftCell.class;
	}


	/**
	 * getSampleDraftCellBySampleDraftCellId(final int sampleDraftCellId)
	 *
	 * @param final int sampleDraftCellId
	 *
	 * @return sampleDraftCell
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftCell getSampleDraftCellBySampleDraftCellId (final int sampleDraftCellId) {
    		HashMap m = new HashMap();
		m.put("sampleDraftCellId", sampleDraftCellId);

		List<SampleDraftCell> results = (List<SampleDraftCell>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleDraftCell rt = new SampleDraftCell();
			return rt;
		}
		return (SampleDraftCell) results.get(0);
	}



	/**
	 * getSampleDraftCellByJobdraftcellIdLibraryindex(final int jobdraftcellId, final int libraryindex)
	 *
	 * @param final int jobdraftcellId, final int libraryindex
	 *
	 * @return sampleDraftCell
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftCell getSampleDraftCellByJobdraftcellIdLibraryindex (final int jobdraftcellId, final int libraryindex) {
    		HashMap m = new HashMap();
		m.put("jobdraftcellId", jobdraftcellId);
		m.put("libraryindex", libraryindex);

		List<SampleDraftCell> results = (List<SampleDraftCell>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleDraftCell rt = new SampleDraftCell();
			return rt;
		}
		return (SampleDraftCell) results.get(0);
	}



}

