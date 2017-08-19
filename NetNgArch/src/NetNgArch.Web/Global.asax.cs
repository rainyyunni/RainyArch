using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using System.Web.Mvc;
using System.Web.Routing;
using NetNgArch.Domain.NHibernateMappingByCode;
using NetNgArch.Web.Mvc.Home;
using ProjectBase.Data;
using ProjectBase.Utils;
using ProjectBase.Web.Mvc;
using SharpArch.NHibernate;
//using Spark.Web.Mvc;

namespace NetNgArch.Web
{
    /// <summary>
    /// 
    /// </summary>
    public class MvcApplication : BaseMvcApplication
    {
        protected override void InitProjectHierarchy()
        {
            ProjectHierarchy.ProjectName = "NetNgArch";
        }

        protected override Hashtable GetNamespaceMapToTablePrefix()
        {
            return new Hashtable
                       {
                           {"TA", "TA_"},
                           {"CZ", "CZ_"},
                           {"GN", "GN_"}
                       };
        }

        public override void Init()
        {
            base.Init();
            //
        }

        protected override void Application_BeginRequest(object sender, EventArgs e)
        {
            base.Application_BeginRequest(sender, e);
        }

        protected override void Application_Start()
        {
            base.Application_Start();
            NHCfgProperties = new Dictionary<string, string> { };
            NHibernateSession.RegisterInterceptor(new MyInterceptor());
            NHibernateSessionModified.AfterInit = AfterNHInit;
        }

        public override void Application_End(object sender, EventArgs e)
        {
            base.Application_End(sender, e);
        }

        private void AfterNHInit()
        {
            //所有登录标志清除
            var sql = "update GN_User set LoginMark=null";
            UtilQuery.StatelessExecuteSql(sql);
        }

        protected void Session_End()
        {
            var loginInfo = ((LoginInfoViewModel)Session["LoginInfo"]);
            if (loginInfo == null) return;
            if (loginInfo.LoginUser == null)
            {
                Util.AddLog("Session End:loginInfo.LoginUser == null");
                return;
            }
            String sql = "update GN_User set LoginMark=null where ID=" + loginInfo.LoginUser.Id;
            UtilQuery.StatelessExecuteSql(sql);
           
        }

    }
}