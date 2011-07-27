
/**
 *
 * AChipseqArunargsService.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunargsService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AChipseqArunargsDao;
import edu.yu.einstein.wasp.model.AChipseqArunargs;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AChipseqArunargsService extends WaspService<AChipseqArunargs> {

  public void setAChipseqArunargsDao(AChipseqArunargsDao aChipseqArunargsDao);
  public AChipseqArunargsDao getAChipseqArunargsDao();

  public AChipseqArunargs getAChipseqArunargsByArunargsId (final int arunargsId);

  public AChipseqArunargs getAChipseqArunargsByArunIdArgc (final Integer arunId, final int argc);

}

