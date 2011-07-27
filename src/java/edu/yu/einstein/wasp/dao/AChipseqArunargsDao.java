
/**
 *
 * AChipseqArunargsDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunargsDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AChipseqArunargsDao extends WaspDao<AChipseqArunargs> {

  public AChipseqArunargs getAChipseqArunargsByArunargsId (final int arunargsId);

  public AChipseqArunargs getAChipseqArunargsByArunIdArgc (final Integer arunId, final int argc);

}

