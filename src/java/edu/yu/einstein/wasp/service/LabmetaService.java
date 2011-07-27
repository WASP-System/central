
/**
 *
 * LabmetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabmetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.LabmetaDao;
import edu.yu.einstein.wasp.model.Labmeta;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface LabmetaService extends WaspService<Labmeta> {

  public void setLabmetaDao(LabmetaDao labmetaDao);
  public LabmetaDao getLabmetaDao();

  public Labmeta getLabmetaByLabmetaId (final int labmetaId);

  public Labmeta getLabmetaByKLabId (final String k, final int labId);

  public void updateByLabId (final int labId, final List<Labmeta> metaList);

}

