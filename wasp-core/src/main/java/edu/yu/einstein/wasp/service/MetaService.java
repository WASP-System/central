
/**
 *
 * MetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the MetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.MetaDao;
import edu.yu.einstein.wasp.model.Meta;

@Service
public interface MetaService extends WaspMetaService<Meta> {

	/**
	 * setMetaDao(MetaDao metaDao)
	 *
	 * @param metaDao
	 *
	 */
	public void setMetaDao(MetaDao metaDao);

	/**
	 * getMetaDao();
	 *
	 * @return metaDao
	 *
	 */
	public MetaDao getMetaDao();

  public Meta getMetaByMetaId (final int metaId);

  public Meta getMetaByPropertyK (final String property, final String k);

  public Meta getMetaByPropertyV (final String property, final String v);


}

