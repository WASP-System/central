package edu.yu.einstein.wasp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import edu.yu.einstein.wasp.exception.UiFieldParseException;

/*
 * Holds definition of user input field
 * @Author Sasha Levchuk
 */
@Entity
@Table(name = "uifield")
public final class UiField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3854600504514244535L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer uiFieldId;
	
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

	@Column(name = "attrvalue")
	protected String attrValue;

	@Column(name = "locale")
	@NotEmpty
	protected String locale;

	

	public Integer getUiFieldId() {
		return uiFieldId;
	}

	public void setUiFieldId(Integer uiFieldId) {
		this.uiFieldId = uiFieldId;
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

	  @Column(name="lastupdts")
	  protected Date lastUpdTs;
	  public void setLastUpdTs (Date lastUpdTs) {
	    this.lastUpdTs = lastUpdTs;
	  }
	  public Date getLastUpdTs () {
	    return this.lastUpdTs;
	  }


	  @Column(name="lastupduser")
	  protected Integer lastUpdUser;
	  public void setLastUpdUser (Integer lastUpdUser) {
	    this.lastUpdUser = lastUpdUser;
	  }
	  public Integer getLastUpdUser () {
	    return this.lastUpdUser;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;

// uiFieldId now Integer instead of in, so avoid npe
if (uiFieldId == null) { return prime * result + 0; }
		result = prime * result + uiFieldId.intValue();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UiField other = (UiField) obj;
		if (uiFieldId.intValue() != other.uiFieldId.intValue())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UiField [uiFieldId=" + uiFieldId + ", domain=" + domain + ", area=" + area + ", name="
				+ name + ", attrName=" + attrName + ", attrValue=" + attrValue
				+ ", locale=" + locale + ", lastUpdTs=" + lastUpdTs
				+ ", lastUpdUser=" + lastUpdUser + "]";
	}
	  
	  

}
