/**
 * 
 */
package edu.yu.einstein.wasp.viewpanel;

/**
 * @author aj
 * 
 */
public class GridColumn {
	
	private String header;
	
	private String dataIndex;
	
	private Integer width = null;
	
	private Integer flex = 0;
	
	private boolean sortable = false;
	
	private boolean hideable = false;
	
	private String align = "left";
	
	private String style = "text-align:left";

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(Integer width) {
		this.width = width;
		this.flex = 0;
	}

	/**
	 * @return the dataIndex
	 */
	public String getDataIndex() {
		return dataIndex;
	}

	/**
	 * @param dataIndex the dataIndex to set
	 */
	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}

	/**
	 * @return the flex
	 */
	public Integer getFlex() {
		return flex;
	}

	/**
	 * @param flex the flex to set
	 */
	public void setFlex(Integer flex) {
		this.flex = flex;
		if (flex > 0) {
			this.width = null;
		}
	}

	/**
	 * @return the sortable
	 */
	public boolean isSortable() {
		return sortable;
	}

	/**
	 * @param sortable the sortable to set
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * @return the hideable
	 */
	public boolean isHideable() {
		return hideable;
	}

	/**
	 * @param hideable the hideable to set
	 */
	public void setHideable(boolean hideable) {
		this.hideable = hideable;
	}

	/**
	 * @return the align
	 */
	public String getCellAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setCellAlign(String align) {
		String al = align.trim().toLowerCase();
		if (al.equals("left") || al.equals("right")) {
			this.align = align;
		}
	}

	/**
	 * @return the style
	 */
	public String getHeaderAlign() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setHeaderAlign(String align) {
		String al = align.trim().toLowerCase();
		if (al.equals("left") || al.equals("right")) {
			this.style = "text-align:" + align;
		}
	}

	/**
	 * @param header
	 * @param dataIndex
	 */
	public GridColumn(String header, String dataIndex) {
		this.header = header;
		this.dataIndex = dataIndex;
	}

	/**
	 * @param header
	 * @param width
	 * @param dataIndex
	 */
	public GridColumn(String header, String dataIndex, Integer width, Integer flex) {
		this.header = header;
		this.dataIndex = dataIndex;
		this.width = width;
	}

	/**
	 * @param header
	 * @param dataIndex
	 * @param flex
	 */
	public GridColumn(String header, String dataIndex, Integer flex) {
		this.header = header;
		this.dataIndex = dataIndex;
		this.flex = flex;
	}

	/**
	 * @param header
	 * @param width
	 * @param dataIndex
	 * @param flex
	 * @param sortable
	 * @param hideable
	 */
	public GridColumn(String header, String dataIndex, Integer width,
			Integer flex, boolean sortable, boolean hideable) {
		this.header = header;
		this.width = width;
		this.dataIndex = dataIndex;
		this.flex = flex;
		this.sortable = sortable;
		this.hideable = hideable;
	}
	

}
