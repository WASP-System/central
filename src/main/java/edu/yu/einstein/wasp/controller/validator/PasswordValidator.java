package edu.yu.einstein.wasp.controller.validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.service.PasswordService;


@Component
public class PasswordValidator{
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PasswordService passwordService;
	
	
	public void validate(BindingResult result, String password1, String password2, MetaAttribute.Area area, String passwordFieldName) {
				    
	    // validate password
	    if (! result.hasFieldErrors(passwordFieldName)){
	    	String areaName = area.name();
			Errors errors = new BindException(result.getTarget(), areaName);
	    	if (password1 == null || password1.isEmpty()){
	    		errors.rejectValue(passwordFieldName, areaName+".password.error", areaName+".password.error (no message has been defined for this property)");
	    	}
	    	logger.debug("ANDY: "+password1);
	    	if (passwordService==null){
	    		logger.debug("ANDY: password service is null");
	    	}
	    	else if (! passwordService.validatePassword(password1) ){ 
		    	errors.rejectValue(passwordFieldName, areaName+".password_invalid.error", areaName+".password_invalid.error (no message has been defined for this property)");
		    }
		    else if (! passwordService.matchPassword(password1, password2) ){
		    	errors.rejectValue(passwordFieldName, areaName+".password_mismatch.error", areaName+".password_mismatch.error (no message has been defined for this property)");
		    }
	    	result.addAllErrors(errors);
	    }
	}
	
}
