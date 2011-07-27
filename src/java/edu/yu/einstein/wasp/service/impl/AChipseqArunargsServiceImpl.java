
/**
 *
 * AChipseqArunargsService.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunargsService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.AChipseqArunargsService;
import edu.yu.einstein.wasp.dao.AChipseqArunargsDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AChipseqArunargs;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AChipseqArunargsServiceImpl extends WaspServiceImpl<AChipseqArunargs> implements AChipseqArunargsService {

  private AChipseqArunargsDao aChipseqArunargsDao;
  @Autowired
  public void setAChipseqArunargsDao(AChipseqArunargsDao aChipseqArunargsDao) {
    this.aChipseqArunargsDao = aChipseqArunargsDao;
    this.setWaspDao(aChipseqArunargsDao);
  }
  public AChipseqArunargsDao getAChipseqArunargsDao() {
    return this.aChipseqArunargsDao;
  }

  // **

  
  public AChipseqArunargs getAChipseqArunargsByArunargsId (final int arunargsId) {
    return this.getAChipseqArunargsDao().getAChipseqArunargsByArunargsId(arunargsId);
  }

  public AChipseqArunargs getAChipseqArunargsByArunIdArgc (final Integer arunId, final int argc) {
    return this.getAChipseqArunargsDao().getAChipseqArunargsByArunIdArgc(arunId, argc);
  }
}

