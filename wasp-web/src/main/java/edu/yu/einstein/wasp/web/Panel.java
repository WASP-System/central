package edu.yu.einstein.wasp.web;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

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
	
	private String htmlContent = "";
	
	private Set<URL> scriptDependencies = new HashSet<URL>();
	
	private Set<URL> cssDependencies = new HashSet<URL>();
	
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
	public Object getContent() {
		return htmlContent;
	}

	public void setContent(Object content) {
		this.htmlContent = (String) content;
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
	
	public void setScriptDependencies(Set<URL> dependencies){
		this.scriptDependencies = dependencies;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<URL> getScriptDependencies() {
		return scriptDependencies;
	}
	
	public void setCssDependencies(Set<URL> dependencies){
		this.cssDependencies = dependencies;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<URL> getCssDependencies() {
		return cssDependencies;
	}

}
