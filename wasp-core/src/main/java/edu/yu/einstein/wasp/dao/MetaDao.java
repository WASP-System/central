
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

  public Meta getMetaByK (final String k);

  public Meta setMeta(final String k, final String v);
  
  public Meta saveMeta(Meta meta);
}

