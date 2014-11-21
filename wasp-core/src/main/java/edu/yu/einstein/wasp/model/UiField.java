package edu.yu.einstein.wasp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import edu.yu.einstein.wasp.exception.UiFieldParseException;

/*
 * Holds definition of user input field
 * @Author Sasha Levchuk
 */
@Entity
@Table(name = "uifield")
public final class UiField extends WaspModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3854600504514244535L;
	
	@Column(name = "domain")
	protected String domain;

	@NotEmpty
	@Column(name = "area")
	protected String area;
	
	@NotEmpty
	@Column(name = "name")
	protected String name;
	
	@NotEmpty
	@Column(name = "attrname")
	protected String attrName;

	@Lob
	@Column(name = "attrvalue")
	protected String attrValue;

	@Column(name = "locale", length=5)
	@NotEmpty
	protected String locale;

	

	@Deprecated
	public Integer getUiFieldId() {
		return getId();
	}

	@Deprecated
	public void setUiFieldId(Integer uiFieldId) {
		setId(uiFieldId);
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	/**
	 * Sets uiFields from String e.g. en_US.myArea.myName.myAttribute=myValue
	 * @param uiFieldsAsString
	 */
	public void setUiFieldString(String uiFieldsAsString){
		String[] keyValuePairs = uiFieldsAsString.split("=");
		if (keyValuePairs.length < 1){
			throw new UiFieldParseException();
		} else if (keyValuePairs.length == 1){
			this.attrValue = "";
		}
		this.attrValue = keyValuePairs[1].trim();
		
		String[] keyComponents = keyValuePairs[0].trim().split(".");
		if (keyComponents.length != 4){
			throw new UiFieldParseException();
		}
		this.locale = keyComponents[0];
		this.area = keyComponents[1];
		this.name = keyComponents[2];
		this.attrName = keyComponents[3];
	}
	
	/**
	 * Sets uiFields from String (assuming area and locale pre-defined e.g.myName.myAttribute=myValue)
	 * @param uiFieldsAsString
	 */
	public void setUiFieldShortString(String uiFieldsAsString){
		String[] keyValuePairs = uiFieldsAsString.split("=");
		if (keyValuePairs.length < 1){
			throw new UiFieldParseException();
		} else if (keyValuePairs.length == 1){
			this.attrValue = "";
		}
		this.attrValue = keyValuePairs[1].trim();
		
		String[] keyComponents = keyValuePairs[0].trim().split(".");
		if (keyComponents.length != 2){
			throw new UiFieldParseException();
		}
		this.name = keyComponents[0];
		this.attrName = keyComponents[1];
	}
	  
	@Override
	public int hashCode(){
	    int hc = new HashCodeBuilder()
	        .append(area)
	        .append(attrName)
	        .append(attrValue)
	        .append(domain)
	        .append(locale)
	        .append(name)
	        .toHashCode();
	    // logger.trace("uifield hashcode: " + hc);
	    return hc;
	}


	@Override
	public boolean equals(final Object obj){
		boolean equals = false;
	    if(obj instanceof UiField){
	        final UiField other = (UiField) obj;
	        equals = new EqualsBuilder()
	            .append(area, other.area)
	            .append(attrName, other.attrName)
	            .append(attrValue, other.attrValue)
	            .append(domain, other.domain)
	            .append(locale, other.locale)
	            .append(name, other.name)
	            .isEquals();
	    } 
	    // logger.trace("uifield " + name + " equals= " + equals);
	    return equals;
	}

	@Override
	public String toString() {
		String message =  "UiField [uiFieldId=" + getId() + ", domain=" + domain + ", area=" + area + ", name="
				+ name + ", attrName=" + attrName + ", attrValue=" + attrValue
				+ ", locale=" + locale + ", lastUpdTs=" + updated
				+ ", lastUpdUser=";
		if (lastUpdatedByUser == null || lastUpdatedByUser.getId()==null ) 
			message += "{not set}";
		else 
			message += lastUpdatedByUser.getId();
		message += "]";
		return message;
	}
	  
	  

}
