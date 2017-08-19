using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Mvc;
using System.Web.Routing;
using System.Web.Script.Serialization;
using AutoMapper;
using ProjectBase.Domain;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc.Angular;

namespace ProjectBase.Web.Mvc
{
    public class BaseController : Controller
    {
        private Object _viewMode;

        public IExceptionTranslator ExTranslator { get; set; }

        protected override void ExecuteCore()
        {
            //this is for debugging for now
            base.ExecuteCore();
        }

        protected override void OnException(ExceptionContext filterContext)
        {
            Util.AddLog("BaseController.OnException", filterContext.Exception);
            if (HttpContext.IsDebuggingEnabled) return;
            var e = filterContext.Exception.InnerException ?? filterContext.Exception;
            filterContext.Result = RcJsonError(e.GetType().Name); ;
            filterContext.ExceptionHandled = true;
       }
        protected override void OnActionExecuting(ActionExecutingContext filterContext)
        {
            if (Request.QueryString[AjaxHelperExtension.Key_For_ForViewModelOnly]!=null) return;
            var viewname = filterContext.ActionDescriptor.ActionName;
            filterContext.Result = View(viewname);
        }
        protected virtual void OnViewExecuting(object viewModel)
        {
        }

        protected void SetViewModel<TParam>(Action<TParam> func)
        {
            if (_viewMode is TParam && _viewMode != null)
            {
                func((TParam) _viewMode);
            }
        }

        protected override ViewResult View(IView view, object model)
        {
            _viewMode = model;
            OnViewExecuting(_viewMode);
            return base.View(view, model);
        }

        protected override ViewResult View(string viewName, string masterName, object model)
        {
            _viewMode = model;
            OnViewExecuting(_viewMode);
            //if (viewName != null && (viewName.StartsWith("/") || viewName.StartsWith("~/")))
            //    Request.RequestContext.RouteData.DataTokens["AbsoluteViewName"] = viewName;
            return base.View(viewName, masterName, model);
        }
        protected JsonResult JsonGet(Object data)
        {
            return Json(data, JsonRequestBehavior.AllowGet);
        }
        protected JsonResult AjaxJson(Object model)
        {
            return AjaxJson(model, true,true);
        }
        protected JsonResult AjaxJson(Object model, bool includeModel, bool includeModelDisplay)
        {
            return RcJson(RichClientJsonResult.Command_ServerVM, new {ViewModel=model});
            /*
            var queryString = new Dictionary<string, object>();
            this.Request.QueryString.CopyTo(queryString);
            var routeValues = RouteData.Values.ToDictionary(o => o.Key, o => o.Value);
            if (includeModel)
                return includeModelDisplay
                       ? base.Json(new { ViewModel = model, ViewModelDisplay = model.Display(), RouteValues = routeValues, QueryString = queryString },JsonRequestBehavior.AllowGet)
                       : base.Json(new { ViewModel = model, RouteValues = routeValues, QueryString = queryString }, JsonRequestBehavior.AllowGet);
            else
            {
                return includeModelDisplay
                       ? base.Json(new { ViewModelDisplay = model.Display(), RouteValues = routeValues, QueryString = queryString }, JsonRequestBehavior.AllowGet)
                       : base.Json(new { RouteValues = routeValues, QueryString = queryString }, JsonRequestBehavior.AllowGet);
          }
            */
        }
        public RichClientJsonResult RcJson()
        {
            return RcJson(null,null);
        }
        public RichClientJsonResult RcJson(Object data)
        {
            return RcJson(RichClientJsonResult.Command_ServerData, data);
        }
        public RichClientJsonResult RcJson(string command, Object data)
        {
            return new RichClientJsonResult(true, command, data);
        }
        public RichClientJsonResult RcJsonError(string msg)
        {
            return RcJsonError(RichClientJsonResult.Command_Message, msg);
        }
        public RichClientJsonResult RcJsonError(string command, string msg)
        {
            return new RichClientJsonResult(false, command, msg);
        }
        //private static IDictionary<string, object> GetViewModelDisplaySimple(object model)
        //{
        //    var rtn = new Dictionary<string, object>();
        //    var props = model.GetType().GetProperties();
        //    Array.ForEach(props, p =>
        //    {
        //        var displaymethod =
        //            model.GetType().GetMethod(p.Name + "_Display");
        //        var displayvalue = displaymethod == null ?
        //            p.GetValue(model, null).Display()
        //            : displaymethod.Invoke(model, null);
        //        var value = p.GetValue(model, null);
        //        if (value!=null&& value.ToString()!= displayvalue)
        //            rtn.Add(p.Name, displayvalue);
        //    });
        //    if (rtn.Count == 0) return null;
        //    return rtn;
        //}
        protected ActionResult ForView()
        {
            return ForView(null, null);
        }

        protected ActionResult ForView(object model)
        {
            return ForView(null,null, model);
        }
        protected ActionResult ForView(string viewName)
        {
            return ForView(viewName, null);
        }
        protected ActionResult ForView(string viewName,object model)
        {
            return ForView(viewName, null, model);
        }
        protected ActionResult ForView(string viewName, string masterName, object model=null)
        {
            return ForView(viewName, masterName, model,true,true);
        }
        protected ActionResult ForView(string viewName, string masterName, object model, bool includeModel, bool includeModelDisplay)
        {
            if (Request.Params[AjaxHelperExtension.Key_For_ForViewModelOnly] == "true")
            {
                _viewMode = model;
                OnViewExecuting(model);
                return AjaxJson(model, includeModel, includeModelDisplay);
            }
            return View(viewName, masterName, model);
        }
        protected ActionResult ForView(IView view, object model=null)
        {
            return ForView(view, model,true,true);
        }
        protected ActionResult ForView(IView view, object model, bool includeModel, bool includeModelDisplay)
        {

            if (Request.Params[AjaxHelperExtension.Key_For_ForViewModelOnly] == "true")
            {
                _viewMode = model;
                OnViewExecuting(model);
                return AjaxJson(model, includeModel, includeModelDisplay);
            }
            return View(view, model);
        }
     /*   replaced by angular
      * protected ActionResult ForPartialView()
        {
            return ForPartialView(null, null);
        }
        protected ActionResult ForPartialView(object model)
        {
            return ForPartialView(null,model);
        }
        protected ActionResult ForPartialView(string viewName, object model)
        {
            return ForPartialView(viewName, model, true, true);
        }
        protected ActionResult ForPartialView(string viewName, object model, bool includeModel, bool includeModelDisplay)
        {
            if (Request.Params[AjaxHelperExtension.Key_For_ForViewModelOnly] == "true")
                return AjaxJson(model, includeModel, includeModelDisplay);
            return PartialView(viewName, model);
        }
*/
        public TDestination Map<TSource, TDestination>(TSource source) where TSource : BaseDomainObject
        {
            Mapper.CreateMap<TSource, TDestination>();

            return Mapper.Map<TSource, TDestination>(source);
        }
 
        public string Message_UserInputError
        {
            get { return "UserInput_NotPassValidation"; }
        }

        public string Message_SaveSuccessfully
        {
            get
            {
                return "Save_Successfully";
            }
        }

        public void SetViewMessage(string message)
        {

            ViewBag.Message = message ;
        }

        private ActionResult ClientCloseFragment()
        {
            return RcJson("CloseFragment",null);
        }

        private ActionResult ClientCloseWindow()
        {
            return RcJson("CloseWindow",null);
        }
        /**
         * 给客户端的redirect命令
         * @param target:根据客户端情况协议的命令内容，对angular一般是state名，通常与action同名
         * @return
         */	
        public ActionResult ClientRedirect(String target) {
		    return ClientRedirectToAction(target);
	    }

        private ActionResult ClientRedirectToAction(string action, string controller=null)
        {
            var url = UrlHelper.GenerateUrl(null, action, controller, this.ControllerContext.RouteData.Values, RouteTable.Routes, 
                                  this.Request.RequestContext, true);
            url = UrlHelperExtension.Deprefix(url);
            return RcJson(RichClientJsonResult.Command_Redirect, url);
        }
        public ActionResult ClientReloadApp(string action, string controller=null,string qs=null,string state=null)
        {
            var url = UrlHelper.GenerateUrl(null, action, controller, this.ControllerContext.RouteData.Values, RouteTable.Routes,
                                  this.Request.RequestContext, true);
            if (!String.IsNullOrEmpty(qs)) url = url + "?" + qs;
            if (!String.IsNullOrEmpty(state))
            {
                state = UrlHelper.GenerateUrl(null, state, controller, this.ControllerContext.RouteData.Values,
                                                      RouteTable.Routes,
                                                      this.Request.RequestContext, true);
                state = UrlHelperExtension.Deprefix(state);
            }
            if (!String.IsNullOrEmpty(state)) url = url + "#" + state;
            return RcJson(RichClientJsonResult.Command_AppPage, url);
        }
        public ActionResult ClientShowMessage(string message=null)
        {
		    if(String.IsNullOrEmpty(message))message=ViewBag.Message;

            string errmsg = "";
            IEnumerable<ModelState> modelStates = null;
            modelStates = this.ViewData.ModelState.Values;
            foreach (ModelState modelState in modelStates)
            {
                foreach (ModelError modelError in modelState.Errors)
                {
                    string errorText = modelError.ErrorMessage;
                    if (String.IsNullOrEmpty(errorText) && modelError.Exception != null)
                    {
                        errorText = modelError.Exception.InnerException == null ?
                            modelError.Exception.Message :
                            modelError.Exception.InnerException.Message;
                    }
                    errmsg = "\r\n" + errmsg + errorText;
                }
            }
            if (Request.IsAjaxRequest())
                return errmsg == "" ? RcJson(RichClientJsonResult.Command_Message, ViewBag.Message) : RcJsonError(errmsg);
            return Content(message, "text/plain", Encoding.Default);
        }
        protected ActionResult AuthFailure()
        {
            return RcJsonError("AuthFailure", "AuthFailure");
        }
        public ActionResult ViewAsExcel(ViewResult view)
        {
            Response.ContentType = "application/vnd.ms-excel";
            return view;
        }
        public String GetLastRequestStringBeforeLogin()
        {
            return (String)Session[GlobalConstant.Attr_LastRequestBeforeLogin];
        }
        public void ClearLastRequestStringBeforeLogin()
        {
            Session[GlobalConstant.Attr_LastRequestBeforeLogin]=null;
        }
    }
  
}

