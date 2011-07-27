
/**
 *
 * LabmetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabmetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface LabmetaDao extends WaspDao<Labmeta> {

  public Labmeta getLabmetaByLabmetaId (final int labmetaId);

  public Labmeta getLabmetaByKLabId (final String k, final int labId);

  public void updateByLabId (final int labId, final List<Labmeta> metaList);

}

