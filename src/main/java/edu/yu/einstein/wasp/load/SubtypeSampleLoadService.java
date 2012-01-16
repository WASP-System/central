
package edu.yu.einstein.wasp.load;

import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.SubtypeSampleMetaService;
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

  @Autowired
  private SubtypeSampleMetaService subtypeSampleMetaService;

  private String sampleType; 
  public void setSampleType(String sampleType) {this.sampleType = sampleType; }

  public SubtypeSampleLoadService (){};

  private List<SubtypeSampleMeta> meta;
  public void setMeta(List<SubtypeSampleMeta> subtypeSampleMeta) {this.meta = subtypeSampleMeta; }


  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    TypeSample typeSample = typeSampleService.getTypeSampleByIName(sampleType); 

    SubtypeSample subtypeSample = subtypeSampleService.getSubtypeSampleByIName(iname); 

    // inserts or update subtype
    if (subtypeSample.getSubtypeSampleId() == null) {
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

    // sync metas
    int lastPosition = 0;
    Map<String, SubtypeSampleMeta> oldSubtypeSampleMetas  = new HashMap<String, SubtypeSampleMeta>();

    if (subtypeSample != null && subtypeSample.getSubtypeSampleMeta() != null) {
      for (SubtypeSampleMeta subtypeSampleMeta: subtypeSample.getSubtypeSampleMeta()) {
        oldSubtypeSampleMetas.put(subtypeSampleMeta.getK(), subtypeSampleMeta);
      }
    }

    if (meta != null) {
      for (SubtypeSampleMeta subtypeSampleMeta: meta) {
  
        // incremental position numbers.
  
        if ( subtypeSampleMeta.getPosition() == null ||
             subtypeSampleMeta.getPosition().intValue() <= lastPosition
          )  {
          subtypeSampleMeta.setPosition(lastPosition +1);
        }
        lastPosition = subtypeSampleMeta.getPosition().intValue();
  
        if (oldSubtypeSampleMetas.containsKey(subtypeSampleMeta.getK())) {
          SubtypeSampleMeta old = oldSubtypeSampleMetas.get(subtypeSampleMeta.getK());
          if ( old.getV().equals(subtypeSampleMeta.getV()) &&
              old.getPosition().intValue() == subtypeSampleMeta.getPosition().intValue()) {
              // the same
              continue;
          }
          // different
          old.setV(subtypeSampleMeta.getV());
          old.setPosition(subtypeSampleMeta.getPosition());
          oldSubtypeSampleMetas.remove(old.getK());
          continue;
        }

        subtypeSampleMeta.setSubtypeSampleId(subtypeSample.getSubtypeSampleId());
        subtypeSampleMeta.setPosition(1);
        subtypeSampleMetaService.save(subtypeSampleMeta);
      }
    }

    // delete the left overs
    for (String subtypeSampleMetaKey : oldSubtypeSampleMetas.keySet()) {
      SubtypeSampleMeta subtypeSampleMeta = oldSubtypeSampleMetas.get(subtypeSampleMetaKey);
      subtypeSampleMetaService.remove(subtypeSampleMeta);
      subtypeSampleMetaService.flush(subtypeSampleMeta);
    }

    updateUiFields(iname, uiFields); 

  }

}

