package edu.yu.einstein.wasp.controller.validator;

import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.User;

/**
 * 
 * @author asmclellan
 *
 */
public class UserPendingMetaValidatorImpl extends MetaValidatorImpl{

	protected UserDao userDao;
	protected LabDao labDao;
	
	public UserPendingMetaValidatorImpl(UserDao userDao, LabDao labDao){
		this.allowableConstraints.add(Constraint.isValidPiId); 
		this.userDao = userDao;
		this.labDao = labDao;
	}

 	@Override
	public void validate(List<? extends MetaBase> list, BindingResult result, String area) {
 		super.validate(list, result, area); // call the overridden method in the superclass to validate
 		
		Errors errors=new BindException(result.getTarget(), area); 
		for(int i=0;i<list.size();i++) {
			MetaBase meta=list.get(i);
			if (meta.getProperty().getFormVisibility().equals(MetaAttribute.FormVisibility.ignore)) continue;
			String constraint=meta.getProperty().getConstraint();
			if (constraint==null) continue;
			String errorFieldName = area+"Meta["+i+"].k";
			String errorMessageKey = meta.getK() + ".error";
		    String defaultMessage = errorMessageKey+" (no message has been defined for this property)";
		
			if (constraint.equals( Constraint.isValidPiId.name() ) ){
		        if (meta.getV()==null || meta.getV().isEmpty()) {
		        	errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
		        } else {
		        	errorMessageKey = meta.getK() + "_notvalid.error";
					defaultMessage = errorMessageKey+" (no message has been defined for this property)";
					//User primaryInvestigator = userDao.getUserByLogin(meta.getV()); //02-04-2013, replaced by next line, as the web page is now accessing the PI via his/her email address rather than login name
					User primaryInvestigator = userDao.getUserByEmail(meta.getV());
					if (primaryInvestigator.getId() == null || primaryInvestigator.getIsActive() == null){
					  errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
					} else {
						Lab lab = labDao.getLabByPrimaryUserId(primaryInvestigator.getId());
						if (lab.getId() == null){
							errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
						}
					}
		        }
			}
		}
		result.addAllErrors(errors);
	}
}
