
package projectbase.mvc;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;


    public class AutoMapperProfile  implements TypeFilter{

    	public static ModelMapper DefaultMapper=new ModelMapper();	
    	private static List<Class<?>> dictEnumconverterClasses=new ArrayList<Class<?>>();

    	@Override
    	public boolean match(MetadataReader metadataReader,
    			MetadataReaderFactory metadataReaderFactory) throws IOException {

    		/*auto scan all subclasses of DictEnum 
    		*/
    		String qclassname = metadataReader.getClassMetadata().getClassName();
    		if (qclassname.endsWith("Enum")) {
    			try {
					dictEnumconverterClasses.add(Class.forName(qclassname));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
    		}
    		return false;
    	}


        public static void Configure()
        {

        	//DefaultMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        	for(Class<?> clazz:dictEnumconverterClasses){
        		try {
        			Class<?>[] all=clazz.getInterfaces();
        			if(all!=null && all.length>0 && all[0]==Converter.class){
        				DefaultMapper.addConverter((Converter<?,?>)clazz.newInstance());
        			}
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
        	}

       }
     }


