package projectbase.mvc;

import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import projectbase.utils.ArgumentNullException;
import projectbase.utils.Util;

public class UrlHelper {
	private HttpServletRequest request;
	private boolean usePrefix=true;

	public UrlHelper(HttpServletRequest req) {
        if (req == null) {
            throw new ArgumentNullException("requestContext");
        }
		this.request=req;
	}
	public UrlHelper(HttpServletRequest req,boolean usePrefix) {
        this(req);
		this.usePrefix=usePrefix;
	}
	public static void ParsePathInfo(HttpServletRequest request){
		if(request.getAttribute(GlobalConstant.Attr_RequestAction)!=null) return;
		String[] pathInfo=request.getPathInfo().split("/");
		Action action=new Action();
		action.setAreaName(pathInfo[pathInfo.length-3]);
		action.setControllerName(pathInfo[pathInfo.length-2]);
		action.setActionName(pathInfo[pathInfo.length-1]);
		BuildActionStack(request,action);

	}
	public static void BuildActionStack(HttpServletRequest request,Action action){
		Stack<Action> stack=(Stack<Action>)request.getAttribute(GlobalConstant.Attr_ActionStack);
		if(stack==null){
			stack=new Stack<Action>();
			request.setAttribute(GlobalConstant.Attr_ActionStack,stack);
		}
		stack.push(action);
		request.setAttribute(GlobalConstant.Attr_RequestAction, action);
	}
	public static String GetPathInfo(HttpServletRequest request){
		Action action=(Action)request.getAttribute(GlobalConstant.Attr_RequestAction);
		if(action==null) return null;
		return action.getPathInfo();
	}
    public String Action(String actionName) {
        return GenerateUrl(actionName, null, (Map<String,Object>)null /* routeValues */);
    }

    public String Action(String actionName, Object routeValues) {
    	return GenerateUrl( actionName, null /* controllerName */, routeValues);
    }

    public String Action(String actionName, Map<String,Object> routeValues) {
        return GenerateUrl( actionName, null /* controllerName */, routeValues);
    }

    public String Action(String actionName, String controllerName) {
        return GenerateUrl(actionName, controllerName, (Map<String,Object>)null /* routeValues */);
    }
    public String Action(String actionName, String controllerName,Object routeValues) {
        return GenerateUrl(actionName, controllerName, routeValues );
    }
    public String Action(String actionName, String controllerName,Map<String,Object> routeValues) {
        return GenerateUrl(actionName, controllerName, routeValues );
    }
    public String State(String sref) {
    	return State( sref, null /* controllerName */, null);
    }

    public String State(String sref, Object routeValues) {
    	return State( sref, null /* controllerName */, routeValues);
    }

    public String State(String sref, Map<String,Object> routeValues) {
        return State( sref, null /* controllerName */, routeValues);
    }

    public String State(String sref, String controllerName) {
        return State(sref, controllerName, (Map<String,Object>)null /* routeValues */);
    }
    public String State(String sref, String controllerName,Object routeValues) {
    	Map<String,Object> routeValuesMap=Util.ObjectToMap(routeValues);
        return State(sref, controllerName, routeValuesMap );
    }
    public String State(String sref, String controllerName,Map<String,Object> routeValues) {
    	String nav=null;
    	String stateName=null;
    	String param="";
    	int pos=0;
    	if(sref.startsWith("root:")||sref.startsWith("forward:")){
    		pos=sref.indexOf(":");
    		nav=sref.substring(0, pos);
    		sref=sref.substring(pos+1);
    	}
    	pos=sref.indexOf("(");
    	if(pos>0){
    		stateName=sref.substring(0,pos);
    		param=sref.substring(pos+1,sref.length()-1);
    	}else{
    		stateName=sref;
    	}
    	if(!stateName.startsWith("/")){
    		stateName=GenerateUrl(stateName, controllerName, routeValues,true);
    	}
    	if(nav!=null)
    		param="({"+param+(param==""?"":",")+"'ajax-nav':'"+nav+"'})";
    	else if(!param.isEmpty()){
    		param="({"+param+"})";
    	}
    		
    	String attr="ui-sref=\""+stateName+param+"\""+" ui-sref-opts=\"{reload:true,inherit:false}\"";
        return attr;
    }

    public String GenerateUrl(String actionName, String controllerName, Object routeValues) {
    	Map<String,Object> routeValuesMap=Util.ObjectToMap(routeValues);
    	return GenerateUrl( actionName, controllerName, routeValuesMap,false);
    }
    public String GenerateUrl(String actionName, String controllerName, Map<String,Object> routeValues,boolean forState) {
    	Action action=(Action)request.getAttribute(GlobalConstant.Attr_RequestAction);
    	String area=null;
		if(action==null) {
    		ParsePathInfo(request);
    		action=(Action)request.getAttribute(GlobalConstant.Attr_RequestAction);
    	}
		area=action.getAreaName();
    	if(controllerName==null) controllerName=action.getControllerName();
    	MultiValueMap<String,String> qs=new LinkedMultiValueMap<String,String>();
    	if(routeValues!=null){
    		if(routeValues.containsKey("Area")){
    			area=routeValues.get("Area").toString();
    			routeValues.remove("Area");
    		}
    	
	    	for(String key:routeValues.keySet()){
	    		Object v=routeValues.get(key);
	    		if(v==null)
	    			qs.add(key,"null");
	    		else
	    			qs.add(key,v.toString());
	    	}
    	}
    	String url=(!forState&&usePrefix?BaseMvcApplication.UrlMappingPrefix:"")+"/"+area+"/"+controllerName+"/"+actionName;
    	UriComponents uriComponents= MvcUriComponentsBuilder.fromUriString(url).queryParams(qs).build();
    	return uriComponents.encode().toUriString();
    	
    }
    
}
