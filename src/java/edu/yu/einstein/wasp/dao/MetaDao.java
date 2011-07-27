
/**
 *
 * MetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the MetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface MetaDao extends WaspDao<Meta> {

  public Meta getMetaByMetaId (final int metaId);

  public Meta getMetaByPropertyK (final String property, final String k);

  public Meta getMetaByPropertyV (final String property, final String v);

}

