package edu.yu.einstein.wasp.viewpanel;

import java.util.List;

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
	
	private String statusField;
	
//	private boolean hasDownload = false;
//	
//	private String downloadLinkField;
//	
//	private String downloadTooltip = "";
//	
//	private String hideDownloadField = "";
	
	private boolean allowSelectDownload = false;
	
	private String selectDownloadText = "";
	
	private String selectDownloadAlign = "center";
	
	private boolean allowGroupDownload = false;
	
	private String groupDownloadTooltip = "";
	
	private String groupDownloadAlign = "right";
	
//	private boolean hasGbLink = false;
//	
//	private String gbTypeField = "";
//	
//	private String gbLinkField = "";
//	
//	private String gbTtpField = "";
//	
//	private String hideGbField = "";

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

//	/**
//	 * @return the hasDownload
//	 */
//	public boolean isHasDownload() {
//		return hasDownload;
//	}
//
//	/**
//	 * @param hasDownload the hasDownload to set
//	 */
//	public void setHasDownload(boolean hasDownload) {
//		this.hasDownload = hasDownload;
//	}
//
//	/**
//	 * @return the downloadLinkField
//	 */
//	public String getDownloadLinkField() {
//		return downloadLinkField;
//	}
//
//	/**
//	 * @param downloadLinkField the downloadLinkField to set
//	 */
//	public void setDownloadLinkField(String downloadLinkField) {
//		this.downloadLinkField = downloadLinkField;
//	}
//
//	/**
//	 * @return the downloadTooltip
//	 */
//	public String getDownloadTooltip() {
//		return downloadTooltip;
//	}
//
//	/**
//	 * @param downloadTooltip the downloadTooltip to set
//	 */
//	public void setDownloadTooltip(String downloadTooltip) {
//		this.downloadTooltip = downloadTooltip;
//	}
//
//	/**
//	 * @return the hideDownloadField
//	 */
//	public String getHideDownloadField() {
//		return hideDownloadField;
//	}
//
//	/**
//	 * @param hideDownloadField the hideDownloadField to set
//	 */
//	public void setHideDownloadField(String hideDownloadField) {
//		this.hideDownloadField = hideDownloadField;
//	}

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

//	/**
//	 * @return the hasGbLink
//	 */
//	public boolean isHasGbLink() {
//		return hasGbLink;
//	}
//
//	/**
//	 * @param hasGbLink the hasGbLink to set
//	 */
//	public void setHasGbLink(boolean hasGbLink) {
//		this.hasGbLink = hasGbLink;
//	}
//
//	/**
//	 * @return the gbTypeField
//	 */
//	public String getGbTypeField() {
//		return gbTypeField;
//	}
//
//	/**
//	 * @param gbTypeField the gbTypeField to set
//	 */
//	public void setGbTypeField(String gbTypeField) {
//		this.gbTypeField = gbTypeField;
//		
//		GridContent gridContent = (GridContent) this.getContent();
//		if (gridContent != null) {
//			List<GridDataField> fields = ((GridContent)gridContent).getDataFields();
//			for (int index = 0; index < fields.size(); index++) {
//				if (fields.get(index).getName().equals(this.gbTypeField)) {
//					List<List<String>> data = gridContent.getData();
//					for (int i = 0; i < data.size(); i++) {
//						List<String> datarow = data.get(i);
//						String type = datarow.get(index);
//						datarow.set(index, "icon-gb-" + type);
//						data.set(i, datarow);
//					}
//					break;
//				}
//			}
//		}
//	}
//
//	/**
//	 * @return the gbLinkField
//	 */
//	public String getGbLinkField() {
//		return gbLinkField;
//	}
//
//	/**
//	 * @param gbLinkField the gbLinkField to set
//	 */
//	public void setGbLinkField(String gbLinkField) {
//		this.gbLinkField = gbLinkField;
//	}
//
//	/**
//	 * @return the gbTtpField
//	 */
//	public String getGbTtpField() {
//		return gbTtpField;
//	}
//
//	/**
//	 * @param gbTtpField the gbTtpField to set
//	 */
//	public void setGbTtpField(String gbTtpField) {
//		this.gbTtpField = gbTtpField;
//	}
//
//	/**
//	 * @return the hideGbField
//	 */
//	public String getHideGbField() {
//		return hideGbField;
//	}
//
//	/**
//	 * @param hideGbField the hideGbField to set
//	 */
//	public void setHideGbField(String hideGbField) {
//		this.hideGbField = hideGbField;
//	}

	@Override
	public void setContent(Content gridContent) {
//		if (this.isHasGbLink() && !this.gbTypeField.isEmpty()) {
//			List<GridDataField> fields = ((GridContent)gridContent).getDataFields();
//			for (int index = 0; index < fields.size(); index++) {
//				if (fields.get(index).getName().equals(this.gbTypeField)) {
//					List<List<String>> data = ((GridContent) gridContent).getData();
//					for (int i = 0; i < data.size(); i++) {
//						List<String> datarow = data.get(i);
//						String type = datarow.get(index);
//						datarow.set(index, "icon-gb-" + type);
//						data.set(i, datarow);
//					}
//					break;
//				}
//			}
//		}
		
		// append actions to datarows if there's some action and actionRefSet is empty
		if (gridContent.getClass() == GridContent.class) {
			if (((GridContent)gridContent).getActionReferenceSet().isEmpty())
				if (!((GridContent)gridContent).getActions().isEmpty())
					((GridContent)gridContent).appendActionsToData();
		}
		
		super.setContent(gridContent);
	}

}
