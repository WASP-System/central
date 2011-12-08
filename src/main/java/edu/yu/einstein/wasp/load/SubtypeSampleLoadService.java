
package edu.yu.einstein.wasp.load;

import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.service.UiFieldService;

import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

import edu.yu.einstein.wasp.model.*;

import java.util.Map; 
import java.util.HashMap; 
import java.util.Set; 
import java.util.List; 
import java.util.Date; 
import java.util.ArrayList; 
import java.util.Locale; 

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.*;

import org.springframework.context.MessageSource;

import org.springframework.util.StringUtils;

import util.spring.PostInitialize;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname
 *   - name
 *   - uifields (List<UiFields)
 *   - sampleType (string to typesample iname table)
 *
 */

@Transactional
public class SubtypeSampleLoadService extends WaspLoadService {

  @Autowired
  private SubtypeSampleService subtypeSampleService;

  @Autowired
  private TypeSampleService typeSampleService;

  private String sampleType; 
  public void setSampleType(String sampleType) {this.sampleType = sampleType; }

  public SubtypeSampleLoadService (){};

  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    TypeSample typeSample = typeSampleService.getTypeSampleByIName(sampleType); 

    SubtypeSample subtypeSample = subtypeSampleService.getSubtypeSampleByIName(iname); 

    // inserts or update workflow
    if (subtypeSample.getSubtypeSampleId() == 0) { 
      subtypeSample = new SubtypeSample();

      subtypeSample.setIName(iname);
      subtypeSample.setName(name);
      subtypeSample.setTypeSampleId(typeSample.getTypeSampleId());

      subtypeSampleService.save(subtypeSample); 

      // refreshes
      subtypeSample = subtypeSampleService.getSubtypeSampleByIName(iname); 

    } else {

      // TODO check if any data chance, if not don't update.
      subtypeSample.setName(name);
      subtypeSample.setTypeSampleId(typeSample.getTypeSampleId());

      subtypeSampleService.save(subtypeSample); 
    }


    updateUiFields(iname, uiFields); 

  }

}

