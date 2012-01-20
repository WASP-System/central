
/**
 *
 * SoftwareDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Software Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SoftwareDao extends WaspDao<Software> {

  public Software getSoftwareBySoftwareId (final Integer softwareId);

  public Software getSoftwareByIName (final String iName);

  public Software getSoftwareByName (final String name);


}

