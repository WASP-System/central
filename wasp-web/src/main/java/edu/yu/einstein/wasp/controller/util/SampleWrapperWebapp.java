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
import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;


public class SampleWrapperWebapp extends SampleWrapper{

	
	public SampleWrapperWebapp(Sample sample) {
		super(sample);
	}
	

	/**
	 * Gets an empty meta list based on a template representing a specified {@link SampleSubtype} and synchronizes with the master list for each area representative
	 * of that SampleSubtype
	 * 
	 * @param sampleSubtype
	 * @param sampleMetaList
	 * @return list of normalized sample meta data
	 */
	public static List<SampleMeta> templateMetaToSubtypeAndSynchronizeWithMaster(SampleSubtype sampleSubtype, List<SampleMeta> sampleMetaList) {
		return templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, sampleMetaList, null); 
	}
	
	/**
	 * Gets an empty meta list based on a template representing a specified {@link SampleSubtype} and synchronizes with the master list for each area representative
	 * of that SampleSubtype
	 * 
	 * @param sampleSubtype
	 * @param sampleMetaList
	 * @return list of normalized sample meta data
	 */
	public static List<SampleMeta> templateMetaToSubtypeAndSynchronizeWithMaster(SampleSubtype sampleSubtype) {
		return templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, null, null); 
	}
	
	/**
	 * Takes a list of meta data, overlays a template representing a specified {@link SampleSubtype} and synchronizes with the master list for each area representative
	 * of that SampleSubtype. Applies visibilityElement mask.
	 * 
	 * @param sampleSubtype
	 * @param sampleMetaList
	 * @return list of normalized sample meta data
	 */
	public static List<SampleMeta> templateMetaToSubtypeAndSynchronizeWithMaster(SampleSubtype sampleSubtype, Map<String, MetaAttribute.FormVisibility> visibilityElementMap) {
		return templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, null, visibilityElementMap); 
	}
	
	/**
	 * Takes a list of meta data, overlays a template representing a specified {@link SampleSubtype} and synchronizes with the master list for each area representative
	 * of that SampleSubtype. If sampleMetaList is null, then the master list is retrieved for each area instead. Applies visibilityElement mask.
	 * @param sampleSubtype
	 * @param sampleMeta
	 * @param visibilityElementMap
	 * @return list of normalized sample meta data
	 */
	public static List<SampleMeta> templateMetaToSubtypeAndSynchronizeWithMaster(SampleSubtype sampleSubtype, List<SampleMeta> sampleMetaList, Map<String, MetaAttribute.FormVisibility> visibilityElementMap) {
  		List<SampleMeta> normalizedSampleMeta = null;
  		try {
  			normalizedSampleMeta = SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, sampleMetaList, visibilityElementMap, SampleMeta.class);
  		} catch(MetadataTypeException e){
  			logger.warn("caught unexpected MetadataTypeException with message: "+ e.getMessage());
  		}
  		return normalizedSampleMeta;
	}
	
	/**
	 * Gets all metadata filled in on the form from the http request using the areas representative of the supplied {@link SampleSubtype} and validates. Validation errors are added to the result. Applies visibilityElementMap.
	 * @param request
	 * @param areas
	 * @param result
	 * @param visibilityElementMap
	 * @return list of sample meta data
	 */
	public static List<SampleMeta> getValidatedMetaFromRequestAndTemplateToSubtype(HttpServletRequest request, SampleSubtype sampleSubtype, BindingResult result, Map<String, MetaAttribute.FormVisibility> visibilityElementMap){
		List<SampleMeta> validatedFormMeta = new ArrayList<SampleMeta>();
		try {
			validatedFormMeta = SampleAndSampleDraftMetaHelper.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result, SampleMeta.class);
  		} catch(MetadataTypeException e){
  			logger.warn("caught unexpected MetadataTypeException with message: "+ e.getMessage());
  		}
		return validatedFormMeta;
	}
	
	/**
	 * Gets all metadata filled in on the form from the http request using the areas representative of the supplied {@link SampleSubtype} and validates. Validation errors are added to the result. 
	 * @param request
	 * @param areas
	 * @param result
	 * @return list of sample meta data
	 */
	public static List<SampleMeta> getValidatedMetaFromRequestAndTemplateToSubtype(HttpServletRequest request, SampleSubtype sampleSubtype, BindingResult result){
		return getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result, null);
	}

}
