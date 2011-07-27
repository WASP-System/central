
/**
 *
 * JobTaskmeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskmeta object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public interface JobTaskmetaDao extends WaspDao<JobTaskmeta> {

  public JobTaskmeta getJobTaskmetaByJobTaskmetaId (final int jobTaskmetaId);

  public JobTaskmeta getJobTaskmetaByKJobtaskId (final String k, final int jobtaskId);

}

