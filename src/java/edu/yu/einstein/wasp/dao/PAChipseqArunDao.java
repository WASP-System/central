
/**
 *
 * PAChipseqArun.java 
 * @author echeng (table2type.pl)
 *  
 * the PAChipseqArun object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public interface PAChipseqArunDao extends WaspDao<PAChipseqArun> {

  public PAChipseqArun getPAChipseqArunByPArunId (final int pArunId);

}

