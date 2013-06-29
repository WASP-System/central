package edu.yu.einstein.wasp.plugin;

import java.net.URL;
import java.util.Set;


public interface ViewPanel {
	
	public String getTitle();
	
	public String getDescription();
	
	public Integer getWidth();
	
	public Integer getHeight();

	public Integer getOrder();
	
	public Set<URL> getScriptDependencies();
	
	public Set<URL> getCssDependencies();

	public Object getContent();

	public boolean isResizable();

	public boolean isMaximizable();

	public boolean isCloseable();


}
