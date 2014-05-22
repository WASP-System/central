package edu.yu.einstein.wasp.controller.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import edu.yu.einstein.wasp.exception.MetaRangeException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;


/**
 * Implements validation of MetaBase children based on "constraint" properties read from uifield table
 * 
 * @Author Sasha Levchuk / asmclellan
 */

public class MetaValidatorImpl implements MetaValidator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected List<Constraint> allowableConstraints = new ArrayList<Constraint>(Arrays.asList(Constraint.NotEmpty, Constraint.RegExp));
	
	@Override
	public void validate(List<? extends MetaBase> list, BindingResult result, String area) {
		Errors errors=new BindException(result.getTarget(), area); 
	
		for(int i=0;i<list.size();i++) {
			MetaBase meta=list.get(i);
			if (meta.getProperty().getFormVisibility().equals(MetaAttribute.FormVisibility.ignore)) continue;
			String constraint = meta.getProperty().getConstraint();
			String regexp = "";
			if (constraint != null && constraint.startsWith(Constraint.RegExp.name()+":")) {
				regexp=constraint.substring(Constraint.RegExp.name().length()+1); 
				constraint = Constraint.RegExp.name();
			}
			MetaAttribute.MetaType metaType = meta.getProperty().getMetaType();
			String range = meta.getProperty().getRange();
			
			String errorFieldName = area+"Meta["+i+"].k";
			String errorMessageKey = meta.getK() + ".error";
			logger.debug("errorMessageKey="+errorMessageKey);
			String defaultMessage = errorMessageKey+" (no message has been defined for this property)";
			if (constraint != null){
				if (! allowableConstraints.contains(Constraint.valueOf(constraint)) ){
					throw new IllegalStateException("Unknown constraint "+constraint+"|"+meta);
				}
				
				if (constraint.equals(Constraint.RegExp.name())) {
					//TODO: optimize for speed (pre-compile) if used outside of admin pages
					Pattern p = Pattern.compile(regexp);
					Matcher m = p.matcher(meta.getV());
					boolean b = m.matches();
					if (!b) {
						errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
						continue;
					}
	
				} else if (constraint.equals(Constraint.NotEmpty.name())){
					if (meta.getV()==null || meta.getV().isEmpty()) {
						errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
						continue;
					}
				}
				else{
					// constraint not processed in this class. Do nothing
				}
			}
			if (!meta.getV().isEmpty()){
				if (metaType == MetaAttribute.MetaType.INTEGER || metaType == MetaAttribute.MetaType.NUMBER){
					// validate 
					if (metaType == MetaAttribute.MetaType.INTEGER){
						try {
							Integer.valueOf(meta.getV());
						} catch(NumberFormatException e){
							errors.rejectValue(errorFieldName, "metadata.metaType.error", "metadata.metaType.error (no message has been defined for this property)");
							continue;
						}
					} else{
						try {
							Float.valueOf(meta.getV());
						} catch(NumberFormatException e){
							errors.rejectValue(errorFieldName, "metadata.metaType.error", "metadata.metaType.error (no message has been defined for this property)");
							continue;
						}
					}
				}
				if (range != null && !range.isEmpty()){
					float rangeMax = 0;
					float rangeMin = 0;
					float value = 0;
					String[] rangeSplit = range.split(":");
					try{
						if (rangeSplit.length == 1){
							rangeMax = Float.parseFloat(rangeSplit[0]);
						} else if (rangeSplit.length > 1){
							rangeMin = Float.parseFloat(rangeSplit[0]);
							rangeMax = Float.parseFloat(rangeSplit[1]);
						}
					} catch(NumberFormatException e){
						throw new MetaRangeException("Cannot parse range values for "+meta.getK()+".range to float", e);
					}
					try {
						value = Float.parseFloat(meta.getV());
					} catch(NumberFormatException e){
						throw new MetaRangeException("Cannot parse value ("+meta.getV()+") for "+meta.getK()+" to float", e);
					}
					
					if ((metaType == MetaAttribute.MetaType.INTEGER || metaType == MetaAttribute.MetaType.NUMBER) && value > rangeMax){
						errors.rejectValue(errorFieldName, "metadata.rangeMax.error", "metadata.rangeMax.error (no message has been defined for this property)");
						continue;
					} else if (metaType == MetaAttribute.MetaType.STRING && meta.getV().length() > rangeMax){
						errors.rejectValue(errorFieldName, "metadata.lengthMax.error",  "metadata.lengthMax.error (no message has been defined for this property)");
						continue;
					} else if ((metaType == MetaAttribute.MetaType.INTEGER || metaType == MetaAttribute.MetaType.NUMBER) && value < rangeMin){
						errors.rejectValue(errorFieldName, "metadata.rangeMin.error",  "metadata.rangeMin.error (no message has been defined for this property)");
						continue;
					} else if (metaType == MetaAttribute.MetaType.STRING && meta.getV().length() < rangeMin){
						errors.rejectValue(errorFieldName, "metadata.lengthMin.error",  "metadata.lengthMin.error (no message has been defined for this property)");
						continue;
					}
				}
			}
		}
		result.addAllErrors(errors);
	}
}
