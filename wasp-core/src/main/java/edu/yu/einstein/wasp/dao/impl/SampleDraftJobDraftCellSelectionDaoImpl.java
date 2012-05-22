
/**
 *
 * SampleDraftJobDraftCellSelectionDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftJobDraftCellSelection Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDraftJobDraftCellSelectionDaoImpl extends WaspDaoImpl<SampleDraftJobDraftCellSelection> implements edu.yu.einstein.wasp.dao.SampleDraftJobDraftCellSelectionDao {

	/**
	 * SampleDraftJobDraftCellSelectionDaoImpl() Constructor
	 *
	 *
	 */
	public SampleDraftJobDraftCellSelectionDaoImpl() {
		super();
		this.entityClass = SampleDraftJobDraftCellSelection.class;
	}


	/**
	 * getSampleDraftJobDraftCellSelectionBySampleDraftJobDraftCellSelectionId(final int sampleDraftJobDraftCellSelectionId)
	 *
	 * @param final int sampleDraftJobDraftCellSelectionId
	 *
	 * @return sampleDraftJobDraftCellSelection
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftJobDraftCellSelection getSampleDraftJobDraftCellSelectionBySampleDraftJobDraftCellSelectionId (final int sampleDraftJobDraftCellSelectionId) {
    		HashMap m = new HashMap();
		m.put("sampleDraftJobDraftCellSelectionId", sampleDraftJobDraftCellSelectionId);

		List<SampleDraftJobDraftCellSelection> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleDraftJobDraftCellSelection rt = new SampleDraftJobDraftCellSelection();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleDraftJobDraftCellSelectionByJobDraftCellSelectionIdLibraryIndex(final int jobDraftCellSelectionId, final int libraryIndex)
	 *
	 * @param final int jobDraftCellSelectionId, final int libraryIndex
	 *
	 * @return sampleDraftJobDraftCellSelection
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftJobDraftCellSelection getSampleDraftJobDraftCellSelectionByJobDraftCellSelectionIdLibraryIndex (final int jobDraftCellSelectionId, final int libraryIndex) {
    		HashMap m = new HashMap();
		m.put("jobDraftCellSelectionId", jobDraftCellSelectionId);
		m.put("libraryIndex", libraryIndex);

		List<SampleDraftJobDraftCellSelection> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleDraftJobDraftCellSelection rt = new SampleDraftJobDraftCellSelection();
			return rt;
		}
		return results.get(0);
	}



}

