package projectbase.mvc;

import org.springframework.beans.SimpleTypeConverter;
import org.springframework.core.convert.ConversionService;

public class ChildActionParamConverter extends SimpleTypeConverter {
	private static ConversionService conversionService=BaseMvcApplication.getSpringContainer().getBean(ConversionService.class);
	
	public ChildActionParamConverter(){
		this.setConversionService(conversionService);
	}
}
