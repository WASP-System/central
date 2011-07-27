
/**
 *
 * LabService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.model.Lab;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface LabService extends WaspService<Lab> {

  public void setLabDao(LabDao labDao);
  public LabDao getLabDao();

  public Lab getLabByLabId (final int labId);

  public Lab getLabByName (final String name);

}

