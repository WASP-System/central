package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.yu.einstein.wasp.exception.UiFieldParseException;
import edu.yu.einstein.wasp.load.service.impl.WaspLoadServiceImpl;
import edu.yu.einstein.wasp.model.UiField;

/**
 * 
 * @author asmclellan
 *
 */
public class UiFieldFamilyWrapperAndLoader {
	
	@Autowired
	@Qualifier("waspLoadServiceImpl")
	WaspLoadServiceImpl waspLoadServiceImpl;

	private String baseLocale;
	
	private String baseArea;
	
	private String baseName;
	
	private String constraint;
	
	private String metaposition;
	
	private String type;
	
	private String range;
	
	private String defaultValue;
	
	private String suffix;
	
	private String customAttribute;
	
	private String customAttrValue;
	
	private String label;
	
	private String control;
	
	private String error;
	
	private String data;
	
	private String tooltip;
	
	private String onClick;
	
	private String onChange;
	
	private List<UiField> uiFields;


	public UiFieldFamilyWrapperAndLoader(){
		baseLocale = Locale.US.toString();
		uiFields = new ArrayList<>();
	}
	
	public UiFieldFamilyWrapperAndLoader(String baseArea, String baseName){
		baseLocale = Locale.US.toString();
		this.baseArea = baseArea;
		this.baseName = baseName;
		uiFields = new ArrayList<>();
	}
	
	public String getBaseLocale() {
		return baseLocale;
	}


	public void setBaseLocale(String baseLocale) {
		this.baseLocale = baseLocale;
	}


	public String getBaseArea() {
		return baseArea;
	}


	public void setBaseArea(String baseArea) {
		this.baseArea = baseArea;
	}


	public String getBaseName() {
		return baseName;
	}


	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}


	public String getConstraint() {
		return constraint;
	}


	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	


	public String getMetaposition() {
		return metaposition;
	}


	public void setMetaposition(String metaposition) {
		this.metaposition = metaposition;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getRange() {
		return range;
	}


	public void setRange(String range) {
		this.range = range;
	}


	public String getDefault() {
		return defaultValue;
	}


	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	public String getSuffix() {
		return suffix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	

	public String getLabel() {
		return label;
	}

	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getTooltip() {
		return tooltip;
	}


	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	public String getControl() {
		return control;
	}


	public void setControl(String control) {
		this.control = control;
	}
	
	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	public void setUsingCustomAttribute(String attrName, String value){
		this.customAttribute = attrName;
		this.customAttrValue = value;
	}
	
	public List<UiField> getUiFields() {
		return uiFields;
	}

	public void setUiFields(List<UiField> uiFields) {
		this.uiFields = uiFields;
	}
	
	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	private List<UiField> generateUiFields(){
		List<UiField> uiFieldList = new ArrayList<UiField>();
		if (this.baseLocale == null || this.baseArea == null || this.baseName == null){
			throw new UiFieldParseException("Cannot make a uiField object without a locale, an area and a basename");
		}
		if (this.constraint != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("constraint");
			uiField.setAttrValue(this.constraint);
			uiFieldList.add(uiField);
		}
		
		if (this.metaposition != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("metaposition");
			uiField.setAttrValue(this.metaposition);
			uiFieldList.add(uiField);
		}
		
		if (this.type != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("type");
			uiField.setAttrValue(this.type);
			uiFieldList.add(uiField);
		}
		
		if (this.range != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("range");
			uiField.setAttrValue(this.range);
			uiFieldList.add(uiField);
		}
		
		if (this.defaultValue != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("default");
			uiField.setAttrValue(this.defaultValue);
			uiFieldList.add(uiField);
		}
		
		if (this.suffix != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("suffix");
			uiField.setAttrValue(this.suffix);
			uiFieldList.add(uiField);
		}
		
		if (this.customAttribute != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName(customAttribute);
			uiField.setAttrValue(this.customAttrValue);
			uiFieldList.add(uiField);
		}
		
		if (this.label != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("label");
			uiField.setAttrValue(this.label);
			uiFieldList.add(uiField);
		}
		
		if (this.tooltip != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("tooltip");
			uiField.setAttrValue(this.tooltip);
			uiFieldList.add(uiField);
		}

		if (this.control != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("control");
			uiField.setAttrValue(this.control);
			uiFieldList.add(uiField);
		}
		
		if (this.error != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("error");
			uiField.setAttrValue(this.error);
			uiFieldList.add(uiField);
		}
		
		if (this.data != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("data");
			uiField.setAttrValue(this.data);
			uiFieldList.add(uiField);
		}
		
		if (this.onClick != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("onclick");
			uiField.setAttrValue(this.onClick);
			uiFieldList.add(uiField);
		}
		
		if (this.onChange != null){
			UiField uiField = new UiField();
			uiField.setLocale(this.baseLocale);
			uiField.setArea(this.baseArea);
			uiField.setName(this.baseName);
			uiField.setAttrName("onchange");
			uiField.setAttrValue(this.onChange);
			uiFieldList.add(uiField);
		}
		
		return uiFieldList;
	}
	
	@PostConstruct
	void loadUiFields(){
		uiFields = generateUiFields();
		if (!uiFields.isEmpty())
			waspLoadServiceImpl.updateUiFields(uiFields);
	}
	
}
