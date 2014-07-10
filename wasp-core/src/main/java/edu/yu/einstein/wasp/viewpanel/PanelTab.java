package edu.yu.einstein.wasp.viewpanel;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a tab display component which acts as a container for displaying a group of Panel objects
 * @author asmclellan
 *
 */
public class PanelTab {
	
	private Set<Panel> panels = new LinkedHashSet<>();
	
	private String name = "";
	
	private String description = "";
	
	private int numberOfColumns = 2; //default
	
	private boolean maxOnLoad = false;
	
	public PanelTab(){}
	
	public PanelTab(Set<Panel> panels, String name, int numberOfColumns) {
		this.panels = panels;
		this.name = name;
		this.numberOfColumns = numberOfColumns;
	}

	public Set<Panel> getPanels() {
		return panels;
	}

	public void setPanels(Set<Panel> panels) {
		this.panels = panels;
	}
	
	public void addPanel(Panel panel) {
		this.panels.add(panel);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
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
