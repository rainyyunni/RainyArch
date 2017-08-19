using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Mvc;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc.Angular;


namespace ProjectBase.Web.Mvc
{
    /// <summary>
    /// check user's priviledges to accessing actions.
    /// you can assign funccode to each action,or the default value for funccode is controllername.actionname 
    /// 注意这个attribute被多线程共享，因此实例变量值被共享，所以得每次给实例变量在使用前赋值
    /// </summary>
    public abstract class BaseAuthAttribute : AuthorizeAttribute
    {
        private ActionResult _failureResult;
        protected string FuncCode { get; set; }
        protected string ControllerActionName { get; set; }
        protected bool AuthEnabled { get; set; }

        protected BaseAuthAttribute()
        {
            AuthEnabled = true;
        }

        protected BaseAuthAttribute(string funcCode)
        {
            AuthEnabled = true;
            FuncCode = funcCode;
        }
        protected BaseAuthAttribute(bool authEnabled)
        {
            AuthEnabled = authEnabled;
        }
        public override void OnAuthorization(AuthorizationContext filterContext)
        {
            ControllerActionName = filterContext.ActionDescriptor.ControllerDescriptor.ControllerName + "." +
               filterContext.ActionDescriptor.ActionName;

            //mvc3对AuthorizeAttribute的处理可能是：如果controller上有该Attribute，就忽略方法上的该Attribute，即方法上Attribute的prop值无法覆盖controller上初始化的值，
            //所以只好此处检测并用方法上的该Attribute的prop值，而将controller上的设置作为方法上没有时的默认值。
            var actionauth =(BaseAuthAttribute) filterContext.ActionDescriptor.GetCustomAttributes(this.GetType(), false).FirstOrDefault();
            if (actionauth!=null)
            {
                AuthEnabled = actionauth.AuthEnabled;
                FuncCode =string.IsNullOrEmpty(actionauth.FuncCode) ? ControllerActionName : actionauth.FuncCode;
            }else
            {
                FuncCode = ControllerActionName;
            }
            base.OnAuthorization(filterContext);
        }

        protected override bool AuthorizeCore(HttpContextBase httpContext)
        {
            if (!AuthEnabled) return true;
            var action = CheckLogin(httpContext);
            if (action!=null){
                _failureResult = action;
                var qs = httpContext.Request.QueryString;
                qs.Remove(AjaxHelperExtension.Key_For_ForViewModelOnly);
                httpContext.Session[GlobalConstant.Attr_LastRequestBeforeLogin]=httpContext.Request.PathInfo + (qs.HasKeys() ?  "?" + qs:"");
                return false;
            }

            if (CanAccess(httpContext))
                WriteUserLog(httpContext);
            else
            {
                _failureResult = new RichClientJsonResult(false, "AuthFailure", "AuthFailure"); 
                return false;
            }

            return true;
        }

        protected override void HandleUnauthorizedRequest(AuthorizationContext context)
        {
                context.Result = _failureResult;
        }


        protected virtual ActionResult CheckLogin(HttpContextBase httpContext)
        {
            return null;
        }

        protected abstract bool CanAccess(HttpContextBase httpContext);

        protected virtual void WriteUserLog(HttpContextBase httpContext)
        {
        }

    }
}
