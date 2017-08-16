package projectbase.mvc;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class WebConfigurationManager {

	public static String AppSettings(String paramName){
		return ((AnnotationConfigWebApplicationContext)BaseMvcApplication.getSpringContainer()).getServletContext().getInitParameter(paramName);
	}
}
