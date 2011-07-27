
/**
 *
 * AChipseqArunService.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.AChipseqArunService;
import edu.yu.einstein.wasp.dao.AChipseqArunDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AChipseqArun;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AChipseqArunServiceImpl extends WaspServiceImpl<AChipseqArun> implements AChipseqArunService {

  private AChipseqArunDao aChipseqArunDao;
  @Autowired
  public void setAChipseqArunDao(AChipseqArunDao aChipseqArunDao) {
    this.aChipseqArunDao = aChipseqArunDao;
    this.setWaspDao(aChipseqArunDao);
  }
  public AChipseqArunDao getAChipseqArunDao() {
    return this.aChipseqArunDao;
  }

  // **

  
  public AChipseqArun getAChipseqArunByArunId (final int arunId) {
    return this.getAChipseqArunDao().getAChipseqArunByArunId(arunId);
  }
}

