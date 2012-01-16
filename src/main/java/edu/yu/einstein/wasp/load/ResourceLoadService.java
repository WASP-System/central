package edu.yu.einstein.wasp.load;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceLane;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.service.ResourceLaneService;
import edu.yu.einstein.wasp.service.ResourceMetaService;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.TypeResourceService;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname / internalname
 *   - name / label
 *   - uifields (List<UiFields>)
 *   - platform
 *   - meta (List<ResourceMeta>)
 *   - cell (List<ResourceLanes>)
 *
 */

@Transactional
public class ResourceLoadService extends WaspLoadService {

  @Autowired
  private ResourceService resourceService;

  @Autowired
  private ResourceLaneService resourceLaneService;

  @Autowired
  private ResourceMetaService resourceMetaService;

  @Autowired
  private TypeResourceService typeResourceService;

  private String platform; 
  public void setPlatform(String platform) { this.platform = platform; }

  private String resourceType; 
  public void setResourceType(String resourceType) {this.resourceType = resourceType; }

  private List<ResourceLane> cells; 
  public void setCell(List<ResourceLane> cell) {this.cells = cell; }

  private List<ResourceMeta> meta; 
  public void setMeta(List<ResourceMeta> meta) {this.meta = meta; }

  @Override
@Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    TypeResource typeResource = typeResourceService.getTypeResourceByIName(resourceType); 

    Resource resource = resourceService.getResourceByIName(iname); 

    // inserts or update workflow
    if (resource.getResourceId() == null) { 
      resource = new Resource();

      resource.setIName(iname);
      resource.setName(name);
      resource.setPlatform(platform);
      resource.setTypeResourceId(typeResource.getTypeResourceId());

      resourceService.save(resource); 

      // refreshes
      resource = resourceService.getResourceByIName(iname); 

    } else {
      resource.setName(name);
      resource.setPlatform(platform);
      resource.setTypeResourceId(typeResource.getTypeResourceId());

      resourceService.save(resource); 
    }


/*
    // sync metas
    int lastPosition = 0;
    Map<String, ResourceMeta> oldResourceMetas  = new HashMap<String, ResourceMeta>();
    for (ResourceMeta resourceMeta: resource.getResourceMeta()) {
      oldResourceMetas.put(resourceMeta.getK(), resourceMeta);
    } 
    for (ResourceMeta resourceMeta: meta) {

      // incremental position numbers. 
      if ( resourceMeta.getPosition() == 0 ||
           resourceMeta.getPosition() <= lastPosition
        )  {
        resourceMeta.setPosition(lastPosition +1); 
      }
      lastPosition = resourceMeta.getPosition();

      if (oldResourceMetas.containsKey(resourceMeta.getK())) {
        ResourceMeta old = oldResourceMetas.get(resourceMeta.getK());
        if ( old.getV().equals(resourceMeta.getV()) && 
            old.getPosition() == resourceMeta.getPosition()) {
          // the same 
          continue;
        }
        // different
        old.setV(resourceMeta.getV());
        old.setPosition(resourceMeta.getPosition());
        resourceMetaService.save(old);

        oldResourceMetas.remove(old.getK());
        continue; 
      }

      resourceMeta.setResourceId(resource.getResourceId()); 
      resourceMetaService.save(resourceMeta); 
    }

    // delete the left overs
    for (String resourceMetaKey : oldResourceMetas.keySet()) {
      ResourceMeta resourceMeta = oldResourceMetas.get(resourceMetaKey); 
      resourceMetaService.remove(resourceMeta); 
      resourceMetaService.flush(resourceMeta); 
    }


    lastPosition = 0;
    Map<String, ResourceLane> oldResourceLanes  = new HashMap<String, ResourceLane>();
    for (ResourceLane resourceCell: resource.getResourceLane()) {
      oldResourceLanes.put(resourceCell.getIName(), resourceCell);
    } 

    for (ResourceLane resourceCell: cells) {
      if (oldResourceLanes.containsKey(resourceCell.getIName())) {
        ResourceLane old = oldResourceLanes.get(resourceCell.getIName()); 

        if ( old.getIName().equals(resourceCell.getIName())) { 
          // the same 
          continue;
        }

        // different
        old.setName(resourceCell.getName());
        resourceLaneService.save(old);

        oldResourceLanes.remove(old.getIName());
        continue;
      }

      resourceCell.setResourceId(resource.getResourceId()); 
      resourceLaneService.save(resourceCell); 
    }


    // delete the left overs
    for (String resourceLaneKey : oldResourceLanes.keySet()) {
      ResourceLane resourceLane = oldResourceLanes.get(resourceLaneKey); 

      / * DOES NOT REMOVE, id fk available.  TODO: use isactive instead?  
      resourceLaneService.remove(resourceLane); 
      resourceLaneService.flush(resourceLane); 
      * /
    }

*/

    updateUiFields(iname, uiFields); 
  }
}

