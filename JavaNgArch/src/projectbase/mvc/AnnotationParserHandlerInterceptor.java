package projectbase.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public abstract class AnnotationParserHandlerInterceptor extends HandlerInterceptorAdapter {
	public abstract Annotation getAnnotaion();
	public abstract void setAnnotaion(Annotation an);
	/**
	 * the annotation class this class parses
	 * @return
	 */
	public abstract Class<? extends Annotation> getAnnotaionClass();
	/**
	 * when the annotation is absent from action,should use annotation on controller class as default.
	 * @return
	 */
	public boolean ShouldParseControllerDefault(){
		return false;
	}
	
	protected String getControllerActionName(HttpServletRequest request,
			HttpServletResponse response, Object handler)
    {
		Method m=((HandlerMethod)handler).getMethod();
		return StringUtils.removeEnd(m.getDeclaringClass().getSimpleName(), "Controller")+"."+m.getName();
    }
	
	
}
