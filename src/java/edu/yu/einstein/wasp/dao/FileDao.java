
/**
 *
 * FileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the FileDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface FileDao extends WaspDao<File> {

  public File getFileByFileId (final int fileId);

}

