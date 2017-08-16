package projectbase.mvc;
/**
 * represent a mvc command
 * @author test
 *
 */
public class Action {
	private String areaName;
	private String controllerName;
	private String actionName;
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String value) {
		this.areaName = value;
	}
	public String getControllerName() {
		return controllerName;
	}
	public void setControllerName(String value) {
		this.controllerName = value;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String value) {
		this.actionName = value;
	}
	public String getPathInfo() {
		return "/"+areaName+"/"+controllerName+"/"+actionName;
	}

}
