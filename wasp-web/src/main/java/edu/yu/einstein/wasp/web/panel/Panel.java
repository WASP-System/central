package edu.yu.einstein.wasp.web.panel;

import edu.yu.einstein.wasp.plugin.ViewPanel;

/**
 * Class for describing a generic vizualisation panel. Plugins may be asked to return a Panel object for displaying data.
 * The panel data may be used by any web rendering tool for presenting a panel within a view.
 * @author asmclellan
 *
 */
public class Panel implements ViewPanel{

	private String title = "";
	
	private String description = "";
	
	private WebContent content;
	
	private Integer width;
	
	private Integer height;
	
	private Integer order;
	
	private String execOnRenderCode = "";
	
	private String execOnResizeCode = "";
	
	private String execOnExpandCode = "";
	
	private boolean isResizable = false;
	
	private boolean isMaximizable = true;
	
	private boolean isCloseable = false;
	
	public Panel() {}
	
	public Panel(String title, WebContent content) {
		this.title = title;
		this.content = content;
	}

	public Panel(String title, String description, Integer width,
			Integer height, Integer order, WebContent content,
			boolean isResizable, boolean isMaximizable, boolean isCloseable) {
		this.title = title;
		this.description = description;
		this.width = width;
		this.height = height;
		this.order = order;
		this.content = content;
		this.isResizable = isResizable;
		this.isMaximizable = isMaximizable;
		this.isCloseable = isCloseable;
	}

	public Panel(String title, String description, Integer width,
			Integer height, WebContent content) {
		this.title = title;
		this.description = description;
		this.width = width;
		this.height = height;
		this.content = content;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebContent getContent() {
		return content;
	}

	public void setContent(WebContent content) {
		this.content = content;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResizable() {
		return isResizable;
	}

	public void setResizable(boolean isResizable) {
		this.isResizable = isResizable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaximizable() {
		return isMaximizable;
	}

	public void setMaximizable(boolean isMaximizable) {
		this.isMaximizable = isMaximizable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCloseable() {
		return isCloseable;
	}

	public void setCloseable(boolean isCloseable) {
		this.isCloseable = isCloseable;
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
