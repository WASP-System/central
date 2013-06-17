package edu.yu.einstein.wasp.plugin;

public interface ViewPanel {
	
	public String getTitle();
	
	public void setTitle(String title);
	
	public String getDescription();
	
	public void setDescription(String description);

	public Integer getWidth();
	
	public void setWidth(Integer width);

	public Integer getHeight();

	public void setHeight(Integer height);

	public Integer getOrder();

	public void setOrder(Integer order);

	public Object getContent();

	public void setContent(Object content);

	public boolean isResizable();

	public void setResizable(boolean isResizable);

	public boolean isMaximizable();

	public void setMaximizable(boolean isMaximizable);

	public boolean isCloseable();

	public void setCloseable(boolean isCloseable);

}
