
package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.SubtypeSampleMeta;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.service.SubtypeSampleMetaService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;


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


  @Override
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
      boolean changed = false;
      if (!subtypeSample.getName().equals(name)){
    	  subtypeSample.setName(name);
    	  changed = true;
      }
      if (subtypeSample.getTypeSampleId().intValue() != typeSample.getTypeSampleId().intValue()){
    	  subtypeSample.setTypeSampleId(typeSample.getTypeSampleId());
    	  changed = true;
      }
      if (changed)
    	  subtypeSampleService.save(subtypeSample); 
    }

    // sync metas
    int lastPosition = 0;
    Map<String, SubtypeSampleMeta> oldSubtypeSampleMetas  = new HashMap<String, SubtypeSampleMeta>();

    if (subtypeSample != null && subtypeSample.getSubtypeSampleMeta() != null) {
      for (SubtypeSampleMeta subtypeSampleMeta: safeList(subtypeSample.getSubtypeSampleMeta())) {
        oldSubtypeSampleMetas.put(subtypeSampleMeta.getK(), subtypeSampleMeta);
      }
    }

    if (meta != null) {
      for (SubtypeSampleMeta subtypeSampleMeta: safeList(meta) ) {
  
        // incremental position numbers.
  
        if ( subtypeSampleMeta.getPosition() == null ||
             subtypeSampleMeta.getPosition().intValue() <= lastPosition
          )  {
          subtypeSampleMeta.setPosition(lastPosition +1);
        }
        lastPosition = subtypeSampleMeta.getPosition().intValue();
        
        if (oldSubtypeSampleMetas.containsKey(subtypeSampleMeta.getK())) {
        	SubtypeSampleMeta old = oldSubtypeSampleMetas.get(subtypeSampleMeta.getK());
            boolean changed = false;
            if (!old.getV().equals(subtypeSampleMeta.getV())){
            	old.setV(subtypeSampleMeta.getV());
            	changed = true;
            }
            if (old.getPosition().intValue() != subtypeSampleMeta.getPosition()){
            	old.setPosition(subtypeSampleMeta.getPosition());
            	changed = true;
            }
            if (changed)
            	subtypeSampleMetaService.save(old);

            oldSubtypeSampleMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
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

