package edu.yu.einstein.wasp.viewpanel;

public class Action {
	
	public enum CallbackFunctionType { UNKNOWN, DOWNLOAD, OPEN_IN_CSS_WIN, OPEN_IN_NEW_BROWSER_WIN }
	
	private String iconClassName = "";
	
	private int icnHashCode = 0;
	
	private String tooltip = "";
	
	private String callbackContent = "";
	
	private CallbackFunctionType callbackFunctionType = CallbackFunctionType.UNKNOWN;
	
	public Action(){
		super();
	}
	
	public Action(String iconClassName, String tooltip, CallbackFunctionType callbackFunctionType, String callbackContent) {
		super();
		this.iconClassName = iconClassName;
		this.icnHashCode = iconClassName.hashCode();
		this.tooltip = tooltip;
		this.callbackFunctionType = callbackFunctionType;
		this.setCallbackContent(callbackContent);
	}

	/**
	 * get icon class name specified in css somewhere e.g. '.icon-group-download { background-image: url(ext/images/icons/fam/disk_multiple.png) !important;}'.
	 * Defaults to empty string
	 * @return
	 */
	public String getIconClassName() {
		return iconClassName;
	}

	public int getIcnHashCode() {
		return icnHashCode;
	}

	/**
	 * Set icon class name specified in css somewhere e.g. '.icon-group-download { background-image: url(ext/images/icons/fam/disk_multiple.png) !important;}'.
	 * @param iconClassName
	 */
	public void setIconClassName(String iconClassName) {
		this.iconClassName = iconClassName;
		this.icnHashCode = iconClassName.hashCode();
	}

	/**
	 * Gets value of tooltip. Defaults to empty string.
	 * @return
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Sets value of tooltip.
	 * @param tooltip
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Gets callback function type, e.g. UNKNOWN, DOWNLOAD, OPEN_IN_CSS_WIN, OPEN_IN_NEW_BROWSER_WIN
	 * @return
	 */
	public CallbackFunctionType getCallbackFunctionType() {
		return callbackFunctionType;
	}

	/**
	 * Sets callback function type, e.g. UNKNOWN, DOWNLOAD, OPEN_IN_CSS_WIN, OPEN_IN_NEW_BROWSER_WIN
	 * @param callbackFunctionType
	 */
	public void setCallbackFunctionType(CallbackFunctionType callbackFunctionType) {
		this.callbackFunctionType = callbackFunctionType;
	}

	/**
	 * Get the callback content to be handled. Handling may be dependent on callback function type. May be a URL or HTML etc. 
	 * Defaults to empty string
	 * @return
	 */
	public String getCallbackContent() {
		return callbackContent;
	}

	/**
	 * Set the callback content to be handled. Handling may be dependent on callback function type. May be a URL or HTML etc. 
	 * @param callbackContent
	 */
	public void setCallbackContent(String callbackContent) {
		this.callbackContent = callbackContent;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null) return false;
		if (!this.getClass().isInstance(obj) && !obj.getClass().isInstance(this)) 
			return false; // allow comparison if one class is derived from the other
		return iconClassName.equals(((Action) obj).getIconClassName()); // iconClassName should be the same
	}
	
	@Override
	public int hashCode(){
		return iconClassName.hashCode();
	}

}
