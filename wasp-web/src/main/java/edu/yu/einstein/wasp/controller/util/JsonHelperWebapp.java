package edu.yu.einstein.wasp.controller.util;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import edu.yu.einstein.wasp.model.WaspModel;


public class JsonHelperWebapp {
	
	public static <T extends WaspModel> T constructInstanceFromJson(String jsonString, Class<T> clazz) throws Exception{
		
		//convert jsonString to some object (if attributes are present that are not part of the class, they will be ignored due to the om.configure setting)
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);//http://stackoverflow.com/questions/4486787/jackson-with-json-unrecognized-field-not-marked-as-ignorable
		T obj = om.readValue(jsonString, clazz);
		return obj;
	}
	public static Map<String,String> constructMapFromJson(String jsonString) throws Exception{
		
		ObjectMapper om = new ObjectMapper();
		Map<String,String> map = om.readValue(jsonString, new TypeReference<HashMap<String,String>>() { });
		return map;
	}
}