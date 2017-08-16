package projectbase.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.JDBCException;

import org.springframework.core.Ordered;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.method.HandlerMethod;

import projectbase.domain.IDBErrorForUser;
import projectbase.domain.IExceptionTranslator;
import projectbase.mvc.result.ActionResult;
import projectbase.mvc.result.AjaxScriptResult;
import projectbase.mvc.result.RichClientJsonResult;
import projectbase.mvc.result.ClientScriptResult;
import projectbase.mvc.result.ContentResult;
import projectbase.mvc.result.FileResult;
import projectbase.mvc.result.HttpStatusCodeResult;
import projectbase.mvc.result.JavaScriptResult;
import projectbase.mvc.result.JsonRequestBehavior;
import projectbase.mvc.result.JsonResult;
import projectbase.mvc.result.ViewResult;
import projectbase.utils.InvalidOperationException;
import projectbase.utils.JavaArchException;
import projectbase.utils.Util;


public class BaseController extends HandlerInterceptorAdapter implements HandlerExceptionResolver , Ordered{
	@Resource
	private IExceptionTranslator exTranslator;

	protected final String ViewNameSuffix4TilesContentTemplate="";//".view";//replaced by angular
	protected final String NoMaster="";

	public IExceptionTranslator getExTranslator() {
		return exTranslator;
	}

	public void setExTranslator(IExceptionTranslator value) {
		exTranslator = value;
	}

	@Resource
	private HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
        if (request.getParameter(AjaxHelperExtension.Key_For_ForViewModelOnly)!=null||request.getParameter("action")!=null)
        	return super.preHandle(request, response, handler);
        String viewname = ((HandlerMethod)handler).getMethod().getName();
        if(viewname.equalsIgnoreCase("ChildAction"))return super.preHandle(request, response, handler);
        this.request=request;
        throw new PreHandleResultException(View(viewname));
	}	
	 @InitBinder
     protected void initBinder(WebDataBinder binder) {
		 if(binder.getTarget() instanceof Validator)
			 binder.addValidators((Validator)binder.getTarget());
     }
	 
	@RequestMapping("JavaArch_ChildAction")
	public ActionResult ChildAction(){
		
		Map<String,Object> map=(Map<String,Object>)request.getAttribute(GlobalConstant.Attr_ChildActionMethodInfo);
		if(map==null) throw new JavaArchException("ChildAction MethodInfo not found");
		Action childAction=(Action)map.get(GlobalConstant.Key_ChildAction);
		List<Object> params=(List<Object>)map.get(GlobalConstant.Key_ChildActionParameters);
		List<Class<?>> paramTypes=new ArrayList<Class<?>>();
		try{
		if(params.size()>0)
			paramTypes=params.stream().map(Object::getClass).collect(Collectors.toList());
		}catch(NullPointerException e){
			throw new InvalidOperationException("paramList for child action contains null value!");
		}
		
		Method m;
		ActionResult rtn;
		String controller=childAction.getControllerName();
		Object controllerInstance;
		try {
			if(StringUtils.isEmpty(controller))
				controllerInstance=this;
			else
				controllerInstance=BaseMvcApplication.getSpringContainer().getBean(StringUtils.uncapitalize(controller)+GlobalConstant.Controller_ClassSuffix);
			String[] classNameParts=StringUtils.split(controllerInstance.getClass().getName(),".");
			String area=classNameParts[classNameParts.length-2];
			childAction.setAreaName(area);
			childAction.setControllerName(controllerInstance.getClass().getSimpleName().replace("Controller", ""));
			UrlHelper.BuildActionStack(request,childAction);
			try{
				m = controllerInstance.getClass().getMethod(childAction.getActionName(), paramTypes.toArray(new Class<?>[0]));
			}catch(NoSuchMethodException ex){
				Optional<Method> found=Arrays.stream(controllerInstance.getClass().getMethods()).filter(o->o.getName()==childAction.getActionName()).findFirst();
				if(!found.isPresent()) throw ex;
				m = found.get();
			}
			List<Object> convertedParams=new ArrayList<Object>();
			Parameter[] mparams=m.getParameters();
			for(int i=0;i<mparams.length;i++){
				if(params.size()>i){
					if(params.getClass()==mparams[i].getType())
						convertedParams.add(params.get(i));
					else
						convertedParams.add(new ChildActionParamConverter().convertIfNecessary(params.get(i),mparams[i].getType()));
				}else{
					RequestParam rp=mparams[i].getAnnotation(RequestParam.class);
					if(rp==null)
						convertedParams.add(null);
					else{
						ChildActionParamConverter converter=new ChildActionParamConverter();
						convertedParams.add(converter.convertIfNecessary(rp.defaultValue(), mparams[i].getType()));
					}
				}
			}			
			rtn=(ActionResult)m.invoke(controllerInstance, convertedParams.toArray());
		} catch (Exception e) {
			throw new JavaArchException(e);
		}
		if(rtn instanceof ViewResult && rtn.getViewName()==null){
			rtn.setViewName(childAction.getPathInfo());
		}
		return rtn;

	}

	protected String Message_UserInputError="UserInput_NotPassValidation";
	  
	protected String Message_SaveSuccessfully="Save_Successfully";

	protected ViewResult View(String viewName) {
		return View(viewName,ViewNameSuffix4TilesContentTemplate, null);
	}	 
	protected ViewResult View(String viewName, Object model) {
		return View(viewName,ViewNameSuffix4TilesContentTemplate, model);
	}	
	protected ViewResult View(String viewName, String viewNameSuffixAsMasterName, Object model) {
		OnViewExecuting(model);
		BuildViewModelStack(model);
		ViewResult rst=new ViewResult(ParseViewName(viewName,viewNameSuffixAsMasterName), model);
		rst.setController(this);
		return rst.LoadHelpers(request,this.getClass());
	}
	private String ParseViewName(String viewName,String viewNameSuffixAsMasterName){
		if(viewName==null){
			//replaced by angular
			//request.setAttribute(RequestToSuffixedViewNameTranslator.Attr_ViewNameSuffix,viewNameSuffixAsMasterName);
			return null;
		}
		if(viewName.startsWith("/")) return viewName+viewNameSuffixAsMasterName;
		String pathInfo=request.getPathInfo();
		if(pathInfo==null) pathInfo=UrlHelper.GetPathInfo(request);
		if(pathInfo==null) throw new JavaArchException("include can't find pathinfo");
		String requestToViewName=pathInfo.substring(1,pathInfo.lastIndexOf('/')+1)+viewName;
		return requestToViewName+viewNameSuffixAsMasterName;
	}
	private void BuildViewModelStack(Object modelObject){
		Stack<Object> stack=(Stack<Object>)request.getAttribute(GlobalConstant.Attr_ViewModelStack);
		if(stack==null){
			stack=new Stack<Object>();
			request.setAttribute(GlobalConstant.Attr_ViewModelStack,stack);
		}
		stack.push(modelObject);
	}

	/**
	 * get called before returned from controller to view primarily designed for
	 * change view model before it's being sent to view
	 * 
	 * @param viewModel
	 */
	protected void OnViewExecuting(Object viewModel) {
	}

	protected ActionResult ForView() {
		return ForView(null, ViewNameSuffix4TilesContentTemplate,null);
	}

	protected ActionResult ForView(Object model) {
		return ForView(null, ViewNameSuffix4TilesContentTemplate, model);
	}

	protected ActionResult ForView(String viewName) {
		return ForView(viewName, ViewNameSuffix4TilesContentTemplate, null);
	}

	protected ActionResult ForView(String viewName, Object model) {
		return ForView(viewName, ViewNameSuffix4TilesContentTemplate, model);
	}

	protected ActionResult ForView(String viewName, String viewNameSuffixAsMasterName,
			Object model) {
		return ForView(viewName, viewNameSuffixAsMasterName, model, true, true);
	}

	protected ActionResult ForView(String viewName, String viewNameSuffixAsMasterName,
			Object model, boolean includeModel, boolean includeModelDisplay) {
		if ("true".equalsIgnoreCase(request.getParameter(AjaxHelperExtension.Key_For_ForViewModelOnly))){
			OnViewExecuting(model);
			return AjaxJson(model, includeModel, includeModelDisplay);
		}
		return View(viewName, viewNameSuffixAsMasterName, model);
	}
/* replaced by angular 
	protected ActionResult ForPartialView() {
		return ForPartialView(null, null);
	}

	protected ActionResult ForPartialView(Object model) {
		return ForPartialView(null, model);
	}
	protected ActionResult ForPartialView(String viewName) {
		return ForPartialView(viewName, null);
	}
	protected ActionResult ForPartialView(String viewName, Object model) {
		return ForPartialView(viewName, model, true, true);
	}

	protected ActionResult ForPartialView(String viewName, Object model,
			boolean includeModel, boolean includeModelDisplay) {
		if ("true".equalsIgnoreCase(request.getParameter(AjaxHelperExtension.Key_For_ForViewModelOnly)))
			return AjaxJson(model, includeModel, includeModelDisplay);
		return View(viewName, NoMaster, model);
	}
*/
	private JsonResult AjaxJson(Object model) {
		return AjaxJson(model, true, true);
	}

	private JsonResult AjaxJson(Object model, boolean includeModel, boolean includeModelDisplay)
    {
    	//Map<String, Object> queryString = new Hashtable<String, Object>();
       //TODO request.getQueryString().(queryString);
        //TODO var routeValues = RouteData.Values.ToDictionary(o => o.Key, o => o.Value);
    	Object m=new Object(){
                	   public Object ViewModel = model;
                	   //public Object ViewModelDisplay = DisplayExtension.Display(model);
                	   //public Object RouteValues = null;//routeValues;
                	   //public Map<String, Object> QueryString = queryString; 
                	};

        return RcJson(RichClientJsonResult.Command_ServerVM,m);
    }

	protected JsonResult JsonGet(Object data) {
		return Json(data, JsonRequestBehavior.AllowGet);
	}
	protected JsonResult Json(Object data) {
		JsonResult r = new JsonResult();
		r.setController(this);
		r.setData(data);
		r.setJsonRequestBehavior(JsonRequestBehavior.DenyGet);
		return r;
	}
	protected JsonResult Json(Object data, JsonRequestBehavior jsonRequestBehavior) {
		JsonResult r = new JsonResult();
		r.setController(this);
		r.setData(data);
		r.setJsonRequestBehavior(jsonRequestBehavior);
		return r;
	}
	protected RichClientJsonResult RcJson() {
		return RcJson(null,null);
	}
	protected RichClientJsonResult RcJson(Object data) {
		return RcJson(RichClientJsonResult.Command_ServerData,data);
	}
	protected RichClientJsonResult RcJson(String command,Object data) {
		RichClientJsonResult r = new RichClientJsonResult(true,command,data);
		r.setController(this);
		r.setJsonRequestBehavior(JsonRequestBehavior.AllowGet);
		return r;
	}
	protected RichClientJsonResult RcJsonError(String msg) {
		return RcJsonError(RichClientJsonResult.Command_Message,msg);
	}
	protected RichClientJsonResult RcJsonError(String command,String msg) {
		RichClientJsonResult r = new RichClientJsonResult(false,command,msg);
		r.setController(this);
		r.setJsonRequestBehavior(JsonRequestBehavior.AllowGet);
		return r;
	}
	protected JavaScriptResult JavaScript(String script) {
		JavaScriptResult r=new JavaScriptResult(script);
		r.setController(this);
		return r;
	}
	
	public ActionResult ClientCloseFragment() {
		return RcJson("CloseFragment",null);
	}

	public ActionResult ClientCloseWindow() {
		return RcJson("CloseWindow",null);
	}
	/**
	 * 给客户端的redirect命令
	 * @param target:根据客户端情况协议的命令内容，对angular一般是state名，通常与action同名,"以/开头为绝对state名"
	 * @return
	 */
	public ActionResult ClientRedirect(String target) {
		if(target.startsWith("/")) return RcJson(RichClientJsonResult.Command_Redirect,target);
		return ClientRedirectToAction(target, this.getClass());
	}
	@Deprecated
	public ActionResult ClientRedirectToAction(String action) {
		return ClientRedirectToAction(action, this.getClass());
	}

	public ActionResult ClientRedirectToAction(String action,
			Class<?> controllerClass) {
		UrlHelper helper=new UrlHelper(request,false);
		String url=helper.Action(action);
		return RcJson(RichClientJsonResult.Command_Redirect,url);
	}

    public ActionResult ClientReloadApp(String action,
			Class<?> controllerClass)
    {
		UrlHelper helper=new UrlHelper(request,false);
		String url=helper.Action(action, controllerClass.getSimpleName());

        return RcJson(RichClientJsonResult.Command_AppPage, url);
    }
	/**
	 * when the client submit request by using AjaxSubmit,it can run java script
	 * after the response returned,so don't use this method to run
	 * javascript.Instead,return JsonResult for the client to decide java script
	 * flow.
	 * 
	 * @param js
	 * @return
	 */
	@Deprecated
	public ActionResult ClientRunJavaScript(String js) {
		if (IsAjaxRequest()) {
			AjaxScriptResult r = new AjaxScriptResult();
			r.setController(this);
			r.setScript(js);
			return r;
		}
		return new ClientScriptResult(js).setController(this);
	}

	public ActionResult ClientShowMessage() {
		return ClientShowMessage(null,null);
	}

	public ActionResult ClientShowMessage(String message) {
		return ClientShowMessage(message,null);
	}
	public ActionResult ClientShowMessage(String message,BindException ex) {
		if(StringUtils.isEmpty(message))message=(String) request.getAttribute(GlobalConstant.Attr_ViewMessage);
		if(ex!=null){
			for(ObjectError err : ex.getAllErrors()){
				message="\r\n"+message+err.getDefaultMessage();
			}
		}
		if (IsAjaxRequest()){
			return ex==null?RcJson(RichClientJsonResult.Command_Message,message):RcJsonError(message);
		}
		return Content(message, "text/plain", GlobalConstant.Encoding_Default);
	}
	private boolean IsAjaxRequest() {
		return Util.IsAjaxRequest(request);
	}

	protected ActionResult Content(String content) {
		return new ContentResult(content).setController(this);
	}

	protected ActionResult Content(String content, String contentType,
			String encoding) {
		ContentResult r = new ContentResult(content);
		r.setController(this);
		r.setContentType(contentType);
		r.setContentEncoding(encoding);
		return r;
	}
	protected ActionResult File(Object content,String fileExt)
    {
		FileResult r= new FileResult(content,fileExt,"");
		return r;
    }
	protected ActionResult File(Object content,String fileExt, String downLoadFileName)
    {
		FileResult r= new FileResult(content,fileExt,downLoadFileName);
		return r;
    }
	/*
	protected ActionResult ForwardToAction(String actionname) {
		return ForwardToAction(actionname, null, null);
	}

	protected ActionResult ForwardToAction(String actionname,
			String controllername, String area) {
		String url = "forward:";
		if (!StringUtils.isEmpty(area))
			url = url + "/" + area;
		if (!StringUtils.isEmpty(controllername))
			url = url + "/" + controllername + "/";
		url = url + actionname;
		ActionResult r = new ActionResult(url);
		r.setController(this);
		return r;
	}
*/
	public void SetViewMessage(String message) {
		request.setAttribute(GlobalConstant.Attr_ViewMessage, message);
	}

  	 public List MapCollection( List<?>  sourceList,Type listType) { 
		 return AutoMapperProfile.DefaultMapper.map(sourceList, listType);
	  }
	 public <TDestination> TDestination Map( Object  source,Class<TDestination> destClazz) { 
		 return AutoMapperProfile.DefaultMapper.map(source, destClazz);
	  }
	 public  void   Map( Object  source,Object destination) { 
		  AutoMapperProfile.DefaultMapper.map(source, destination);
	  }  
     protected ActionResult AuthFailure()
     {
     	return RcJsonError("AuthFailure","AuthFailure");
     }
	protected ActionResult OnException(HttpServletRequest req,
			HttpServletResponse response, Object handler, Exception ex) {
		if(ex instanceof BindException){
			SetViewMessage(Message_UserInputError);
			return ClientShowMessage(null,(BindException)ex);
		}else if(ex instanceof PreHandleResultException){
			return ((PreHandleResultException) ex).getResult();
		}else if(ex instanceof JDBCException){
			Exception error = exTranslator.Translate(ex);
			if (error instanceof IDBErrorForUser) {
				return RcJsonError(error.getMessage());
			}
		}
		
		try {
			Util.AddLog("BaseController.OnException", ex);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String debug = req.getServletContext().getInitParameter(
				GlobalConstant.ContextParam_Debug);
		if (debug.equalsIgnoreCase("true"))
			throw new RuntimeException(ex);
		return RcJsonError(ex.getClass().getName());

	}
	public String GetLastRequestStringBeforeLogin() {
		return (String)request.getSession().getAttribute(GlobalConstant.Attr_LastRequestBeforeLogin);
	}
	public void ClearLastRequestStringBeforeLogin() {
		request.getSession().removeAttribute(GlobalConstant.Attr_LastRequestBeforeLogin);
	}
	@Override
	public final ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		request.setAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE, ex);
		return OnException(request, response, handler, ex);
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
