
/**
 *
 * RunFileService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunFileService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RunFileDao;
import edu.yu.einstein.wasp.model.RunFile;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface RunFileService extends WaspService<RunFile> {

  public void setRunFileDao(RunFileDao runFileDao);
  public RunFileDao getRunFileDao();

  public RunFile getRunFileByRunlanefileId (final int runlanefileId);

  public RunFile getRunFileByFileId (final int fileId);

}

