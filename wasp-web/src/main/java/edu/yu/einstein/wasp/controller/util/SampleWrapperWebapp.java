package edu.yu.einstein.wasp.controller.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;


public class SampleWrapperWebapp extends SampleWrapper{

	
	public SampleWrapperWebapp(Sample sample, SampleSourceDao sampleSourceDao) {
		super(sample, sampleSourceDao);
	}
	
	/**
	 * Retrieves a list of ALL meta data associated with this biological sample using only the areas comprising the provided {@inheritDoc SampleSubtype}
	 * and also found in UiFields
	 * @param sampleSubtype
	 * @return list of sample meta data
	 */
	public List<SampleMeta> getMetaTemplatedToSampleSybtypeAndSynchronizedToMaster(SampleSubtype sampleSubtype) {
		return getMetaTemplatedToSampleSybtypeAndSynchronizedToMaster(sampleSubtype, null); 
	}

	/**
	 * Retrieves a list of ALL meta data associated with this biological sample using only the areas comprising the provided {@inheritDoc SampleSubtype}
	 * and also found in UiFields. Applies the provided visibilityElementmap to control metadata visibility on forms
	 * @param sampleSubtype
	 * @return list of sample meta data
	 */
	public List<SampleMeta> getMetaTemplatedToSampleSybtypeAndSynchronizedToMaster(SampleSubtype sampleSubtype, Map<String, MetaAttribute.FormVisibility> visibilityElementMap) {
  		MetaHelper sampleMetaHelper = new MetaHelper(SampleMeta.class); 
  		List<SampleMeta> normalizedSampleMeta = new ArrayList<SampleMeta>();
  		List<String> sampleSubtypeComponentAreas = sampleSubtype.getComponentMetaAreas();
  		for(String area : sampleSubtypeComponentAreas){
  			sampleMetaHelper.setArea(area);
  			normalizedSampleMeta.addAll(sampleMetaHelper.syncWithMaster(this.getAllSampleMeta(), visibilityElementMap));
  		}
  		return normalizedSampleMeta;
	}
	
	public List<SampleMeta> getValidatedMetaFromRequest(HttpServletRequest request, BindingResult result, Map<String, MetaAttribute.FormVisibility> visibilityElementMap){
		List<SampleMeta> validatedFormMeta = new ArrayList<SampleMeta>();
		MetaHelperWebapp metaHelper = new MetaHelperWebapp(SampleMeta.class);
		for (String area : this.getAllSampleMetaAreas()){
			metaHelper.setArea(area);
			validatedFormMeta.addAll(metaHelper.getFromRequest(request, visibilityElementMap, SampleMeta.class));
		}
		MetaHelperWebapp.validate(metaHelper.getParentArea(), validatedFormMeta, result);
			
		
		return validatedFormMeta;
	}
	
	public List<SampleMeta> getValidatedMetaFromRequest(HttpServletRequest request, BindingResult result){
		return getValidatedMetaFromRequest(request, result, null);
	}

}
