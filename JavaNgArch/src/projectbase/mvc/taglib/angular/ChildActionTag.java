package projectbase.mvc.taglib.angular;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import projectbase.mvc.Action;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.UrlHelper;

public class ChildActionTag extends org.apache.taglibs.standard.tag.el.core.ImportTag {

private String action;
private String controller;
private String area;
private Object param;
private Map<String,Object> paramMap;
private List<Object> paramList;

public String getAction() {
	return action;
}

public void setAction(String value) {
	this.action = value;
}
public List<Object> getParamList() {
	return paramList;
}

public void setParamList(List<Object> paramList) {
	this.paramList = paramList;
}

public String getController() {
	return controller;
}

public void setController(String controller) {
	this.controller = controller;
}

public String getArea() {
	return area;
}

public void setArea(String area) {
	this.area = area;
}
public Map<String,Object> getParamPap() {
	return paramMap;
}

public void setParamMap(Map<String,Object> value) {
	this.paramMap = value;
}
public Object getParam() {
	return param;
}

public void setParam(Object param) {
	this.param = param;
}

	@Override
	public int doStartTag() throws JspException {
		UrlHelper urlHelper=(UrlHelper)pageContext.getAttribute(GlobalConstant.Attr_UrlHelper, PageContext.REQUEST_SCOPE);
		String url=urlHelper.Action(GlobalConstant.MappingPath_ChildAction);
		setUrl(url);

		Map<String,Object> map=new Hashtable<String,Object>();
		Action childAction=new Action();
		childAction.setActionName(action);
		childAction.setControllerName(controller);
		childAction.setAreaName(area);
		map.put(GlobalConstant.Key_ChildAction, childAction);
		List<Object> params=new ArrayList<Object>();
		if(paramList!=null){
			params.addAll(paramList);
		}else if(param!=null){
			params.add(param);
		}
		map.put(GlobalConstant.Key_ChildActionParameters, params);
		pageContext.setAttribute(GlobalConstant.Attr_ChildActionMethodInfo, map,PageContext.REQUEST_SCOPE);
		return super.doStartTag();
	}

	@Override
	public void doFinally() {
		Stack<Object> viewModelStack=(Stack<Object>)pageContext.getAttribute(GlobalConstant.Attr_ViewModelStack, PageContext.REQUEST_SCOPE);
		viewModelStack.pop();
		Object includingModel=viewModelStack.peek();
		pageContext.setAttribute(GlobalConstant.Attr_ViewModel, includingModel,PageContext.REQUEST_SCOPE);
		
		Stack<Action> actionStack=(Stack<Action>)pageContext.getAttribute(GlobalConstant.Attr_ActionStack, PageContext.REQUEST_SCOPE);
		actionStack.pop();
		Action	includingAction=actionStack.peek();
		pageContext.setAttribute(GlobalConstant.Attr_RequestAction, includingAction,PageContext.REQUEST_SCOPE);
		super.doFinally();
	}


	






}
