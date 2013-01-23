
/**
 *
 * SampleBarcodeDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcode Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleBarcode;


@Transactional
@Repository
public class SampleBarcodeDaoImpl extends WaspDaoImpl<SampleBarcode> implements edu.yu.einstein.wasp.dao.SampleBarcodeDao {

	/**
	 * SampleBarcodeDaoImpl() Constructor
	 *
	 *
	 */
	public SampleBarcodeDaoImpl() {
		super();
		this.entityClass = SampleBarcode.class;
	}


	/**
	 * getSampleBarcodeBySampleBarcode(final int sampleBarcode)
	 *
	 * @param final int sampleBarcode
	 *
	 * @return sampleBarcode
	 */

	@Override
	@Transactional
	public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleBarcode", sampleBarcode);

		List<SampleBarcode> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleBarcode rt = new SampleBarcode();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleBarcodeBySampleId(final int sampleId)
	 *
	 * @param final int sampleId
	 *
	 * @return sampleBarcode
	 */

	@Override
	@Transactional
	public SampleBarcode getSampleBarcodeBySampleId (final int sampleId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", sampleId);

		List<SampleBarcode> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleBarcode rt = new SampleBarcode();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleBarcodeByBarcodeId(final int barcodeId)
	 *
	 * @param final int barcodeId
	 *
	 * @return sampleBarcode
	 */

	@Override
	@Transactional
	public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("barcodeId", barcodeId);

		List<SampleBarcode> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleBarcode rt = new SampleBarcode();
			return rt;
		}
		return results.get(0);
	}



}

