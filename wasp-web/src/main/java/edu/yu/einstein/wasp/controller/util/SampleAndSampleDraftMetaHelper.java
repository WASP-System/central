package edu.yu.einstein.wasp.controller.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.util.MetaHelper;

/**
 * Handles SampleMeta and SampleDraftMeta metadata for webforms
 * @author asmclellan
 *
 */
public abstract class SampleAndSampleDraftMetaHelper {
	
	private static <T extends MetaBase> void  validateClass(Class<T> clazz) throws MetadataTypeException {
		if (!clazz.equals(SampleMeta.class) && !clazz.equals(SampleDraftMeta.class)){
			throw new MetadataTypeException("clazz must be of type SampleMeta or sampleDraftMeta");
		}
	}

	/**
	 * Gets an empty meta list based on a template representing a specified {@link SampleSubtype} and synchronizes with the master list for each area representative
	 * of that SampleSubtype
	 * 
	 * @param sampleSubtype
	 * @param sampleMetaList
	 * @return list of normalized sample meta data
	 */
	public static <T extends MetaBase> List<T> templateMetaToSubtypeAndSynchronizeWithMaster(SampleSubtype sampleSubtype, List<T> metaList, Class<T> clazz) throws MetadataTypeException {
		return templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, metaList, null, clazz); 
	}
	
	/**
	 * Gets an empty meta list based on a template representing a specified {@link SampleSubtype} and synchronizes with the master list for each area representative
	 * of that SampleSubtype
	 * 
	 * @param sampleSubtype
	 * @param sampleMetaList
	 * @return list of normalized sample meta data
	 */
	public static <T extends MetaBase> List<T> templateMetaToSubtypeAndSynchronizeWithMaster(SampleSubtype sampleSubtype, Class<T> clazz) throws MetadataTypeException {
		return templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, null, null, clazz); 
	}
	
	/**
	 * Takes a list of meta data, overlays a template representing a specified {@link SampleSubtype} and synchronizes with the master list for each area representative
	 * of that SampleSubtype. Applies visibilityElement mask.
	 * 
	 * @param sampleSubtype
	 * @param sampleMetaList
	 * @return list of normalized sample meta data
	 */
	public static <T extends MetaBase> List<T> templateMetaToSubtypeAndSynchronizeWithMaster(SampleSubtype sampleSubtype, Map<String, MetaAttribute.FormVisibility> visibilityElementMap, Class<T> clazz) throws MetadataTypeException {
		return templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, null, visibilityElementMap, clazz); 
	}
	
	/**
	 * Takes a list of meta data, overlays a template representing a specified {@link SampleSubtype} and synchronizes with the master list for each area representative
	 * of that SampleSubtype. If sampleMetaList is null, then the master list is retrieved for each area instead. Applies visibilityElement mask.
	 * @param sampleSubtype
	 * @param sampleMeta
	 * @param visibilityElementMap
	 * @return list of normalized sample meta data
	 */
	public static <T extends MetaBase> List<T> templateMetaToSubtypeAndSynchronizeWithMaster(SampleSubtype sampleSubtype, List<T> sampleMetaList, Map<String, MetaAttribute.FormVisibility> visibilityElementMap, Class<T> clazz) throws MetadataTypeException {
  		validateClass(clazz);
		MetaHelper sampleMetaHelper = new MetaHelper(clazz); 
  		List<T> normalizedSampleMeta = new ArrayList<T>();
  		for(String area : sampleSubtype.getComponentMetaAreas()){
  			sampleMetaHelper.setArea(area);
  			if (sampleMetaList != null){
  				normalizedSampleMeta.addAll(sampleMetaHelper.syncWithMaster(sampleMetaList, visibilityElementMap));
  			} else {
  				normalizedSampleMeta.addAll(sampleMetaHelper.getMasterList(visibilityElementMap, clazz));
  			}
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
	public static <T extends MetaBase>  List<T> getValidatedMetaFromRequestAndTemplateToSubtype(HttpServletRequest request, SampleSubtype sampleSubtype, BindingResult result, Map<String, MetaAttribute.FormVisibility> visibilityElementMap, Class<T> clazz) throws MetadataTypeException{
		validateClass(clazz);
		List<T> validatedFormMeta = new ArrayList<T>();
		MetaHelperWebapp metaHelper = new MetaHelperWebapp(clazz);
		for (String area : sampleSubtype.getComponentMetaAreas()){
			metaHelper.setArea(area);
			validatedFormMeta.addAll(metaHelper.getFromRequest(request, visibilityElementMap, clazz));
		}
		MetaHelperWebapp.validate(metaHelper.getParentArea(), validatedFormMeta, result);
		return validatedFormMeta;
	}
	
	/**
	 * Gets all metadata filled in on the form from the json string using the areas representative of the supplied {@link SampleSubtype} and validates. Validation errors are added to the result. 
	 * @param request
	 * @param areas
	 * @param result
	 * @return list of sample meta data
	 */
	public static <T extends MetaBase> List<T> getValidatedMetaFromRequestAndTemplateToSubtype(HttpServletRequest request, SampleSubtype sampleSubtype, BindingResult result, Class<T> clazz) throws MetadataTypeException{
		return getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result, null, clazz);
	}

	/**
	 * Gets all metadata filled in on the form from json map using the areas representative of the supplied {@link SampleSubtype} and validates. Validation errors are added to the result. Applies visibilityElementMap.
	 * @param Map<String,String> jsonMap (json converted to Map<String,String>)
	 * @param areas
	 * @param result
	 * @param visibilityElementMap
	 * @return list of sample meta data
	 */
	public static <T extends MetaBase>  List<T> getValidatedMetaFromJsonAndTemplateToSubtype(Map<String,String> jsonMap, SampleSubtype sampleSubtype, BindingResult result, Map<String, MetaAttribute.FormVisibility> visibilityElementMap, Class<T> clazz) throws MetadataTypeException{
		validateClass(clazz);
		List<T> validatedFormMeta = new ArrayList<T>();
		MetaHelperWebapp metaHelper = new MetaHelperWebapp(clazz);
		for (String area : sampleSubtype.getComponentMetaAreas()){
			metaHelper.setArea(area);
			validatedFormMeta.addAll(metaHelper.getFromJson(jsonMap, visibilityElementMap, clazz));
		}
		MetaHelperWebapp.validate(metaHelper.getParentArea(), validatedFormMeta, result);
		return validatedFormMeta;
	}
	
	/**
	 * Gets all metadata filled in on the form from the json map using the areas representative of the supplied {@link SampleSubtype} and validates. Validation errors are added to the result. 
	 * @param Map<String,String> jsonMap (json converted to Map<String,String>)
	 * @param areas
	 * @param result
	 * @return list of sample meta data
	 */
	public static <T extends MetaBase> List<T> getValidatedMetaFromJsonAndTemplateToSubtype(Map<String,String> jsonMap, SampleSubtype sampleSubtype, BindingResult result, Class<T> clazz) throws MetadataTypeException{
		return getValidatedMetaFromJsonAndTemplateToSubtype(jsonMap, sampleSubtype, result, null, clazz);
	}
}
