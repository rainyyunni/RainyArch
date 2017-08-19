using System.Collections;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using NetNgArch.Domain.BDInterface;
using NetNgArch.Domain.DomainModel.GN;
using ProjectBase.Domain;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using System.Collections.Generic;
using System;
using ProjectBase.Web.Mvc.Angular;


namespace NetNgArch.Web.Mvc.Home
{
    public class HomeController : BaseController
    {
        public IAdminBD AdminBD { get; set; }
        public ICommonBD<Corp> CorpBD { get; set; }
        public ICommonBD<Dept> DeptBD { get; set; }
        public ICommonBD<User> UserBD { get; set; }

        public ActionResult ShowLogin()
        {
            if (Session["LoginInfo"] != null) return ClientRedirect("MainFrameLoggedIn");
            var m = new LoginAttemptViewModel()
                        {
                            CorpCode = "1",
                            Code = "admin",
                            Password = "1"
                        };
            return ForView("Login", m);
        }
        [HttpPost]
        [Transaction]
        public ActionResult Login(LoginAttemptViewModel attempt)
        {
            if (Session["LoginInfo"] != null) return ClientRedirect("MainFrameLoggedIn");

            var loginUser = AdminBD.GetLoginUser(attempt.Code, attempt.Password);
            if (loginUser == null)
            {
                SetViewMessage("Login_Wrong");
                return ClientShowMessage();
            }
            if (!loginUser.IsActive)
            {
                SetViewMessage("Login_IsNotActive");
                return ClientShowMessage();
            }
            var loginMark = Request.Cookies["LoginMark"];
            if (loginUser.LoginMark!=null){//检查本次登录是否为上次的登录的会话延续
                if (loginMark==null) return ClientShowMessage("Login_AlreadyIn");
                var lastsessionId = loginMark["lastsessionId"];
                if (lastsessionId != loginUser.LoginMark) return ClientShowMessage("Login_AlreadyIn");
            }

            loginMark = new HttpCookie("LoginMark");
            loginMark.HttpOnly = true;
            loginMark["lastsessionId"] = Session.SessionID;
            loginMark.Expires = DateTime.Now.AddDays(2);
            Response.Cookies.Add(loginMark);
           
            loginUser.LoginMark = Session.SessionID;
            var loginInfo = new LoginInfoViewModel();
            loginInfo.LoginUser = loginUser;
            loginInfo.LoginCorp = loginUser.Dept.Corp;
            var tmp = loginInfo.LoginCorp.Name;//make proxy load the real entity
            var i = 0;
            foreach(var func in loginInfo.LoginCorp.Funcs)
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
            Session["LoginInfo"] = loginInfo;
            return ClientRedirect("MainFrameLoggedIn");
        }
    
        public ActionResult MainFrame()
        {
            Util.test();
            var loginInfo = ((LoginInfoViewModel) Session["LoginInfo"]);
            if (loginInfo != null)
            {
                AdminBD.RefreshUser(loginInfo.LoginUser);
            }
            return ForView(loginInfo);
        }
        [HttpPost]
        public ActionResult Logout()
        {
            Session.Abandon();
            return ClientReloadApp("MainFrame",null);
        }
    }
}
