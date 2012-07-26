package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.exception.UiFieldParseException;
import edu.yu.einstein.wasp.model.UiField;


/**
 * base clase for all the wasp loaders
 * full of convenience methods like
 *	- updateUiFields
 *
 */

public abstract class WaspLoader {

	protected final Logger logger = Logger.getLogger(getClass());
	
	protected String domain;
	public void setDomain(String domain) { this.domain = domain;}

	protected String iname; 
	public void setIName(String iname) {this.iname = iname; }
	
	protected String name; 
	public void setName(String name) {this.name = name; }
	
	protected String area;
	public void setArea(String area){
		this.area = StringUtils.replace(area, ".", "_"); 
		this.iname = area;
	}
	
	protected List<UiField> uiFields; 
	
	public void setUiFields(List<UiField> uiFields) {
		this.uiFields = uiFields;	
	}
	
	public void setUiFieldGroups(List< List<UiField> > uiFieldsList) {
		int metapositionOffset = 0;
		for (List<UiField> uiFieldSet : safeList(uiFieldsList)){
			int metapositionMaxPos = -1;
			for (UiField uiField : uiFieldSet){
				if (uiField.getAttrName().equals("metaposition")){
					int metaPosition;
					try{
						metaPosition = Integer.parseInt(uiField.getAttrValue());
					} 
					catch(NumberFormatException e){
						throw new UiFieldParseException("Cannot parse '"+uiField.getAttrValue()+"' to Integer");
					}
					uiField.setAttrValue( Integer.toString(metapositionOffset + metaPosition) );
					if (metaPosition > metapositionMaxPos)
						metapositionMaxPos = metaPosition;
				}
				if (this.uiFields == null)
					this.uiFields = new  ArrayList<UiField>();
				this.uiFields.add(uiField);
			}
		}
	}
	
	public void setUiFieldsFromWrapper(List<UiFieldFamilyWrapper> uiFieldWrappers) {
		for (UiFieldFamilyWrapper uiFieldWrapper : safeList(uiFieldWrappers)){
			if (this.uiFields == null)
				this.uiFields = new  ArrayList<UiField>();
			this.uiFields.addAll(uiFieldWrapper.getUiFields());
		}
	}
	
	public void setUiFieldGroupsFromWrapper(List< List<UiFieldFamilyWrapper> > uiFieldWrappers) {
		int metapositionOffset = 0;
		for (List<UiFieldFamilyWrapper> uiFieldWrapperSet : safeList(uiFieldWrappers)){
			int metapositionMaxPos = -1;
			for (UiFieldFamilyWrapper uiFieldWrapper : uiFieldWrapperSet){
				for (UiField uiField : uiFieldWrapper.getUiFields()){
					if (uiField.getAttrName().equals("metaposition")){
						int metaPosition;
						try{
							metaPosition = Integer.parseInt(uiField.getAttrValue());
						} 
						catch(NumberFormatException e){
							throw new UiFieldParseException("Cannot parse '"+uiField.getAttrValue()+"' to Integer");
						}
						uiField.setAttrValue( Integer.toString(metapositionOffset + metaPosition) );
						if (metaPosition > metapositionMaxPos)
							metapositionMaxPos = metaPosition;
					}
					if (this.uiFields == null)
						this.uiFields = new  ArrayList<UiField>();
					this.uiFields.add(uiField);
				}
			}
		}
	}
	
	
	
	public List<UiField> getUiFields() {return this.uiFields; }

	protected static final Logger log = Logger.getLogger(WaspLoader.class);
	
	protected WaspLoader sourceLoadService;
	
	public void setInheritUiFieldsFromLoadService(WaspLoader sourceLoadService){
		this.sourceLoadService = sourceLoadService;
	}
	
	
	public WaspLoader(){}
	
	public WaspLoader (WaspLoader sourceLoadService){
		this.sourceLoadService = sourceLoadService;
	}
		
	
	@SuppressWarnings("unchecked")
	protected <E> List<E> safeList(List<E> list){
		return (list == null ? Collections.EMPTY_LIST : list);
	}
	
	@SuppressWarnings("unchecked")
	protected <E> Set<E> safeSet(Set<E> set){
		return (set == null ? Collections.EMPTY_SET : set);
	}

}

