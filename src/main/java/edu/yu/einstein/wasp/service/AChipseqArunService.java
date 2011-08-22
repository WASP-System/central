
/**
 *
 * AChipseqArunService.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AChipseqArunDao;
import edu.yu.einstein.wasp.model.AChipseqArun;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AChipseqArunService extends WaspService<AChipseqArun> {

  public void setAChipseqArunDao(AChipseqArunDao aChipseqArunDao);
  public AChipseqArunDao getAChipseqArunDao();

  public AChipseqArun getAChipseqArunByArunId (final int arunId);

}

