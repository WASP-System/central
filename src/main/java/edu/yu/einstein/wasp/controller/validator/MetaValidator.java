package edu.yu.einstein.wasp.controller.validator;

import java.util.List;

import org.springframework.validation.BindingResult;

import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;

/*
 * implements validation of MetaBase children based on uifield.constraint database field.
 * @Author Sasha Levchuk
 */

public interface MetaValidator {

	public void validate(List<? extends MetaBase> list, BindingResult result, MetaAttribute.Area area);

	public void validate(List<? extends MetaBase> list, BindingResult result, String area);
	
	public void validate(List<? extends MetaBase> list, BindingResult result, MetaAttribute.Area area, MetaAttribute.Area parentArea);
	
	public void validate(List<? extends MetaBase> list, BindingResult result, String area, String parentarea);
}
