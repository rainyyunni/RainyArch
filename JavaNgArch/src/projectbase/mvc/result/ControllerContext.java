package projectbase.mvc.result;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerContext {
	private HttpServletResponse response;
	private HttpServletRequest request;
	private Object controller;
	private Map<String, ?> viewData;
	public Object getController() {
		return controller;
	}
	public void setController(Object value) {
		this.controller = value;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest value) {
		this.request = value;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse value) {
		this.response = value;
	}
	public ControllerContext() {

	}
	public Map<String, ?> getViewData() {
		return viewData;
	}
	public void setViewData(Map<String, ?> value) {
		this.viewData = value;
	}
}

