using System;
using System.Collections.Generic;
using System.IO;
using System.Web;
using System.Web.Configuration;
using System.Linq;
using System.Web.Mvc;
using NetNgArch.Domain.BDInterface;
using NetNgArch.Domain.DomainModel.GN;
using NetNgArch.Web.Mvc.Home;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using ProjectBase.Web.Mvc.Angular;


namespace NetNgArch.Web.Mvc.Shared
{
    public class AppBaseController : BaseController
    {
        public IAdminBD AdminBD { get; set; }
        public static string DefaultPassword = "c4ca4238a0b923820dcc509a6f75849b";//默认密码为1
        public static string AdminCode = "Admin"; //用于管理部门和用户的固定代码
  
        protected int GetLoginCorpId()
        {
            var corp = ((LoginInfoViewModel)Session["LoginInfo"]).LoginCorp;
            return corp.Id;
        }
       protected Corp GetLoginCorp()
       {
           var corp = ((LoginInfoViewModel) Session["LoginInfo"]).LoginCorp;
           AdminBD.RefreshCorp(corp);
           return corp;
       }
       protected User GetLoginUser()
       {
           var user = ((LoginInfoViewModel) Session["LoginInfo"]).LoginUser;
           AdminBD.RefreshUser(user);
           return user;
       }
      
        protected bool CanAccess(string funcCode)
        {
            var logininfo = (LoginInfoViewModel)Session["LoginInfo"];
            return logininfo.CanAccess(funcCode);

        }
 
        protected void CheckFuncAuth(string funcCode)
        {
            var logininfo = (LoginInfoViewModel)Session["LoginInfo"];
            if (logininfo.CanAccess(funcCode)) return;
            throw new AuthFailureException();
        }

        //登录用户是否为admin
        public bool IsAdmin()
        {
            return IsAdmin(GetLoginUser());
        }
        //给定用户是否为admin
        public bool IsAdmin(User user)
        {
            return user != null && !user.IsTransient() && user.Code.ToLower()==AdminCode.ToLower();
        }
        protected ActionResult FileNotFound()
        {
            return RcJsonError("FileNotFound", "FileNotFound");
        }
        protected override void OnException(ExceptionContext filterContext)
        {
            if (filterContext.Exception is AuthFailureException)
            {
                filterContext.Result = AuthFailure();
                filterContext.ExceptionHandled = true;
                return;
            }
            base.OnException(filterContext);
        }
        private string ToHtmlEntityString(string s)
        {
            var chars = s.ToCharArray();
            var entitystring = "";
            Array.ForEach(chars, c =>
                                     {
                                         var ss = HttpUtility.UrlEncodeUnicode(c + "").Replace("%u", "&#x");
                                         entitystring = entitystring + ss;
                                         if (ss.StartsWith("&#x")) entitystring = entitystring + ";";
                                     });
            return entitystring;
        }
       
    }
}
