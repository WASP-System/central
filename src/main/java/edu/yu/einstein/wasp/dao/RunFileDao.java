
/**
 *
 * RunFileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunFile Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface RunFileDao extends WaspDao<RunFile> {

  public RunFile getRunFileByRunlanefileId (final int runlanefileId);

  public RunFile getRunFileByFileId (final int fileId);


}

