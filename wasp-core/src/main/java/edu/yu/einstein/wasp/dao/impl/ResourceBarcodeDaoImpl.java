
/**
 *
 * ResourceBarcodeDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcode Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceBarcode;


@Transactional
@Repository
public class ResourceBarcodeDaoImpl extends WaspDaoImpl<ResourceBarcode> implements edu.yu.einstein.wasp.dao.ResourceBarcodeDao {

	/**
	 * ResourceBarcodeDaoImpl() Constructor
	 *
	 *
	 */
	public ResourceBarcodeDaoImpl() {
		super();
		this.entityClass = ResourceBarcode.class;
	}


	/**
	 * getResourceBarcodeByResourceBarcodeId(final Integer resourceBarcodeId)
	 *
	 * @param final Integer resourceBarcodeId
	 *
	 * @return resourceBarcode
	 */

	@Override
	@Transactional
	public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final Integer resourceBarcodeId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", resourceBarcodeId);

		List<ResourceBarcode> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceBarcode rt = new ResourceBarcode();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceBarcodeByResourceId(final Integer resourceId)
	 *
	 * @param final Integer resourceId
	 *
	 * @return resourceBarcode
	 */

	@Override
	@Transactional
	public ResourceBarcode getResourceBarcodeByResourceId (final Integer resourceId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("resourceId", resourceId);

		List<ResourceBarcode> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceBarcode rt = new ResourceBarcode();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getResourceBarcodeByBarcodeId(final Integer barcodeId)
	 *
	 * @param final Integer barcodeId
	 *
	 * @return resourceBarcode
	 */

	@Override
	@Transactional
	public ResourceBarcode getResourceBarcodeByBarcodeId (final Integer barcodeId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("barcodeId", barcodeId);

		List<ResourceBarcode> results = this.findByMap(m);

		if (results.size() == 0) {
			ResourceBarcode rt = new ResourceBarcode();
			return rt;
		}
		return results.get(0);
	}



}

