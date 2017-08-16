package projectbase.mvc.result;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;

import projectbase.domain.DictEnum;

public class JavaScriptSerializer {
	private static ObjectMapper om=new ObjectMapper();
	static{
		//日期时间在Json中统一序列化为字符串，统一格式
//		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//		om.setDateFormat(fmt);  
		SimpleModule module = new SimpleModule();  
		module.addSerializer(DictEnum.class, new DictEnumJsSerializer());  
		om.registerModule(module);  
		//使json对属性名处理与iavaBean一致
		om.setPropertyNamingStrategy(new PropertyNamingStrategy.PropertyNamingStrategyBase () {
	        @Override
	        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
	        {
	        	String name=method.getName();
	        	if(name.startsWith("get")&&StringUtils.isAllUpperCase(name.substring(4,5))) {
	        		name=name.substring(3);
	        	}else if(name.startsWith("is")&&StringUtils.isAllUpperCase(name.substring(3,4))){
	        		name=name.substring(2);
	        	}else{
	        		name=defaultName;
	        	}
	        		
	            return translate(name);
	        }	 
		    @Override
		    public String translate(String name) {
		        return name;
		    }
		 
		});
	}
	public String Serialize(Object data){
		try{
			return om.writeValueAsString(data);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	

}
