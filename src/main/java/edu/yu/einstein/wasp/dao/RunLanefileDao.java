
/**
 *
 * RunLanefileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLanefileDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface RunLanefileDao extends WaspDao<RunLanefile> {

  public RunLanefile getRunLanefileByRunLanefileId (final int runLanefileId);

  public RunLanefile getRunLanefileByFileId (final int fileId);

}

