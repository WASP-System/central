
/**
 *
 * AChipseqArunDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AChipseqArunDao extends WaspDao<AChipseqArun> {

  public AChipseqArun getAChipseqArunByArunId (final int arunId);

}

