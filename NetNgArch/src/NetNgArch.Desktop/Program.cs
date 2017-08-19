using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using NetNgArch.Domain.NHibernateMappingByCode;
using ProjectBase.Data;
using ProjectBase.Desktop;
using ProjectBase.Utils;
using SharpArch.NHibernate;

namespace NetNgArch.Desktop
{
    static class Program
    {
        public static readonly DesktopApplication App=new DesktopApplication();
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {

            Application.ApplicationExit += App.Application_End;
            try
            {
                App.Application_Start();
                Application.EnableVisualStyles();
                Application.SetCompatibleTextRenderingDefault(false);
                Application.Run(new Form1());
            }catch(Exception e)
            {
                App.Application_Error(e);
            }
        }
    }

    public class DesktopApplication:BaseApplication
    {
        protected override void InitProjectHierarchy()
        {
            ProjectHierarchy.ProjectName = "NetNgArch";
            ProjectHierarchy.MvcNS = "NetNgArch.Desktop";
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


        public override void Application_Start()
        {
            NHCfgProperties = new Dictionary<string, string> { };
            NHibernateSession.RegisterInterceptor(new MyInterceptor());
            NHibernateSessionModified.AfterInit = AfterNHInit;
            base.Application_Start();
        }
        private void AfterNHInit()
        {
            //所有登录标志清除
            var sql = "update GN_User set LoginMark=null";
            UtilQuery.StatelessExecuteSql(sql);
        }
    }
}
