package edu.yu.einstein.wasp;

public class MetaMessage{
	
	private String key;
	
	private String name;
	
	private String value;

	public MetaMessage(String key, String name, String value) {
		this.key = key;
		this.name = name;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
	
}