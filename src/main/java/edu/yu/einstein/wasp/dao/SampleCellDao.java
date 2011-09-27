
/**
 *
 * SampleCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SampleCellDao extends WaspDao<SampleCell> {

  public SampleCell getSampleCellBySampleCellId (final int sampleCellId);

  public SampleCell getSampleCellByJobcellIdLibraryindex (final int jobcellId, final int libraryindex);


}

