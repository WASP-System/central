
/**
 *
 * MetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the MetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.MetaService;
import edu.yu.einstein.wasp.dao.MetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Meta;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MetaServiceImpl extends WaspServiceImpl<Meta> implements MetaService {

  private MetaDao metaDao;
  @Autowired
  public void setMetaDao(MetaDao metaDao) {
    this.metaDao = metaDao;
    this.setWaspDao(metaDao);
  }
  public MetaDao getMetaDao() {
    return this.metaDao;
  }

  // **

  
  public Meta getMetaByMetaId (final int metaId) {
    return this.getMetaDao().getMetaByMetaId(metaId);
  }

  public Meta getMetaByPropertyK (final String property, final String k) {
    return this.getMetaDao().getMetaByPropertyK(property, k);
  }

  public Meta getMetaByPropertyV (final String property, final String v) {
    return this.getMetaDao().getMetaByPropertyV(property, v);
  }
}

