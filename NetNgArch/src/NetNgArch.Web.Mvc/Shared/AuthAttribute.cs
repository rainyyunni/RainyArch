using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Mvc;
using NetNgArch.Domain.BDInterface;
using NetNgArch.Domain.DomainModel.GN;
using NetNgArch.Web.Mvc.Home;
using ProjectBase.Domain;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using ProjectBase.Web.Mvc.Angular;


namespace NetNgArch.Web.Mvc.Shared
{
    public class AuthAttribute : BaseAuthAttribute
    {
        public AuthAttribute()
        {
        }

        public AuthAttribute(string funcCode): base(funcCode){}


        protected override ActionResult CheckLogin(HttpContextBase httpContext)
        {
            if (httpContext.Session["LoginInfo"] == null)
            {
                //var js = "window.location='/Home/Login';";
                //if (httpContext.Request.IsAjaxRequest())
                //    return new RichClientJsonResult(false, RichClientJsonResult.Command_Redirect, "/Home/ShowLogin");
                //return new ClientScriptResult(js);

                //for test conveniece,auto login
                AutoLoginForTest(httpContext);

            }
 
            return null;
        }


        protected override bool CanAccess(HttpContextBase httpContext)
        {
            //return true;
            var funccode = string.IsNullOrEmpty(FuncCode) ? ControllerActionName : FuncCode;
            if (!Util.FuncMap.ContainsKey(funccode)) return true;
            var loginInfo = (LoginInfoViewModel) httpContext.Session["LoginInfo"];
            return loginInfo.CanAccess(funccode);
        }

        protected void AutoLoginForTest(HttpContextBase httpContext)
        {
            var AdminBD = BaseMvcApplication.WindsorContainer.Resolve<IAdminBD>();
            var loginUser = AdminBD.GetLoginUser("admin", AppBaseController.DefaultPassword);
            var loginInfo = new LoginInfoViewModel();
            loginInfo.LoginUser = loginUser;
            loginInfo.LoginCorp = loginUser.Dept.Corp;
            var tmp = loginInfo.LoginCorp.Name;//make proxy load the real entity
            var i = 0;
            foreach (var func in loginInfo.LoginCorp.Funcs)
            {
                loginInfo.AddCorpFuncCode(func.Code);
            }
            foreach (var func in loginUser.Dept.Funcs)
            {
                loginInfo.AddDeptFuncCode(func.Code);
            }
            foreach (var func in loginUser.Funcs)
            {
                loginInfo.AddUserFuncCode(func.Code);
            }
            httpContext.Session["LoginInfo"] = loginInfo;
        }

    }


}
