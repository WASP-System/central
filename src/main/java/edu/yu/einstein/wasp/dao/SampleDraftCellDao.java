
/**
 *
 * SampleDraftCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SampleDraftCellDao extends WaspDao<SampleDraftCell> {

  public SampleDraftCell getSampleDraftCellBySampleDraftCellId (final int sampleDraftCellId);

  public SampleDraftCell getSampleDraftCellByJobdraftcellIdLibraryindex (final int jobdraftcellId, final int libraryindex);


}

