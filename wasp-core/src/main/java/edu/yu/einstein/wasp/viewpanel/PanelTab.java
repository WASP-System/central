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
	
	private String tabTitle = "";
	
	private String description = "";
	
	private int numberOfColumns = 2; //default
	
	public PanelTab(){}
	
	public PanelTab(Set<Panel> panels, String tabTitle, int numberOfColumns) {
		this.panels = panels;
		this.tabTitle = tabTitle;
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

	public String getTabTitle() {
		return tabTitle;
	}

	public void setTabTitle(String name) {
		this.tabTitle = name;
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
}
