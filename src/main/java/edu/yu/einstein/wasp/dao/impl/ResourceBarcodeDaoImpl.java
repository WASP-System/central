
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceBarcode;

@SuppressWarnings("unchecked")
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
	 * getResourceBarcodeByResourceBarcodeId(final int resourceBarcodeId)
	 *
	 * @param final int resourceBarcodeId
	 *
	 * @return resourceBarcode
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final int resourceBarcodeId) {
    		HashMap m = new HashMap();
		m.put("resourceBarcodeId", resourceBarcodeId);

		List<ResourceBarcode> results = (List<ResourceBarcode>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceBarcode rt = new ResourceBarcode();
			return rt;
		}
		return (ResourceBarcode) results.get(0);
	}



	/**
	 * getResourceBarcodeByResourceId(final int resourceId)
	 *
	 * @param final int resourceId
	 *
	 * @return resourceBarcode
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceBarcode getResourceBarcodeByResourceId (final int resourceId) {
    		HashMap m = new HashMap();
		m.put("resourceId", resourceId);

		List<ResourceBarcode> results = (List<ResourceBarcode>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceBarcode rt = new ResourceBarcode();
			return rt;
		}
		return (ResourceBarcode) results.get(0);
	}



	/**
	 * getResourceBarcodeByBarcodeId(final int barcodeId)
	 *
	 * @param final int barcodeId
	 *
	 * @return resourceBarcode
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public ResourceBarcode getResourceBarcodeByBarcodeId (final int barcodeId) {
    		HashMap m = new HashMap();
		m.put("barcodeId", barcodeId);

		List<ResourceBarcode> results = (List<ResourceBarcode>) this.findByMap((Map) m);

		if (results.size() == 0) {
			ResourceBarcode rt = new ResourceBarcode();
			return rt;
		}
		return (ResourceBarcode) results.get(0);
	}



}

