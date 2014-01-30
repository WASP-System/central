package edu.yu.einstein.wasp.controller.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class ExtTreeGridResponseSerializer extends JsonSerializer<ExtTreeGridResponse> {
	
	  public void serialize(ExtTreeGridResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeStartObject();
	    jgen.writeObject(value.getModelList());
	    jgen.writeObjectField("totalCount", value.getTotalCount());
	    jgen.writeEndObject();
	  }

	  public ExtTreeGridResponseSerializer() {
		super();
	  }
	
}
