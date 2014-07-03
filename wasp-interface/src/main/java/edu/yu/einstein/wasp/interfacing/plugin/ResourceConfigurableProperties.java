package edu.yu.einstein.wasp.interfacing.plugin;

import java.util.HashMap;
import java.util.Map;

public class ResourceConfigurableProperties extends HashMap<String, Object> {

	private static final long serialVersionUID = 3050958366226295318L;
	
	private Map<String, String> i18nMessageKeys = new HashMap<>();

	public ResourceConfigurableProperties() {
	
	}

	public ResourceConfigurableProperties(int initialCapacity) {
		super(initialCapacity);
	}

	public ResourceConfigurableProperties(Map<? extends String, ? extends Object> m) {
		super(m);
	}

	public ResourceConfigurableProperties(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	public String getI18nMessageKey(String key){
		if (i18nMessageKeys.containsKey(key))
			return i18nMessageKeys.get(key);
		return key;
					
	}
	
	public void addI18nMessageKey(String key, String i18nMessageKey) throws Exception{
		if (!this.containsKey(key))
			throw new Exception("attempting to set i18nMessageKey on non-existent key");
		i18nMessageKeys.put(key, i18nMessageKey);
	}
	
}
