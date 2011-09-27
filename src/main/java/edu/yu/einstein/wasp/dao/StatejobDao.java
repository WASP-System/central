
/**
 *
 * StatejobDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Statejob Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface StatejobDao extends WaspDao<Statejob> {

  public Statejob getStatejobByStatejobId (final int statejobId);


}

