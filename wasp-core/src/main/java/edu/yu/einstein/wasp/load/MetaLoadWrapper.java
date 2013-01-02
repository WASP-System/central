package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.exception.MetaClassNotFoundException;
import edu.yu.einstein.wasp.model.MetaBase;

public class MetaLoadWrapper {

	private String area = "";
	
	private Map<String,String> metaDataList = new HashMap<String, String>();

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setMetaList(List<String> metaStringList) {
		for(String metaString : metaStringList){
			String[] metaStringSplit= metaString.split("=");
			String key = metaStringSplit[0].trim();
			String value = "";
			for (int i=1; i< metaStringSplit.length; i++){
				value += metaStringSplit[i];
			}
			metaDataList.put(key, value);
		}
	}
	
	public void setMeta(String metaString) {
		List<String> metaStringList = new ArrayList<String>();
		metaStringList.add(metaString);
		setMetaList(metaStringList);
	}
	
	public void setMetaMap(Map<String, String> metaDataList) {
		this.metaDataList = metaDataList;
	}
	
	public Map<String, String> getMetaMap() {
		return metaDataList;
	}
	
	public <T extends MetaBase> List<T> getMeta(Class<T> clazz){
		List<T> metaList = new ArrayList<T>();
		try{
			for (String key : metaDataList.keySet()){
				T meta = clazz.newInstance();
				String k = area;
				if (!k.isEmpty())
					k += ".";
				k += key;
				meta.setK(k);
				meta.setV(metaDataList.get(key));
				metaList.add(meta);
			}
		} catch(Exception e){
			throw new MetaClassNotFoundException("Problem handling metadata. Suspect that there is no metadata class with name '"+ clazz.getName() +"'", e);
		}
		return metaList;
	}
}
