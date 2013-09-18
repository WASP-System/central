package edu.yu.einstein.wasp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.model.MetaAttribute.Control;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;


/**
 * contains utility methods for manipulating 
 * with *meta and MetaAttribute objects
 * @author Sasha Levchuk
 *
 */
public final class MetaUtil {
	
		protected final static Logger logger = LoggerFactory.getLogger(MetaUtil.class);
	
		/**
		 * 1. populates "control" and "position" property of each object in the list
		 * with values from the uifield messages database table
		 *
		 * 2. Sorts list on meta.position field 
		 * @param list
		 * @param area
		 * @param locale
		 */
		@SuppressWarnings("unchecked")
		public static final <T> void setAttributesAndSort(List<T> list, String area, Locale locale) {
			 
			 for(T t:list) {
				 
				 MetaBase m=(MetaBase)t;
		
				
				String basename=m.getK().substring(area.length()+1);
				
				//some old key we dont know about 
				if (getValue(area,basename,"metaposition")==null) continue;
				
				MetaAttribute p=new MetaAttribute();
				
				m.setProperty(p);
				
				Integer pos=-1;
						    		
				try {	    			
					pos=Integer.parseInt(getValue(area,basename,"metaposition"));
				} catch (Throwable e) {}
				
				p.setMetaposition(pos);
				
				
				p.setControl(getControl(getValue(area,basename,"control"), locale));
				
				
				p.setLabel(getValue(area,basename,"label", locale));
				p.setConstraint(getValue(area,basename,"constraint", locale));
				p.setError(getValue(area,basename,"error",locale));		 		
				
			 }
			 
			 Collections.sort( list, META_POSITION_COMPARATOR);
		 }
		
	/**
	 * Gets a control object for supplied key (e.g. 'user.country.control')
	 * @param key
	 * @param locale
	 * @return @{link Control}
	 */
	public static Control getControl(String key, Locale locale) {
		
		//WaspMessageSourceImpl
		if (! DBResourceBundle.MESSAGE_SOURCE.contains(key, locale) ){
			return null;
		}
		logger.debug("getting controlStr for key=" + key);
		String controlStr=getMessage(key, locale);
		logger.debug("got controlStr=" + controlStr);
		return getControl(controlStr);
		
	}
	
	/**
	 * Creates and returns a {@link Control} object by parsing supplied message (controlStr).
	 * In controlStr a select box is represented as either
	 * 1) select:${beanName}:beanAttributeNameForItemValue:beanAttributeNameForItemLabel (e.g. ${user}:UserId:lastName) or
	 * 2) select:item1Value:item1Label;item2Value:item2Label etc
	 * @param controlStr
	 * @return @{link Control} (null if controlStr equals null)
	 */
	public static Control getControl(String controlStr) {
		
		if (controlStr==null) return null;
		
		String typeStr=controlStr.substring(0,controlStr.indexOf(":"));
		
		MetaAttribute.Control.Type type=MetaAttribute.Control.Type.valueOf(typeStr);
		
		if (type!=MetaAttribute.Control.Type.select) return null;
		
		MetaAttribute.Control control=new MetaAttribute.Control();
			
		control.setType(MetaAttribute.Control.Type.select); 				
			
			if (controlStr.startsWith("select:${")) {
				String [] els=StringUtils.tokenizeToStringArray(controlStr,":");
				if (els==null || els.length!=4) {
					throw new IllegalStateException(controlStr+" must match 'select:${beanName}:itemValue:itemLabel' pattern");
				}
				
				String beanName=els[1].replace("${","").replace("}", "");
				control.setItems(beanName);
				control.setItemValue(els[2]);
				control.setItemLabel(els[3]);
				 					
			} else {
			 
				List<MetaAttribute.Control.Option> options=new ArrayList<MetaAttribute.Control.Option>();
				
				String[] pairs=StringUtils.tokenizeToStringArray(controlStr.substring(controlStr.indexOf(":")+1),";");
							
				for(String el:pairs) {
					String [] pair=StringUtils.split(el,":");
					MetaAttribute.Control.Option option = new MetaAttribute.Control.Option();
					option.setValue(pair[0]);
					option.setLabel(pair[1]);
					options.add(option);
				}
				control.setOptions(options);	
			}						

			return control;
	}
	
	/**
	 * Returns message (US locale) for given area, name and attribute (represented as key 'area.name.attribute' in the database)
	 * @param area
	 * @param name
	 * @param attribute
	 * @return message
	 */
	private static String getValue(String area, String name, String attribute) {
			 
			 return getMessage(area+"."+name+"."+attribute,Locale.US);
				 
	 }
	 
	/**
	 * Returns message for given area, name and attribute (represented as key 'area.name.attribute' in the database) plus locale
	 * @param area
	 * @param name
	 * @param attribute
	 * @param locale
	 * @return message
	 */
	 private static String getValue(String area, String name, String attribute, Locale locale) {
		 
		 return getMessage(area+"."+name+"."+attribute,locale);
				 
	 }
		
	/**
	 * Returns message for given key ('area.name.attribute' in the database) and locale 
	 * @param key
	 * @param locale
	 * @return
	 */
	private static String getMessage(String key, Locale locale) {
		return DBResourceBundle.MESSAGE_SOURCE.getNestedMessage(key, null, locale);  // possibly nested internationalized values
	}
		 
	@SuppressWarnings("rawtypes")
	private final static Comparator META_POSITION_COMPARATOR =  new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			
			MetaBase f1=(MetaBase)o1;
			MetaBase f2=(MetaBase)o2;

			if (f1==null || f1.getProperty()==null || f1.getProperty().getMetaposition().intValue()==-1 ) return -1;
			if (f2==null || f2.getProperty()==null || f2.getProperty().getMetaposition().intValue()==-1 ) return 1;
			
			
			Integer p1=f1.getProperty().getMetaposition();
			Integer p2=f2.getProperty().getMetaposition();	    		 
			
			return p1.compareTo(p2);
	
		}
	};

	
}
