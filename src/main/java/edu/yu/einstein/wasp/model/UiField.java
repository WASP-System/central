package edu.yu.einstein.wasp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

/*
 * Holds definition of user input field
 * @Author Sasha Levchuk
 */
@Entity
@Table(name = "uifield")
public final class UiField implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int uiFieldId;

	@NotEmpty
	private String area;
	
	@NotEmpty
	private String name;

	@Column(name = "attrname")
	@NotEmpty
	private String attrName;

	@Column(name = "attrvalue")
	private String attrValue;

	@Column(name = "locale")
	@NotEmpty
	protected String locale;

	

	public int getUiFieldId() {
		return uiFieldId;
	}

	public void setUiFieldId(int uiFieldId) {
		this.uiFieldId = uiFieldId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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
	  protected int lastUpdUser;
	  public void setLastUpdUser (int lastUpdUser) {
	    this.lastUpdUser = lastUpdUser;
	  }
	  public int getLastUpdUser () {
	    return this.lastUpdUser;
	  }


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	  
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uiFieldId;
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
		if (uiFieldId != other.uiFieldId)
			return false;
		return true;
	}
	  
	  

}
