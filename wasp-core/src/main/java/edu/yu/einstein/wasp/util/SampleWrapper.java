package edu.yu.einstein.wasp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;

/**
 * This wrapper implements the BioMolecule interface and helps manage information related to a biological sample,
 * specifically a DNA sample or Library sample. It hides complexity such as exactly which Sample entities and 
 * their meta are used to gather information about the biological sample. 
 * @author ASMcLellan
 *
 */
public class SampleWrapper implements BioMoleculeI{

	protected SampleWrapper parent = null; // e.g. if this sample is a library then the parent might be a source DNA sample
	protected Sample sample = null; // the sample object wrapped by this class
	
	protected static final Logger	logger	= Logger.getLogger(SampleWrapper.class);
		
	/**
	 * Constructor: requires the target sample object and a sampleSourceDao implementation
	 * @param sample
	 * @param sampleSourceDao
	 */
	public SampleWrapper(Sample sample, SampleSourceDao sampleSourceDao){
		this.sample = sample;
		Sample parentSample = sampleSourceDao.getParentSampleByDerivedSampleId(sample.getSampleId());
		if (parentSample.getSampleId() != null){
  			parent = new SampleWrapper(parentSample, sampleSourceDao);
  		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SampleMeta> getAllSampleMeta(){
		List<SampleMeta> sm = new ArrayList<SampleMeta>();
		sm.addAll(sample.getSampleMeta());
		if (parent != null)
			sm.addAll(parent.getAllSampleMeta());
		return sm;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sample getSampleObject() {
		return sample;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getAllSampleMetaAreas(){
		Set<String> areas = new HashSet<String>();
		for(SampleMeta sm : this.getAllSampleMeta()){
			areas.add(MetaHelper.getAreaFromMeta(sm));
		}
		return areas;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SampleMeta> getMetaTemplatedToSampleSybtype(SampleSubtype sampleSubtype) {  
		List<SampleMeta> templateSampleMeta = new ArrayList<SampleMeta>();
  		List<String> sampleSubtypeComponentAreas = sampleSubtype.getComponentMetaAreas();
  		for(String area : sampleSubtypeComponentAreas){
  			templateSampleMeta.addAll(MetaHelper.getMetaSubsetByArea(area, this.getAllSampleMeta()));
  		}
  		return templateSampleMeta;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateMetaToListAndSave(List<SampleMeta> inputMetaList, SampleMetaDao sampleMetaDao) throws MetadataException{
		Map<Integer, Map<String, SampleMeta> > indexedAllInheritedMetaBySampleId = new HashMap<Integer, Map<String, SampleMeta> >();
		for(SampleMeta sm :  this.getAllSampleMeta()){
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
