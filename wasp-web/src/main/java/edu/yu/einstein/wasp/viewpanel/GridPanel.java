package edu.yu.einstein.wasp.viewpanel;

/**
 * Class for describing a generic vizualisation panel. Plugins may be asked to return a WebPanel object for displaying data.
 * The panel data may be used by any web rendering tool for presenting a panel within a view.
 * @author asmclellan
 *
 */
public class GridPanel extends Panel{
	
	private boolean grouping = false;
	
	private String groupFieldName;
	
	private String groupHeader = "{name}";
	
	private boolean hasDownload = false;
	
	private String downloadLinkFieldName;
	
	private String downloadTooltip = "";
	
	private boolean allowSelectDownload = false;
	
	private String selectDownloadText = "";
	
	private String selectDownloadAlign = "center";
	
	private boolean allowGroupDownload = false;
	
	private String groupDownloadTooltip = "";
	
	private String groupDownloadAlign = "right";

	public GridPanel() {}
	
	public GridPanel(String title, Content content) {
		super(title, content);
	}

	public GridPanel(String title, String description, Integer width, Integer height, Integer order, Content content, boolean isResizable, boolean isMaximizable, boolean isCloseable) {
		super(title, description, width, height, order, content, isResizable, isMaximizable, isCloseable);
	}

	public GridPanel(String title, String description, Integer width, Integer height, Content content) {
		super(title, description, width, height, content);
	}

	/**
	 * @return the grouping
	 */
	public boolean isGrouping() {
		return grouping;
	}

	/**
	 * @param grouping the grouping to set
	 */
	public void setGrouping(boolean grouping) {
		this.grouping = grouping;
	}

	/**
	 * @return the groupField
	 */
	public String getGroupFieldName() {
		return groupFieldName;
	}

	/**
	 * @param groupField the groupField to set
	 */
	public void setGroupFieldName(String groupFieldName) {
		this.groupFieldName = groupFieldName;
	}

	/**
	 * @return the groupHeader
	 */
	public String getGroupHeader() {
		return groupHeader;
	}

	/**
	 * @param groupHeader the groupHeader to set
	 */
	public void setGroupHeader(String groupHeader) {
		this.groupHeader = groupHeader;
	}

	/**
	 * @return the hasDownload
	 */
	public boolean isHasDownload() {
		return hasDownload;
	}

	/**
	 * @param hasDownload the hasDownload to set
	 */
	public void setHasDownload(boolean hasDownload) {
		this.hasDownload = hasDownload;
	}

	/**
	 * @return the downloadLinkField
	 */
	public String getDownloadLinkFieldName() {
		return downloadLinkFieldName;
	}

	/**
	 * @param downloadLinkField the downloadLinkField to set
	 */
	public void setDownloadLinkFieldName(String downloadLinkFieldName) {
		this.downloadLinkFieldName = downloadLinkFieldName;
	}

	/**
	 * @return the downloadTooltip
	 */
	public String getDownloadTooltip() {
		return downloadTooltip;
	}

	/**
	 * @param downloadTooltip the downloadTooltip to set
	 */
	public void setDownloadTooltip(String downloadTooltip) {
		this.downloadTooltip = downloadTooltip;
	}

	/**
	 * @return the allowSelectDownload
	 */
	public boolean isAllowSelectDownload() {
		return allowSelectDownload;
	}

	/**
	 * @param allowSelectDownload the allowSelectDownload to set
	 */
	public void setAllowSelectDownload(boolean allowSelectDownload) {
		this.allowSelectDownload = allowSelectDownload;
	}

	/**
	 * @return the selectDownloadText
	 */
	public String getSelectDownloadText() {
		return selectDownloadText;
	}

	/**
	 * @param selectDownloadText the selectDownloadText to set
	 */
	public void setSelectDownloadText(String selectDownloadText) {
		this.selectDownloadText = selectDownloadText;
	}

	/**
	 * @return the selectDownloadAlign
	 */
	public String getSelectDownloadAlign() {
		return selectDownloadAlign;
	}

	/**
	 * @param selectDownloadAlign the selectDownloadAlign to set
	 */
	public void setSelectDownloadAlign(String selectDownloadAlign) {
		this.selectDownloadAlign = selectDownloadAlign;
	}

	/**
	 * @return the allowGroupDownload
	 */
	public boolean isAllowGroupDownload() {
		return allowGroupDownload;
	}

	/**
	 * @param allowGroupDownload the allowGroupDownload to set
	 */
	public void setAllowGroupDownload(boolean allowGroupDownload) {
		this.allowGroupDownload = allowGroupDownload;
	}

	/**
	 * @return the groupDownloadTooltip
	 */
	public String getGroupDownloadTooltip() {
		return groupDownloadTooltip;
	}

	/**
	 * @param groupDownloadTooltip the groupDownloadTooltip to set
	 */
	public void setGroupDownloadTooltip(String groupDownloadTooltip) {
		this.groupDownloadTooltip = groupDownloadTooltip;
	}

	/**
	 * @return the groupDownloadAlign
	 */
	public String getGroupDownloadAlign() {
		return groupDownloadAlign;
	}

	/**
	 * @param groupDownloadAlign the groupDownloadAlign to set
	 */
	public void setGroupDownloadAlign(String groupDownloadAlign) {
		this.groupDownloadAlign = groupDownloadAlign;
	}
	
	

}
