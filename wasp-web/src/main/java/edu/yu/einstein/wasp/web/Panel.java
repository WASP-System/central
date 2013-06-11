package edu.yu.einstein.wasp.web;

/**
 * Class for describing a generic vizualisation panel. Plugins may be asked to return a Panel object for displaying data.
 * The panel data may be used by any web rendering tool for presenting a panel within a view.
 * @author asmclellan
 *
 */
public class Panel {

	private String title = "";
	
	private String description = "";
	
	private String htmlContent = "";
	
	private Integer width;
	
	private Integer height;
	
	private Integer order;
	
	private boolean isResizable = false;
	
	private boolean isMaximizable = true;
	
	private boolean isCloseable = false;
	
	public Panel() {}
	
	public Panel(String title, String htmlContent) {
		this.title = title;
		this.htmlContent = htmlContent;
	}

	public Panel(String title, String description, Integer width,
			Integer height, Integer order, String htmlContent,
			boolean isResizable, boolean isMaximizable, boolean isCloseable) {
		this.title = title;
		this.description = description;
		this.width = width;
		this.height = height;
		this.order = order;
		this.htmlContent = htmlContent;
		this.isResizable = isResizable;
		this.isMaximizable = isMaximizable;
		this.isCloseable = isCloseable;
	}

	public Panel(String title, String description, Integer width,
			Integer height, String htmlContent) {
		this.title = title;
		this.description = description;
		this.width = width;
		this.height = height;
		this.htmlContent = htmlContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public boolean isResizable() {
		return isResizable;
	}

	public void setResizable(boolean isResizable) {
		this.isResizable = isResizable;
	}

	public boolean isMaximizable() {
		return isMaximizable;
	}

	public void setMaximizable(boolean isMaximizable) {
		this.isMaximizable = isMaximizable;
	}

	public boolean isCloseable() {
		return isCloseable;
	}

	public void setCloseable(boolean isCloseable) {
		this.isCloseable = isCloseable;
	}

}
