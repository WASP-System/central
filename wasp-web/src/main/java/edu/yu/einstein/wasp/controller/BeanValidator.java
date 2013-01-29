package edu.yu.einstein.wasp.controller;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


@Component
public class BeanValidator implements org.springframework.validation.Validator, InitializingBean {
	  
	    private javax.validation.Validator validator;
	 
	    @Override
		public void afterPropertiesSet() throws Exception {
	        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	        validator = validatorFactory.usingContext().getValidator();
	    }
	 
	    @Override
		public boolean supports(Class<?> clazz) {
	        return true;
	    }
	 
	    @Override
		public void validate(Object target, Errors errors) {
		  
	        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
	        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
	            String propertyPath = constraintViolation.getPropertyPath().toString();	      
	            String area=constraintViolation.getRootBeanClass().getSimpleName();
	            
	            area=WordUtils.uncapitalize(area);
	            
	            errors.rejectValue(propertyPath, area+"."+propertyPath+".error");
	        }
	        
	    }
	}