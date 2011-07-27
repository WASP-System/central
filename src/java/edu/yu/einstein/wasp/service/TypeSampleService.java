
/**
 *
 * TypeSampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.TypeSampleDao;
import edu.yu.einstein.wasp.model.TypeSample;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TypeSampleService extends WaspService<TypeSample> {

  public void setTypeSampleDao(TypeSampleDao typeSampleDao);
  public TypeSampleDao getTypeSampleDao();

  public TypeSample getTypeSampleByTypeSampleId (final int typeSampleId);

  public TypeSample getTypeSampleByIName (final String iName);

  public TypeSample getTypeSampleByName (final String name);

}

