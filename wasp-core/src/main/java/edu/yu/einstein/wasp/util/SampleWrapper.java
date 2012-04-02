package edu.yu.einstein.wasp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;



public class SampleWrapper implements BioMoleculeI{

	private SampleWrapper parent = null;
	private Sample sample = null;
	
	
	@Autowired
	private SampleSourceDao sampleSourceDao;
	
	@Autowired
	private SampleMetaDao sampleMetaDao;
	
	public SampleWrapper(Sample sample){
		this.sample = sample;
		Sample parentSample = sampleSourceDao.getParentSampleByDerivedSampleId(sample.getSampleId());
  		if (parentSample.getSampleId() != null){
  			parent = new SampleWrapper(parentSample);
  		}
	}

	public List<SampleMeta> getAllInheritedMeta(){
		List<SampleMeta> sm = new ArrayList<SampleMeta>();
		sm.addAll(sample.getSampleMeta());
		sm.addAll(parent.getAllInheritedMeta());
		return sm;
	}

	@Override
	public Sample getSampleObject() {
		return sample;
	}

	@Override
	public Set<String> getMetaAreas() {
		Set<String> areas = new HashSet<String>();
		for(SampleMeta sm : sample.getSampleMeta()){
			areas.add(MetaHelper.getAreaFromMeta(sm));
		}
		return areas;
	}

	@Override
	public List<SampleMeta> getMetaTemplatedToSampleSybtype(SampleSubtype sampleSubtype) {  
		return getMetaTemplatedToSampleSybtype(sampleSubtype, null);
	}

	public List<SampleMeta> getMetaTemplatedToSampleSybtype(SampleSubtype sampleSubtype, Map<String, MetaAttribute.FormVisibility> visibilityElementMap) {
  		MetaHelper sampleMetaHelper = new MetaHelper(SampleMeta.class); 
  		List<SampleMeta> normalizedSampleMeta = new ArrayList<SampleMeta>();
  		List<String> sampleSubtypeComponentAreas = sampleSubtype.getComponentMetaAreas();
  		for(String area : sampleSubtypeComponentAreas){
  			sampleMetaHelper.setArea(area);
  			normalizedSampleMeta.addAll(sampleMetaHelper.syncWithMaster(this.getAllInheritedMeta(), visibilityElementMap));
  		}
  		return normalizedSampleMeta;
	}
	
	public void updateMetaToListAndSave(List<SampleMeta> inputMetaList) throws MetadataException{
		Map<Integer, Map<String, SampleMeta> > indexedAllInheritedMetaBySampleId = new HashMap<Integer, Map<String, SampleMeta> >();
		for(SampleMeta sm :  this.getAllInheritedMeta()){
			Map<String, SampleMeta> indexedAllInheritedMeta = new HashMap<String, SampleMeta>();
			indexedAllInheritedMeta.put(sm.getK(), sm);
			indexedAllInheritedMetaBySampleId.put(sm.getSampleId(), indexedAllInheritedMeta);
		}
		for(SampleMeta inputMeta : inputMetaList){
			if (inputMeta.getSampleId() != null && indexedAllInheritedMetaBySampleId.containsKey(inputMeta.getSampleId())){
				// update existing meta
				Map <String, SampleMeta> metaToChangeMap = indexedAllInheritedMetaBySampleId.get(inputMeta.getSampleId());
				if (metaToChangeMap.containsKey(inputMeta.getK()) && !metaToChangeMap.get(inputMeta.getK()).getV().equals(inputMeta.getV())){
					SampleMeta metaToUpdate = metaToChangeMap.get(inputMeta.getK());
					metaToUpdate.setV(inputMeta.getV()); // should merge automatically as is entity managed
				}
			} else{
				// new meta 
				if (inputMeta.getSampleId() == null){
					inputMeta.setSampleId(sample.getSampleId());
					sampleMetaDao.persist(inputMeta);
				} else{
					// metadata has a sampleId not associated with this wrapper
					String validSampleIdString = "";
					for (Integer sampleId: indexedAllInheritedMetaBySampleId.keySet()){
						if (validSampleIdString.length() > 0) 
							validSampleIdString += ", ";
						validSampleIdString += String.valueOf(sampleId);
					}
					throw new MetadataException("Metadata is associated with a sample with id="+String.valueOf(inputMeta.getSampleId())+" that is not in the managed list of ids="+validSampleIdString);
				}
				
			}
		}
	}
	
}
