package edu.yu.einstein.wasp.controller.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaAttribute;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;

/*
 * implements validation of MetaBase children based on messages_en.properties file
 * @Author Sasha Levchuk
 */

public interface MetaValidator {

	public void setValidateList(List <String> validateList); 

	public void validate(List<String> validateList, List<? extends MetaBase> list, BindingResult result, MetaAttribute.Area area);

	public void validate(List<String> validateList, List<? extends MetaBase> list, BindingResult result, String area);

	public void validate(List<? extends MetaBase> list, BindingResult result, MetaAttribute.Area area);

	public void validate(List<? extends MetaBase> list, BindingResult result, String area);
	
	public void validate(List<? extends MetaBase> list, BindingResult result, MetaAttribute.Area area, MetaAttribute.Area parentArea);
	
	public void validate(List<? extends MetaBase> list, BindingResult result, String area, String parentarea);
}
