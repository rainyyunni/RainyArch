package projectbase.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
/**
 * for using tiles,now no use because of angular
 * @author tudoubaby
 *
 */
public class RequestToSuffixedViewNameTranslator extends DefaultRequestToViewNameTranslator{
	public static String Attr_ViewNameSuffix="ViewNameSuffixAsMasterName";
	@Override
	public String getViewName(HttpServletRequest request) {
		String viewName=super.getViewName(request);
		String suffix=(String)request.getAttribute(Attr_ViewNameSuffix);
		if(suffix!=null){
			viewName=viewName+suffix;
		}
		return viewName;
	}

}
