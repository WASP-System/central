package edu.yu.einstein.wasp.viewpanel;

/**
 * Class for describing a generic vizualisation panel. Plugins may be asked to return a WebPanel object for displaying data.
 * The panel data may be used by any web rendering tool for presenting a panel within a view.
 * @author asmclellan
 *
 */
public class GridPanel extends Panel{
	
	private String type = "GridPanel";
	
	private boolean grouping = false;
	
	private String groupField;
	
	private String groupHeader = "{name}";
	
	private boolean hasDownload = false;
	
	private String downloadLinkField;
	
	private String downloadTooltip = "";
	
	private boolean allowSelectDownload = false;
	
	private String selectDownloadText = "";
	
	private String selectDownloadAlign = "center";
	
	private boolean allowGroupDownload = false;
	
	private String groupDownloadTooltip = "";
	
	private String groupDownloadAlign = "right";
	
	private String statusField;
	
	private boolean hasGbUcscLink = false;
	
	private String gbUcscLinkField;
	
	private String gbUcscTooltip = "";

	public GridPanel() {}
	
	public GridPanel(String title, Content content) {
		super(title, content);
	}

	public GridPanel(String title, String description, Integer width, Integer height, Integer order, Content content, boolean resizable, boolean maximizable, boolean closeable) {
		super(title, description, width, height, order, content, resizable, maximizable, closeable);
	}

	public GridPanel(String title, String description, Integer width, Integer height, Content content) {
		super(title, description, width, height, content);
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
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
	public String getGroupField() {
		return groupField;
	}

	/**
	 * @param groupField the groupField to set
	 */
	public void setGroupField(String groupField) {
		this.groupField = groupField;
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
	public String getDownloadLinkField() {
		return downloadLinkField;
	}

	/**
	 * @param downloadLinkField the downloadLinkField to set
	 */
	public void setDownloadLinkField(String downloadLinkField) {
		this.downloadLinkField = downloadLinkField;
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

	/**
	 * @return the statusField
	 */
	public String getStatusField() {
		return statusField;
	}

	/**
	 * @param statusField the statusField to set
	 */
	public void setStatusField(String statusField) {
		this.statusField = statusField;
	}

	/**
	 * @return the hasGbUcsc
	 */
	public boolean isHasGbUcscLink() {
		return hasGbUcscLink;
	}

	/**
	 * @param hasGbUcsc the hasGbUcsc to set
	 */
	public void setHasGbUcscLink(boolean hasGbUcscLink) {
		this.hasGbUcscLink = hasGbUcscLink;
	}

	/**
	 * @return the gbUcscLinkField
	 */
	public String getGbUcscLinkField() {
		return gbUcscLinkField;
	}

	/**
	 * @param gbUcscLinkField the gbUcscLinkField to set
	 */
	public void setGbUcscLinkField(String gbUcscLinkField) {
		this.gbUcscLinkField = gbUcscLinkField;
	}

	/**
	 * @return the gbUcscTooltip
	 */
	public String getGbUcscTooltip() {
		return gbUcscTooltip;
	}

	/**
	 * @param gbUcscTooltip the gbUcscTooltip to set
	 */
	public void setGbUcscTooltip(String gbUcscTooltip) {
		this.gbUcscTooltip = gbUcscTooltip;
	}
	
	

}
