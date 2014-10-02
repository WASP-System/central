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
	
	private String cellAlign = "left";
	
	private String headerAlign = "left";
	
	private boolean shownInTtp = false;

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
	 * @param flex the flex to set, "0" mean "no flex"
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
	 * @return the cellAlign
	 */
	public String getCellAlign() {
		return cellAlign;
	}

	/**
	 * @param cellAlign the cellAlign to set "left" or "center" or "right"
	 */
	public void setCellAlign(String cellAlign) {
		String al = cellAlign.trim().toLowerCase();
		if (al.equals("left") || al.equals("center") || al.equals("right")) {
			this.cellAlign = al;
		}
	}

	/**
	 * @return the headerAlign
	 */
	public String getHeaderAlign() {
		return headerAlign;
	}

	/**
	 * @param headerAlign the headerAlign to set "left" or "center" or "right"
	 */
	public void setHeaderAlign(String headerAlign) {
		String al = headerAlign.trim().toLowerCase();
		if (al.equals("left") || al.equals("center") || al.equals("right")) {
			this.headerAlign = al;
		}
	}

	/**
	 * @return the shownInTtp
	 */
	public boolean isShownInTtp() {
		return shownInTtp;
	}

	/**
	 * @param shownInTtp the shownInTtp to set
	 */
	public void setShownInTtp(boolean shownInTtp) {
		this.shownInTtp = shownInTtp;
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
	 * @param dataIndex
	 * @param showInTtp
	 */
	public GridColumn(String header, String dataIndex, boolean shownInTtp) {
		this.header = header;
		this.dataIndex = dataIndex;
		this.shownInTtp = shownInTtp;
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
	 * @param dataIndex
	 * @param flex
	 * @param showInTtp
	 */
	public GridColumn(String header, String dataIndex, Integer flex, boolean shownInTtp) {
		this.header = header;
		this.dataIndex = dataIndex;
		this.flex = flex;
		this.shownInTtp = shownInTtp;
	}

	/**
	 * @param header
	 * @param dataIndex
	 * @param flex
	 * @param cellalign "left" or "center" or "right"
	 */
	public GridColumn(String header, String dataIndex, Integer flex, String cellAlign) {
		this.header = header;
		this.dataIndex = dataIndex;
		if (flex!=null && flex>0) {
			this.width = 0;
			this.flex = flex;
		}
		this.setCellAlign(cellAlign);
	}

	/**
	 * @param header
	 * @param dataIndex
	 * @param flex
	 * @param cellalign "left" or "center" or "right"
	 * @param headeralign "left" or "center" or "right"
	 */
	public GridColumn(String header, String dataIndex, Integer flex, String cellAlign, String headerAlign) {
		this.header = header;
		this.dataIndex = dataIndex;
		if (flex!=null && flex>0) {
			this.width = 0;
			this.flex = flex;
		}
		this.setCellAlign(cellAlign);
		this.setHeaderAlign(headerAlign);
	}

	/**
	 * @param header
	 * @param dataIndex
	 * @param width width can be set only when flex is 0 or null; width is reset to 0 when flex>0
	 * @param flex
	 */
	public GridColumn(String header, String dataIndex, Integer width, Integer flex) {
		this.header = header;
		this.dataIndex = dataIndex;
		this.width = width;
		if (flex!=null && flex>0) {
			this.width = 0;
			this.flex = flex;
		}
	}

	/**
	 * @param header
	 * @param dataIndex
	 * @param width width can be set only when flex is 0 or null; width is reset to 0 when flex>0
	 * @param flex
	 * @param cellalign "left" or "center" or "right"
	 */
	public GridColumn(String header, String dataIndex, Integer width, Integer flex, String cellAlign) {
		this.header = header;
		this.dataIndex = dataIndex;
		this.width = width;
		if (flex!=null && flex>0) {
			this.width = 0;
			this.flex = flex;
		}
		this.setCellAlign(cellAlign);
	}

	/**
	 * @param header
	 * @param dataIndex
	 * @param width width can be set only when flex is 0 or null; width is reset to 0 when flex>0
	 * @param flex
	 * @param cellalign "left" or "center" or "right"
	 * @param headeralign "left" or "center" or "right"
	 */
	public GridColumn(String header, String dataIndex, Integer width, Integer flex, String cellAlign, String headerAlign) {
		this.header = header;
		this.dataIndex = dataIndex;
		this.width = width;
		if (flex!=null && flex>0) {
			this.width = 0;
			this.flex = flex;
		}
		this.setCellAlign(cellAlign);
		this.setHeaderAlign(headerAlign);
	}

	/**
	 * @param header
	 * @param dataIndex
	 * @param width width can be set only when flex is 0 or null; width is reset to 0 when flex>0
	 * @param flex
	 * @param sortable
	 * @param hideable
	 */
	public GridColumn(String header, String dataIndex, Integer width,
			Integer flex, boolean sortable, boolean hideable) {
		this.header = header;
		this.width = width;
		this.dataIndex = dataIndex;
		if (flex!=null && flex>0) {
			this.width = 0;
			this.flex = flex;
		}
		this.sortable = sortable;
		this.hideable = hideable;
	}
	

}
