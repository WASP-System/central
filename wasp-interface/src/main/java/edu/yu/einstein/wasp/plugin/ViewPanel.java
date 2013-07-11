package edu.yu.einstein.wasp.plugin;



public interface ViewPanel {
	
	public String getTitle();
	
	public String getDescription();
	
	public Integer getWidth();
	
	public Integer getHeight();

	public Integer getOrder();
	
	public Object getContent();
	
	public String getExecOnRenderCode();
	
	public String getExecOnResizeCode();
	
	public String getExecOnExpandCode();

	public boolean isResizable();

	public boolean isMaximizable();

	public boolean isCloseable();


}
