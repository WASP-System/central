
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

import edu.yu.einstein.wasp.model.Statejob;


public interface StatejobDao extends WaspDao<Statejob> {

  public Statejob getStatejobByStatejobId (final int statejobId);


}

