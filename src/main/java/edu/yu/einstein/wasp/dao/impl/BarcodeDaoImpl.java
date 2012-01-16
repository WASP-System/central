
/**
 *
 * BarcodeDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Barcode Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Barcode;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class BarcodeDaoImpl extends WaspDaoImpl<Barcode> implements edu.yu.einstein.wasp.dao.BarcodeDao {

	/**
	 * BarcodeDaoImpl() Constructor
	 *
	 *
	 */
	public BarcodeDaoImpl() {
		super();
		this.entityClass = Barcode.class;
	}


	/**
	 * getBarcodeByBarcodeId(final int barcodeId)
	 *
	 * @param final int barcodeId
	 *
	 * @return barcode
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Barcode getBarcodeByBarcodeId (final int barcodeId) {
    		HashMap m = new HashMap();
		m.put("barcodeId", barcodeId);

		List<Barcode> results = this.findByMap(m);

		if (results.size() == 0) {
			Barcode rt = new Barcode();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getBarcodeByBarcode(final String barcode)
	 *
	 * @param final String barcode
	 *
	 * @return barcode
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Barcode getBarcodeByBarcode (final String barcode) {
    		HashMap m = new HashMap();
		m.put("barcode", barcode);

		List<Barcode> results = this.findByMap(m);

		if (results.size() == 0) {
			Barcode rt = new Barcode();
			return rt;
		}
		return results.get(0);
	}



}

