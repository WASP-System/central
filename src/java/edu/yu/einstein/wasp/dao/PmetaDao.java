
/**
 *
 * Pmeta.java 
 * @author echeng (table2type.pl)
 *  
 * the Pmeta object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public interface PmetaDao extends WaspDao<Pmeta> {

  public Pmeta getPmetaByPmetaId (final int pmetaId);

  public Pmeta getPmetaByKPId (final String k, final int pId);

}

