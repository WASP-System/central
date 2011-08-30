package edu.yu.einstein.wasp.controller.validator;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.service.PasswordService;
import edu.yu.einstein.wasp.service.impl.PasswordServiceImpl;


public class PasswordValidator{
	
    private PasswordService passwordService = new PasswordServiceImpl();
	
	public void validate(BindingResult result, String password1, String password2, MetaAttribute.Area area) {
				    
	    // validate password
	    if (! result.hasFieldErrors("password")){
	    	String areaName = area.name();
			Errors errors = new BindException(result.getTarget(), areaName);
	    	if (password1 == null || password1.isEmpty()){
	    		errors.rejectValue("password", areaName+".password.error", areaName+".password.error (no message has been defined for this property)");
	    	}
	    	else if (! passwordService.validatePassword(password1) ){ 
		    	errors.rejectValue("password", areaName+".password_invalid.error", areaName+".password_invalid.error (no message has been defined for this property)");
		    }
		    else if (! passwordService.matchPassword(password1, password2) ){
		    	errors.rejectValue("password", areaName+".password_mismatch.error", areaName+".password_mismatch.error (no message has been defined for this property)");
		    }
	    	result.addAllErrors(errors);
	    }
	}
	
}
