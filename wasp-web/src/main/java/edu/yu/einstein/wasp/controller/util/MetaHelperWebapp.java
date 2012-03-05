package edu.yu.einstein.wasp.controller.util;


import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.controller.validator.MetaValidatorImpl;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.util.MetaHelper;


/**
 * Contains utility methods for manipulating
 * meta and MetaAttribute objects
 * @author Sasha Levchuk
 *
 */
public class MetaHelperWebapp extends MetaHelper {

	
	/**
	 * Constructor
	 * @param area
	 * @param parentArea
	 * @param clazz
	 * @param session
	 */
	public <T extends MetaBase> MetaHelperWebapp(String area, String parentArea, Class<T> clazz, HttpSession session) {
		super(area, parentArea, clazz, (Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
	}
	
	/**
	 * Constructor
	 * @param area
	 * @param clazz
	 * @param session
	 */
	public <T extends MetaBase> MetaHelperWebapp(String area, Class<T> clazz,HttpSession session) {
		this(area,area,clazz,session);
	}


	/**
	 *  Generates Master List and pulls in from values from request.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, Class<T> clazz) {
		return getFromRequest(request, (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}

	/**
	 * Generates Master List and pulls in from values from request.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param visibility 
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {
		List<T> list = getMasterList(visibility, clazz);

		Map params = request.getParameterMap();
		for (T obj: list) {
			String requestKey = parentArea + "Meta" + "_" + obj.getK();
			logger.debug("getFromRequest(): looking for '" + requestKey + "' in request parameters");
			if (! params.containsKey(requestKey)) { continue; }
			try {
				obj.setV(((String[])params.get(requestKey))[0]);
			} catch (Throwable e) {
				throw new IllegalStateException("cannot merge attributes ",e);
			}
		}

		this.lastList =  list; 

		return list;
	}
	
	/**
	 * Generates Master List and pulls in from values from request (Json form)
	 * @param request
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJsonForm(HttpServletRequest request, Class<T> clazz) {
		return getFromJsonForm(request, (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}

	/**
	 * Generates Master List and pulls in from values from request (Json form)
	 * @param request
	 * @param visibility
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJsonForm(HttpServletRequest request, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {
		List<T> list = getMasterList(visibility, clazz);

		Map params = request.getParameterMap();

		for (T obj: list) {
			String requestKey = obj.getK();
			if (! params.containsKey(requestKey)) { continue; }
			try {
				obj.setV(((String[])params.get(requestKey))[0]);
			} catch (Throwable e) {
				throw new IllegalStateException("cannot merge attributes ",e);
			}
		}

		this.lastList =  list; 

		return list;
	}


	/**
	 * Get a {@link MetaValidatorImpl} instance 
	 * Validates any metadata with constraints in the supplied list
	 * @param list
	 * @return
	 */
	public final MetaValidator getMetaValidator(List<? extends MetaBase> list) {
		return getMetaValidator(list, MetaValidatorImpl.class);
	}
	
	/**
	 * Get a {@link MetaValidator} derived instance of type specified by 'metaValidatorClazz'
	 * Validates any metadata with constraints in the supplied list
	 * @param list
	 * @param metaValidatorClazz
	 * @return
	 */
	public final <T extends MetaValidator> T getMetaValidator(List<? extends MetaBase> list, Class <T>metaValidatorClazz) {
		if (list == null) return null;
		T validator;
		try {
			validator = metaValidatorClazz.newInstance();
		} catch (Exception e) {
			return null;
		}
		return validator;
	}
	
	/**
	 * Get a {@link MetaValidatorImpl} instance 
	 * Validates any metadata with constraints in the last generated metadata list
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @return
	 */
	public final MetaValidator getMetaValidator() {
		return getMetaValidator(this.lastList);
	}
	
	/**
	 * Get a {@link MetaValidator} derived instance of type specified by 'metaValidatorClazz'
	 * Validates any metadata with constraints in the last generated metadata list
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param metaValidatorClazz
	 * @return
	 */
	public final <T extends MetaValidator> T getMetaValidator(Class<T> metaValidatorClazz) {
		return getMetaValidator(this.lastList, metaValidatorClazz);
	}
	
	/**
	 * Validates any metadata with constraints in the supplied list using the supplied validator object
	 * @param validator
	 * @param list
	 * @param result
	 */
	public void validate(MetaValidator validator, List<? extends MetaBase> list, BindingResult result) {
		validator.validate(list, result, area, parentArea);
	}

	/**
	 * Validates any metadata with constraints in the last generated metadata list using the supplied validator object
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param validator
	 * @param result
	 */
	public void validate(MetaValidator validator, BindingResult result) {
		validator.validate(this.lastList, result, area, parentArea);
	}

	/**
	 * Validates any metadata with constraints in the supplied list using {@link MetaValidatorImpl}
	 * @param list
	 * @param result
	 */
	public void validate(List<? extends MetaBase> list, BindingResult result) {
		getMetaValidator(list).validate(list, result, area, parentArea);
	}
	
	/**
	 * Validates any metadata with constraints in the last generated metadata list using {@link MetaValidatorImpl}
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param result
	 */
	public void validate(BindingResult result) {
		getMetaValidator().validate(this.lastList, result, area, parentArea);
	}

	/**
	 * Validates any metadata with constraints in the last generated metadata list using a {@link MetaValidator} derived instance of type specified by 'metaValidatorClazz'
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param metaValidatorClazz
	 * @param result
	 */
	public <T extends MetaValidator> void validate(Class<T> metaValidatorClazz, BindingResult result) {
		getMetaValidator(this.lastList, metaValidatorClazz).validate(this.lastList, result, area, parentArea);
	}
}
