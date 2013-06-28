package edu.yu.einstein.wasp.plugin;


public interface ViewPanel {
	
	public String getTitle();
	
	public String getDescription();
	
	public Integer getWidth();
	
	public Integer getHeight();

	public Integer getOrder();

	public Object getContent();

	public boolean isResizable();

	public boolean isMaximizable();

	public boolean isCloseable();


}
