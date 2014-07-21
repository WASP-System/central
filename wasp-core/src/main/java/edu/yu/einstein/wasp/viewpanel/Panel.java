package edu.yu.einstein.wasp.viewpanel;

/**
 * Class for describing a generic vizualisation panel. Plugins may be asked to return a Panel object for displaying data.
 * The panel data may be used by any web rendering tool for presenting a panel within a view.
 * @author asmclellan
 *
 */
public class Panel {

	private String title = "";
	
	private String description = "";
	
	private Content content = null;
	
	private Integer width;
	
	private Integer height;
	
	private Integer order;
	
	private boolean resizable = false;
	
	private boolean maximizable = true;
	
	private boolean closeable = false;
	
	private boolean maxOnLoad = false;
	
	public Panel() {}
	
	public Panel(String title, Content content) {
		this.title = title;
		this.content = content;
	}

	public Panel(String title, String description, Integer width, Integer height, Integer order, Content content,
			boolean resizable, boolean maximizable, boolean closeable) {
		this.title = title;
		this.description = description;
		this.width = width;
		this.height = height;
		this.order = order;
		this.content = content;
		this.resizable = resizable;
		this.maximizable = maximizable;
		this.closeable = closeable;
	}

	public Panel(String title, String description, Integer width, Integer height, Content content) {
		this.title = title;
		this.description = description;
		this.width = width;
		this.height = height;
		this.content = content;
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

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public boolean isMaximizable() {
		return maximizable;
	}

	public void setMaximizable(boolean maximizable) {
		this.maximizable = maximizable;
	}

	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	/**
	 * @return the maxOnLoad
	 */
	public boolean isMaxOnLoad() {
		return maxOnLoad;
	}

	/**
	 * @param maxOnLoad the maxOnLoad to set
	 */
	public void setMaxOnLoad(boolean maxOnLoad) {
		this.maxOnLoad = maxOnLoad;
	}
	

}
