package edu.yu.einstein.wasp.viewpanel;

public class Action {

	public enum CallbackFunctionType {
		UNKNOWN, DOWNLOAD, OPEN_IN_CSS_WIN, OPEN_IN_NEW_BROWSER_WIN
	}

	public enum GroupActionAlignType {
		LEFT, RIGHT
	}

	private String iconClassName = "";

	private int icnHashCode = 0;

	private String tooltip = "";

	private String callbackContent = "";

	private CallbackFunctionType callbackFunctionType = CallbackFunctionType.UNKNOWN;

	private boolean group = false;

	private String groupIconClassName = "";

	private String groupTooltip = "";

	private GroupActionAlignType groupAlign = GroupActionAlignType.RIGHT;

	public Action() {
		super();
	}

	public Action(String iconClassName, String tooltip,
			CallbackFunctionType callbackFunctionType, String callbackContent) {
		super();
		this.iconClassName = iconClassName;
		this.icnHashCode = iconClassName.hashCode();
		this.tooltip = tooltip;
		this.callbackFunctionType = callbackFunctionType;
		this.setCallbackContent(callbackContent);
	}

	public Action(String iconClassName, String tooltip,
			CallbackFunctionType callbackFunctionType, String callbackContent,
			boolean group, String groupIconClassName) {
		super();
		this.iconClassName = iconClassName;
		this.icnHashCode = iconClassName.hashCode();
		this.tooltip = tooltip;
		this.callbackFunctionType = callbackFunctionType;
		this.setCallbackContent(callbackContent);
		this.group = group;
		this.groupIconClassName = groupIconClassName;
	}

	public Action(String iconClassName, String tooltip,
			CallbackFunctionType callbackFunctionType, String callbackContent,
			boolean group, String groupIconClassName, String groupTooltip,
			GroupActionAlignType groupAlign) {
		super();
		this.iconClassName = iconClassName;
		this.icnHashCode = iconClassName.hashCode();
		this.tooltip = tooltip;
		this.callbackFunctionType = callbackFunctionType;
		this.setCallbackContent(callbackContent);
		this.group = group;
		this.groupIconClassName = groupIconClassName;
		this.groupTooltip = groupTooltip;
		this.groupAlign = groupAlign;
	}

	/**
	 * get icon class name specified in css somewhere e.g. '.icon-group-download
	 * { background-image: url(ext/images/icons/fam/disk_multiple.png)
	 * !important;}'. Defaults to empty string
	 * 
	 * @return
	 */
	public String getIconClassName() {
		return iconClassName;
	}

	public int getIcnHashCode() {
		return icnHashCode;
	}

	/**
	 * Set icon class name specified in css somewhere e.g. '.icon-group-download
	 * { background-image: url(ext/images/icons/fam/disk_multiple.png)
	 * !important;}'.
	 * 
	 * @param iconClassName
	 */
	public void setIconClassName(String iconClassName) {
		this.iconClassName = iconClassName;
		this.icnHashCode = iconClassName.hashCode();
	}

	/**
	 * Gets value of tooltip. Defaults to empty string.
	 * 
	 * @return
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Sets value of tooltip.
	 * 
	 * @param tooltip
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Gets callback function type, e.g. UNKNOWN, DOWNLOAD, OPEN_IN_CSS_WIN,
	 * OPEN_IN_NEW_BROWSER_WIN
	 * 
	 * @return
	 */
	public CallbackFunctionType getCallbackFunctionType() {
		return callbackFunctionType;
	}

	/**
	 * Sets callback function type, e.g. UNKNOWN, DOWNLOAD, OPEN_IN_CSS_WIN,
	 * OPEN_IN_NEW_BROWSER_WIN
	 * 
	 * @param callbackFunctionType
	 */
	public void setCallbackFunctionType(
			CallbackFunctionType callbackFunctionType) {
		this.callbackFunctionType = callbackFunctionType;
	}

	/**
	 * Get the callback content to be handled. Handling may be dependent on
	 * callback function type. May be a URL or HTML etc. Defaults to empty
	 * string
	 * 
	 * @return
	 */
	public String getCallbackContent() {
		return callbackContent;
	}

	/**
	 * Set the callback content to be handled. Handling may be dependent on
	 * callback function type. May be a URL or HTML etc.
	 * 
	 * @param callbackContent
	 */
	public void setCallbackContent(String callbackContent) {
		this.callbackContent = callbackContent;
	}

	/**
	 * @return the group
	 */
	public boolean isGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(boolean group) {
		this.group = group;
	}

	/**
	 * @return the groupIconClassName
	 */
	public String getGroupIconClassName() {
		return groupIconClassName;
	}

	/**
	 * @param groupIconClassName
	 *            the groupIconClassName to set
	 */
	public void setGroupIconClassName(String groupIconClassName) {
		this.groupIconClassName = groupIconClassName;
	}

	/**
	 * @return the groupTooltip
	 */
	public String getGroupTooltip() {
		return groupTooltip;
	}

	/**
	 * @param groupTooltip
	 *            the groupTooltip to set
	 */
	public void setGroupTooltip(String groupTooltip) {
		this.groupTooltip = groupTooltip;
	}

	/**
	 * @return the groupAlign
	 */
	public GroupActionAlignType getGroupAlign() {
		return groupAlign;
	}

	/**
	 * @param groupAlign
	 *            the groupAlign to set
	 */
	public void setGroupAlign(GroupActionAlignType groupAlign) {
		this.groupAlign = groupAlign;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!this.getClass().isInstance(obj)
				&& !obj.getClass().isInstance(this))
			return false; // allow comparison if one class is derived from the
							// other
		return iconClassName.equals(((Action) obj).getIconClassName()); 
	}

	@Override
	public int hashCode() {
		return iconClassName.hashCode();
	}

}
