
/**
 *
 * MetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the MetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.MetaDao;
import edu.yu.einstein.wasp.model.Meta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface MetaService extends WaspService<Meta> {

  public void setMetaDao(MetaDao metaDao);
  public MetaDao getMetaDao();

  public Meta getMetaByMetaId (final int metaId);

  public Meta getMetaByPropertyK (final String property, final String k);

  public Meta getMetaByPropertyV (final String property, final String v);

}

