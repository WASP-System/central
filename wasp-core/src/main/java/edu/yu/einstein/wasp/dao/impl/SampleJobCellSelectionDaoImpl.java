
/**
 *
 * SampleJobCellSelectionDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleJobCellSelection Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleJobCellSelection;


@Transactional
@Repository
public class SampleJobCellSelectionDaoImpl extends WaspDaoImpl<SampleJobCellSelection> implements edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao {

	/**
	 * SampleJobCellSelectionDaoImpl() Constructor
	 *
	 *
	 */
	public SampleJobCellSelectionDaoImpl() {
		super();
		this.entityClass = SampleJobCellSelection.class;
	}


	/**
	 * getSampleJobCellSelectionBySampleJobCellSelectionId (final int sampleJobCellSelectionId)
	 *
	 * @param final int sampleJobCellSelectionId
	 *
	 * @return sampleJobCellSelection
	 */

	@Override
	@Transactional
	public SampleJobCellSelection getSampleJobCellSelectionBySampleJobCellSelectionId (final int sampleJobCellSelectionId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleJobCellSelectionId", sampleJobCellSelectionId);

		List<SampleJobCellSelection> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleJobCellSelection rt = new SampleJobCellSelection();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleJobCellSelectionBySampleJobCellSelectionIdLibraryIndex (final int jobCellSelectionId, final int libraryIndex)
	 *
	 * @param final int jobCellSelectionId, final int libraryIndex
	 *
	 * @return sampleJobCellSelection
	 */

	@Override
	@Transactional
	public SampleJobCellSelection getSampleJobCellSelectionBySampleJobCellSelectionIdLibraryIndex (final int jobCellSelectionId, final int libraryIndex) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobCellSelectionId", jobCellSelectionId);
		m.put("libraryIndex", libraryIndex);

		List<SampleJobCellSelection> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleJobCellSelection rt = new SampleJobCellSelection();
			return rt;
		}
		return results.get(0);
	}



}

