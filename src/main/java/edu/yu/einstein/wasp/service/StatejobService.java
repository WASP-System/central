
/**
 *
 * StatejobService.java 
 * @author echeng (table2type.pl)
 *  
 * the StatejobService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.model.Statejob;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StatejobService extends WaspService<Statejob> {

  public void setStatejobDao(StatejobDao statejobDao);
  public StatejobDao getStatejobDao();

  public Statejob getStatejobByStatejobId (final int statejobId);

}

