package projectbase.data.hibernatemapbycode.convention;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import projectbase.domain.BaseDomainObjectWithTypedId;

public class DOClassAnnotationAppender implements TypeFilter { 

	public static Logger log = Logger.getLogger(DOClassAnnotationAppender.class);
	private AnnotatingConvention convention=new AnnotatingConvention();
	public static List<Class<? extends BaseDomainObjectWithTypedId<Integer>>> annotatedDoClasses=new ArrayList<Class<? extends BaseDomainObjectWithTypedId<Integer>>>();
	private static List<String> doClasses = new ArrayList<String>();

	public static List<String> getDoClasses() {
		return doClasses;
	}
	public static List<Class<? extends BaseDomainObjectWithTypedId<Integer>>> getAnnotatedDoClasses() {
		return annotatedDoClasses;
	}
	@Override
	public boolean match(MetadataReader metadataReader,
			MetadataReaderFactory metadataReaderFactory) throws IOException {
		/*auto scan and find all do class names
		*/
		String qclassname = metadataReader.getClassMetadata().getClassName();
			
			doClasses.add(qclassname);
			try{
				Class<? extends BaseDomainObjectWithTypedId<Integer>> annotated=convention.GetAnnotatedClass(qclassname);
				if(annotated!=null)	annotatedDoClasses.add(annotated);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			
		return false;
	}	
	
}
