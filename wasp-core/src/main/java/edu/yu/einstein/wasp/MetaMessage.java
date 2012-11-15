package edu.yu.einstein.wasp;


public class MetaMessage{
	
	private String group;
	
	private String name;
	
	private String value;
	
	private String uniqueKey;

	public MetaMessage(String uniqueKey, String group, String name, String value) {
		this.uniqueKey = uniqueKey;
		this.group = group;
		this.name = name;
		this.value = value;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return "[ uniqueKey=" + uniqueKey + ", group=" + group + ", name=" + name + ", value=" + value + " ]";
	}
	
}