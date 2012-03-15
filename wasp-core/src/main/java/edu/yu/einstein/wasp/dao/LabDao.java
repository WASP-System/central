
/**
 *
 * LabDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Lab Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.Lab;


public interface LabDao extends WaspDao<Lab> {

  public Lab getLabByLabId (final int labId);

  public Lab getLabByName (final String name);

  public Lab getLabByPrimaryUserId (final int primaryUserId);

  public List<Lab> getActiveLabs();


}

