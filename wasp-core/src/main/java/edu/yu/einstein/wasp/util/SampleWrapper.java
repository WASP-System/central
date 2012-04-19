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
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * This wrapper implements the BioMolecule interface and helps manage information related to a biological sample,
 * specifically a DNA sample or Library sample. It hides complexity such as exactly which Sample entities and 
 * their meta are used to gather information about the biological sample. 
 * @author ASMcLellan
 *
 */
public class SampleWrapper implements BioMoleculeWrapperI{

	protected SampleWrapper parent = null; // e.g. if this sample is a library then the parent might be a source DNA sample
	protected Sample sample = null; // the sample object wrapped by this class
	
	protected static final Logger logger = Logger.getLogger(SampleWrapper.class);
		
	/**
	 * Constructor: requires the target sample object and a sampleSourceDao implementation
	 * @param sample
	 * @param sampleSourceDao
	 */
	public SampleWrapper(Sample sample, SampleSourceDao sampleSourceDao){
		this.sample = sample;
		if (sample.getSampleId() != null){
			Sample parentSample = sampleSourceDao.getParentSampleByDerivedSampleId(sample.getSampleId());
			if (parentSample.getSampleId() != null){
	  			parent = new SampleWrapper(parentSample, sampleSourceDao);
	  		}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SampleMeta> getAllSampleMeta(){
		List<SampleMeta> sm = new ArrayList<SampleMeta>();
		if (sample.getSampleMeta() != null && !sample.getSampleMeta().isEmpty())
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
	public SampleWrapper getParentWrapper() {
		return parent;
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
	public void setParent(Sample parentSample, SampleSourceDao sampleSourceDao) throws SampleParentChildException{
		if (parent != null && 
				parent.getSampleObject().getSampleId() != null && 
				sampleSourceDao.getParentSampleByDerivedSampleId(sample.getSampleId()).getSampleId() != null && 
				sampleSourceDao.getParentSampleByDerivedSampleId(sample.getSampleId()).getSampleId().intValue() == parent.getSampleObject().getSampleId().intValue()){
			throw new SampleParentChildException("parentSample already assigned as parent of sample managed by SampleWrapper");
		}
		parent = new SampleWrapper(parentSample, sampleSourceDao);
	}
	
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SampleMeta> updateMetaToList(List<SampleMeta> inputMetaList, SampleMetaDao sampleMetaDao){
		return updateMetaToList(inputMetaList, sampleMetaDao, true);
	}
	
	/**
	 * internal method to do meta updating to supplied list.
	 * @param inputMetaList
	 * @param sampleMetaDao
	 * @param isPrimaryCalledInstance - a boolean to tell if method has been called internally or from public method 
	 * @return
	 */
	private List<SampleMeta> updateMetaToList(List<SampleMeta> inputMetaList, SampleMetaDao sampleMetaDao, boolean isPrimaryCalledInstance){
		List<SampleMeta> returnMetaList = new ArrayList<SampleMeta>();
		List<SampleMeta> currentlyProcessedMetaList = new ArrayList<SampleMeta>();
		// recursively work forward through ancestry and add meta to list
		// deplete inputMetaList as meta used (use once)
		if (parent != null)
			returnMetaList.addAll(parent.updateMetaToList(inputMetaList, sampleMetaDao, false));
		 
		Map<String, SampleMeta> indexedCurrentSampleMeta = new HashMap<String, SampleMeta>();
		if (sample.getSampleMeta() != null){
			for(SampleMeta sm :  sample.getSampleMeta()){
				indexedCurrentSampleMeta.put(sm.getK(), sm);
				if (sm.getSampleId() == null && sample.getSampleId() != null){
					sm.setSampleId(sample.getSampleId());
					sampleMetaDao.save(sm);
				}
			}
		}
		for (SampleMeta inputMeta: inputMetaList){
			if (indexedCurrentSampleMeta.containsKey(inputMeta.getK())){
				SampleMeta currentSampleMeta = indexedCurrentSampleMeta.get(inputMeta.getK());
				// there is an index in the current sample's Meta matching that of the current input meta value
				if (inputMeta.getSampleId() == null || inputMeta.getSampleId().intValue() == sample.getSampleId().intValue() ){
					// either no sampleId is set or the sampleId set matches the current meta sampleId
					if (! inputMeta.getV().equals(currentSampleMeta.getV())){
						// value changed so update
						currentSampleMeta.setV(inputMeta.getV());
						if (sample.getSampleId() != null){
							//existing sample so save
							sampleMetaDao.save(currentSampleMeta);
						}
					} else {
						// value unchanged so do nothing 
					}
					currentlyProcessedMetaList.add(currentSampleMeta);
				}
			} else if (inputMeta.getSampleId() != null && sample.getSampleId() != null && inputMeta.getSampleId().intValue() == sample.getSampleId().intValue() ){
				// new meta for this sample 
				sampleMetaDao.save(inputMeta);
				currentlyProcessedMetaList.add(inputMeta);
				indexedCurrentSampleMeta.put(inputMeta.getK(), inputMeta);
			}
		}
		for (SampleMeta currentlyProcessedMeta: currentlyProcessedMetaList)
			inputMetaList.remove(currentlyProcessedMeta); // we're done with this
			
	
		if (isPrimaryCalledInstance){
			for (SampleMeta inputMeta: inputMetaList){
				// new meta should be all that remains if anything. 
				if (inputMeta.getSampleId() == null){
					if (sample.getSampleId() != null){
						inputMeta.setSampleId(sample.getSampleId());
						sampleMetaDao.save(inputMeta);
					}
					currentlyProcessedMetaList.add(inputMeta);
					indexedCurrentSampleMeta.put(inputMeta.getK(), inputMeta);
				} else {
					logger.warn("Ignoring input metadata '" + inputMeta.getK() + "'(sampleId='"+inputMeta.getSampleId()+"')  as not compatible with managed sample (sampleId='"+sample.getSampleId()+"') or ancestors!!");
				}
			}
			if (sample.getSampleId() == null){
				List<SampleMeta> newSampleMetaList = new ArrayList<SampleMeta>();
				newSampleMetaList.addAll(indexedCurrentSampleMeta.values());
				sample.setSampleMeta(newSampleMetaList);
			}
		}
		returnMetaList.addAll(indexedCurrentSampleMeta.values());
		return returnMetaList;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveAll(SampleService sampleService, SampleSourceDao sampleSourceDao){
		if (parent != null)
			parent.saveAll(sampleService, sampleSourceDao); // Propagate up just in case
		// persist sample if not yet persisted
		if (sample.getSampleId() == null){
			sampleService.saveSampleWithAssociatedMeta(sample);
		}
		
		// save sample -> parent relationship if it doesn't exist
		if (parent != null && (sampleSourceDao.getParentSampleByDerivedSampleId(sample.getSampleId()).getSampleId() == null ||
			sampleSourceDao.getParentSampleByDerivedSampleId(sample.getSampleId()).getSampleId().intValue() != parent.getSampleObject().getSampleId().intValue()) ){
			SampleSource sampleSource = new SampleSource();
			sampleSource.setSampleId(sample.getSampleId());
			sampleSource.setSourceSampleId(parent.getSampleObject().getSampleId());
			sampleSourceDao.save(sampleSource);
		}
	}
}
