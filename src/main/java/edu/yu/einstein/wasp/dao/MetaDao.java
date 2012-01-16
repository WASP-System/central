
/**
 *
 * MetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Meta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Meta;


public interface MetaDao extends WaspDao<Meta> {

  public Meta getMetaByMetaId (final int metaId);

  public Meta getMetaByPropertyK (final String property, final String k);

  public Meta getMetaByPropertyV (final String property, final String v);


}

