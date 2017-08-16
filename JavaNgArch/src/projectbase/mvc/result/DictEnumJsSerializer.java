package projectbase.mvc.result;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import projectbase.domain.DictEnum;

public class DictEnumJsSerializer extends JsonSerializer<DictEnum> {  
	   
	@Override
	public void serialize(DictEnum value, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeNumber(value.getIntValue());
		
	}  
} 
