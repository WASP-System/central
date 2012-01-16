
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleBarcode;

@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode) {
    		HashMap m = new HashMap();
		m.put("sampleBarcode", sampleBarcode);

		List<SampleBarcode> results = (List<SampleBarcode>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleBarcode rt = new SampleBarcode();
			return rt;
		}
		return (SampleBarcode) results.get(0);
	}



	/**
	 * getSampleBarcodeBySampleId(final int sampleId)
	 *
	 * @param final int sampleId
	 *
	 * @return sampleBarcode
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleBarcode getSampleBarcodeBySampleId (final int sampleId) {
    		HashMap m = new HashMap();
		m.put("sampleId", sampleId);

		List<SampleBarcode> results = (List<SampleBarcode>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleBarcode rt = new SampleBarcode();
			return rt;
		}
		return (SampleBarcode) results.get(0);
	}



	/**
	 * getSampleBarcodeByBarcodeId(final int barcodeId)
	 *
	 * @param final int barcodeId
	 *
	 * @return sampleBarcode
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId) {
    		HashMap m = new HashMap();
		m.put("barcodeId", barcodeId);

		List<SampleBarcode> results = (List<SampleBarcode>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleBarcode rt = new SampleBarcode();
			return rt;
		}
		return (SampleBarcode) results.get(0);
	}



}

