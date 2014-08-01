package edu.yu.einstein.wasp.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ExtTreeModel extends ExtModel{
	
	private static final long serialVersionUID = 4827904024619482123L;

	public static class ExtIcon implements Serializable{
		
		private static final long serialVersionUID = -7359308603187815929L;
		
		public static final ExtIcon TASK_FOLDER = new ExtIcon("task-folder");
		public static final ExtIcon TASK = new ExtIcon("task");
		
		private final String iconName;
		
		public String getIconName() {
			return iconName;
		}

		public ExtIcon(String name) {this.iconName = name;}
		
		@Override
		public String toString(){ return iconName; }
		
	}
	
	private String id = "";
	
	private String iconCls = ExtIcon.TASK_FOLDER.toString(); // default
	
	private boolean isLeaf = false;
	
	private boolean isExpanded = false;
	
	private List<ExtTreeModel> children = null; // return null if no children rather than empty list otherwise Ext JS tree does not show arrows to expand node
	
	public ExtTreeModel(){}
	
	public ExtTreeModel(String id, ExtIcon iconCls, boolean isExpanded, boolean isLeaf){
		this.id = id;
		this.iconCls = iconCls.toString();
		this.isExpanded = isExpanded;
		this.isLeaf = isLeaf;
	}
	
	@JsonIgnore
	public void addChild(ExtTreeModel model){
		if (this.children == null)
			this.children = new ArrayList<>();
		this.children.add(model);
	}
	
	@JsonIgnore
	public void addChildren(List<ExtTreeModel> children){
		if (this.children == null)
			this.children = new ArrayList<>();
		this.children.addAll(children);
	}
	
	public List<ExtTreeModel> getChildren(){
		return this.children;
	}
	
	public void setChildren(List<ExtTreeModel> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(ExtIcon iconCls) {
		this.iconCls = iconCls.toString();
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	
}
