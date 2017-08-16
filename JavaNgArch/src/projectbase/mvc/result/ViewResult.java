package projectbase.mvc.result;

import javax.servlet.http.HttpServletRequest;

import projectbase.mvc.GlobalConstant;
import projectbase.mvc.UrlHelper;
import projectbase.utils.Res;

/**
 * to provide a JstlView(jsp) along with ViewModel and Helper Objects like in Asp.Net mvc3
 * so no need to use other databinding tags in the jsp file
 * instead,use those helpers to do a good job 
 * @author tudoubaby
 *
 */
public class ViewResult extends ActionResult {
	public ViewResult LoadHelpers(HttpServletRequest req,Class<?> controllerClass){
		req.setAttribute(GlobalConstant.Attr_UrlHelper, new UrlHelper(req));
		//this line is replaced by angular
		//req.setAttribute(GlobalConstant.Attr_ResHelper, new Res());
		UrlHelper.ParsePathInfo(req);
		SetLangContext(req);
		return this;
		
	}
	public ViewResult(String viewName, Object modelObject) {
		super(viewName, modelObject);
	}
	public ViewResult(String viewName) {
		super(viewName,null);
	}
	

}
