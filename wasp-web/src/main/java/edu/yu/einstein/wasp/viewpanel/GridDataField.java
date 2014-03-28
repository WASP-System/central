/**
 * 
 */
package edu.yu.einstein.wasp.viewpanel;

/**
 * @author aj
 *
 */
public class GridDataField {
	
	private String name;
	
	private String type;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param name
	 * @param type
	 */
	public GridDataField(String name, String type) {
		this.name = name;
		this.type = type;
	}

}
