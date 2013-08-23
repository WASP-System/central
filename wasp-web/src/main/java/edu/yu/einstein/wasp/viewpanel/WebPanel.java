package edu.yu.einstein.wasp.viewpanel;

/**
 * Class for describing a generic vizualisation panel. Plugins may be asked to return a WebPanel object for displaying data.
 * The panel data may be used by any web rendering tool for presenting a panel within a view.
 * @author asmclellan
 *
 */
public class WebPanel extends Panel{

	private String execOnRenderCode = "";
	
	private String execOnResizeCode = "";
	
	private String execOnExpandCode = "";
	
	public WebPanel() {}
	
	public WebPanel(String title, Content content) {
		super(title, content);
	}

	public WebPanel(String title, String description, Integer width, Integer height, Integer order, Content content, boolean isResizable, boolean isMaximizable, boolean isCloseable) {
		super(title, description, width, height, order, content, isResizable, isMaximizable, isCloseable);
	}

	public WebPanel(String title, String description, Integer width, Integer height, Content content) {
		super(title, description, width, height, content);
	}
	
	/**
	 * Returns code that is to be executed when a panel is rendered. This might be a jQuery function for example. 
	 */
	public String getExecOnRenderCode() {
		return execOnRenderCode;
	}

	public void setExecOnRenderCode(String execOnRenderCode) {
		this.execOnRenderCode = execOnRenderCode;
	}

	/**
	 * Returns code that is to be executed when a panel is resized. This might be a jQuery function for example. 
	 * If not set but 'execOnRender' code is set, this code will be called on resize, otherwise returns an empty string.
	 */
	public String getExecOnResizeCode() {
		if (execOnResizeCode == null || execOnResizeCode.isEmpty())
			return execOnRenderCode;
		return execOnResizeCode;
	}

	public void setExecOnResizeCode(String execOnResizeCode) {
		this.execOnResizeCode = execOnResizeCode;
	}

	/**
	 * Returns code that is to be executed when a panel is expanded. This might be a jQuery function for example. 
	 * If not set but 'execOnRender' code is set, this code will be called on expanding, otherwise returns an empty string.
	 */
	public String getExecOnExpandCode() {
		if (execOnExpandCode == null || execOnExpandCode.isEmpty())
			return execOnRenderCode;
		return execOnExpandCode;
	}

	public void setExecOnExpandCode(String execOnExpandCode) {
		this.execOnExpandCode = execOnExpandCode;
	}

	

}
