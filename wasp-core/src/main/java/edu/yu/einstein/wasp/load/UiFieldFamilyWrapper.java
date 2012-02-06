package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.exception.UiFieldParseException;
import edu.yu.einstein.wasp.model.UiField;

public class UiFieldFamilyWrapper {

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

	private Map<String, String> localizedLabel;
	
	private Map<String, String> localizedControl;
	
	private Map<String, String> localizedError;
	
	private Map<String, String> localizedData;
	
	public UiFieldFamilyWrapper(){}
	
	public UiFieldFamilyWrapper(String baseLocale, String baseArea, String baseName){
		this.baseLocale = baseLocale;
		this.baseArea = baseArea;
		this.baseName = baseName;
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
	

	public Map<String, String> getLocalizedLabel() {
		return localizedLabel;
	}


	public void setLocalizedLabel(Map<String, String> localizedLabel) {
		this.localizedLabel = localizedLabel;
	}
	
	public void setLabel(String label) {
		if (this.localizedLabel == null){
			this.localizedLabel = new HashMap<String, String>();
		}
		this.localizedLabel.put(this.baseLocale, label);
	}


	public Map<String, String> getLocalizedControl() {
		return localizedControl;
	}


	public void setLocalizedControl(Map<String, String> localizedControl) {
		this.localizedControl = localizedControl;
	}
	
	public void setControl(String control) {
		if (this.localizedControl == null){
			this.localizedControl = new HashMap<String, String>();
		}
		this.localizedControl.put(this.baseLocale, control);
	}


	public Map<String, String> getLocalizedError() {
		return localizedError;
	}


	public void setLocalizedError(Map<String, String> localizedError) {
		this.localizedError = localizedError;
	}
	
	public void setError(String error) {
		if (this.localizedError == null){
			this.localizedError = new HashMap<String, String>();
		}
		this.localizedError.put(this.baseLocale, error);
	}


	public Map<String, String> getLocalizedData() {
		return localizedData;
	}
	
	public void setData(String data) {
		if (this.localizedData == null){
			this.localizedData = new HashMap<String, String>();
		}
		this.localizedData.put(this.baseLocale, data);
	}

	public void setLocalizedData(Map<String, String> localizedData) {
		this.localizedData = localizedData;
	}

	public void setUsingCustomAttribute(String attrName, String value){
		this.customAttribute = attrName;
		this.customAttrValue = value;
	}
	
	public List<UiField> getUiFields(){
		List<UiField> uiFieldList = new ArrayList<UiField>();
		if (this.baseLocale == null || this.baseArea == null || this.baseName == null){
			throw new UiFieldParseException("Cannot make a uiField object without a locale, an area and a field name");
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
		
		if (this.localizedLabel != null){
			for (String locale : this.localizedLabel.keySet()){
				UiField uiField = new UiField();
				uiField.setLocale(locale);
				uiField.setArea(this.baseArea);
				uiField.setName(this.baseName);
				uiField.setAttrName("label");
				uiField.setAttrValue(this.localizedLabel.get(locale));
				uiFieldList.add(uiField);
			}
		}

		if (this.localizedControl != null){
			for (String locale : this.localizedControl.keySet()){
				UiField uiField = new UiField();
				uiField.setLocale(locale);
				uiField.setArea(this.baseArea);
				uiField.setName(this.baseName);
				uiField.setAttrName("control");
				uiField.setAttrValue(this.localizedControl.get(locale));
				uiFieldList.add(uiField);
			}
		}
		
		if (this.localizedError != null){
			for (String locale : this.localizedError.keySet()){
				UiField uiField = new UiField();
				uiField.setLocale(locale);
				uiField.setArea(this.baseArea);
				uiField.setName(this.baseName);
				uiField.setAttrName("error");
				uiField.setAttrValue(this.localizedError.get(locale));
				uiFieldList.add(uiField);
			}
		}
		
		if (this.localizedData != null){
			for (String locale : this.localizedData.keySet()){
				UiField uiField = new UiField();
				uiField.setLocale(locale);
				uiField.setArea(this.baseArea);
				uiField.setName(this.baseName);
				uiField.setAttrName("data");
				uiField.setAttrValue(this.localizedData.get(locale));
				uiFieldList.add(uiField);
			}
		}
		return uiFieldList;
	}
	
}
