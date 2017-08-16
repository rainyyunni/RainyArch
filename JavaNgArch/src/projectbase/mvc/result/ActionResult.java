package projectbase.mvc.result;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import projectbase.domain.DictEnum;
import projectbase.mvc.DisplayExtension;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.UrlHelper;
import projectbase.utils.Util;

public class ActionResult extends ModelAndView implements IActionResult,View{
	private Object controller;
	public Object getController() {
		return controller;
	}
	public ActionResult setController(Object value) {
		this.controller = value;
		return this;
	}
	public ActionResult() {
		super();
	}
	public ActionResult(String viewName) {
		super(viewName);
	}
	public ActionResult(String viewName, Map<String, ?> model) {
		super(viewName,model);
	}
	public ActionResult(String viewName, Object modelObject) {
		super(viewName,GlobalConstant.Attr_ViewModel, modelObject);
	}
	public ActionResult(View view) {
		super(view);
	}
	public ActionResult(View view, Map<String, ?> model) {
		super(view,model);
	}
	public ActionResult(View view, String modelName, Object modelObject) {
		super(view,modelName,modelObject);
	}
	public void ExecuteResult(ControllerContext context)throws IOException{
		
	}
	@Override
	public String getContentType() {
		return "text/html; charset=UTF-8";
	}
	/**
	 * note that this render method only be called for non-viewresult actionresult,
	 * cause a viewresult will be resolved by viewname to jspview and rendered by the viewengine instead of this render method 
	 * when this.view=null,jspview will be applied as default view so make sure to set this.view=this so that this render method will be called
	 */
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UrlHelper.ParsePathInfo(request);
		SetLangContext(request);
		ControllerContext context=new ControllerContext();
		context.setRequest(request);
		context.setResponse(response);
		context.setController(controller);
		context.setViewData(model);
		ExecuteResult(context);
		
	}
	
	protected void SetLangContext(HttpServletRequest request){
		request.setAttribute(GlobalConstant.Attr_DisplayExtension, new DisplayExtension());
		request.setAttribute(GlobalConstant.Attr_DictMap, Util.DictMap());
		request.setAttribute(GlobalConstant.Attr_EnumMap, DictEnum.getEnumMap());
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ActionResult: ");
		if (isReference()) {
			sb.append("reference to view with name '").append(this.getViewName()).append("'");
		}
		else {
			sb.append("materialized View is [").append(this.getClass()).append(']');
		}
		sb.append("; model is ").append(this.getModel());
		return sb.toString();
	}

}
