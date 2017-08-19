using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Web;
using System.Web.Configuration;
using System.Web.Mvc;
using System.Web.Routing;
using AutoMapper;
using Castle.Windsor;
using CommonServiceLocator.WindsorAdapter;
using Microsoft.Practices.ServiceLocation;
using ProjectBase.BusinessDelegate;
using ProjectBase.Data;
using ProjectBase.Utils;
using SharpArch.NHibernate;
using SharpArch.NHibernate.Web.Mvc;
using SharpArch.Web.Mvc.Castle;
//using Spark.Web.Mvc;
//using Spark.Web.Mvc.Descriptors;
using log4net.Config;
using System.Linq;


namespace ProjectBase.Web.Mvc
{
    /// <summary>
    /// initialize and configure all components, namely nhibernate,mvc3,castle windor,log4net,spark,autoMapper
    /// </summary>
    public abstract class BaseMvcApplication : HttpApplication
    {
        private ISessionStorage _webSessionStorage;
        private IApplicationStorage _webApplicationStorage;
        private static readonly IWindsorContainer _container = new WindsorContainer();
        protected static IDictionary<string, string> NHCfgProperties; 

        /// <summary>
        /// Provides a globally available access to the <see cref="IWindsorContainer" /> instance.
        /// </summary>
        public static IWindsorContainer WindsorContainer
        {
            get { return _container; }
        }

        /// <summary>
        /// Due to issues on IIS7, the NHibernate initialization must occur in Init().
        /// But Init() may be invoked more than once; accordingly, we introduce a thread-safe
        /// mechanism to ensure it's only initialized once.
        /// See http://msdn.microsoft.com/en-us/magazine/cc188793.aspx for explanation details.
        /// </summary>
        public override void Init()
        {
            base.Init();
            _webSessionStorage = new WebSessionStorage(this);
            _webApplicationStorage=new WebApplicationStorage(Application);
            Util.InitStorage(_webApplicationStorage);
        }

        protected virtual void Application_BeginRequest(object sender, EventArgs e)
        {
            NHibernateInitializer.Instance().InitializeNHibernateOnce(this.InitialiseNHibernateSessions);
        }
        protected virtual void Application_Error(object sender, EventArgs e)
        {
            if(!HttpContext.Current.IsDebuggingEnabled)
            {
                var html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body><h2>系统运行错误："+Server.GetLastError().GetBaseException().GetType()+"</h2></body></htm>";
                HttpContext.Current.Response.ClearContent();
                HttpContext.Current.Response.Write(html);
                HttpContext.Current.Response.End();
            }
            Util.AddLog("Application_Error", Server.GetLastError().GetBaseException());
        }

        protected virtual void Application_Start()
        {
            InitProjectHierarchy();
            InitSpark();
            InitMvc();
            InitLog4net();
            InitializeServiceLocator();
            InitAutoMapper();
            InitOther();
        }

        protected abstract void InitProjectHierarchy();

        protected void InitSpark()
        {
           ViewEngines.Engines.Clear();
            //var t = new SparkViewFactory();
            //t.AddFilter(new AreaDescriptorFilter());
          var t = new CustomRazorViewEngine();
          ViewEngines.Engines.Add(t);
        }
        protected void InitLog4net()
        {
            XmlConfigurator.Configure();
        }
        protected void InitMvc()
        {
            AreaRegistration.RegisterAllAreas();
            RegisterGlobalFilters(GlobalFilters.Filters);
            RegisterRoutes(RouteTable.Routes);

            ModelBinders.Binders.DefaultBinder = new ProjectBase.Web.Mvc.DefaultModelBinder();

            //remove System.Web.Mvc.clientDataTypeValidator to use custom version instead
            var clientDataTypeValidator = ModelValidatorProviders.Providers.OfType<System.Web.Mvc.ClientDataTypeModelValidatorProvider>().FirstOrDefault();
            if (null != clientDataTypeValidator)
            {
                ModelValidatorProviders.Providers.Remove(clientDataTypeValidator);
            }
            ModelValidatorProviders.Providers.Add(new ProjectBase.Web.Mvc.Angular.ClientDataTypeModelValidatorProvider());

            ModelMetadataProviders.Current = new ProjectBase.Web.Mvc.Angular.CustomModelMetadataProvider();

        }
        protected void InitAutoMapper()
        {
            AutoMapperProfile.DomainModelMappingAssemblies = GetDomainModelMappingAssemblies();
            Mapper.Initialize(x => x.AddProfile<AutoMapperProfile>());
        }
        protected void InitOther()
        {
            NHibernateExceptionTranslator.InitStorage(_webApplicationStorage);
        }

        public virtual void Application_End(object sender, EventArgs e)
        {
            _container.Dispose();
        }
        /// <summary>
        /// Instantiate the container and add all Controllers that derive from
        /// WindsorController to the container.  Also associate the Controller
        /// with the WindsorContainer ControllerFactory.
        /// </summary>
        protected virtual void InitializeServiceLocator() 
        {
            ControllerBuilder.Current.SetControllerFactory(new WindsorControllerFactory(_container));
            _container.RegisterControllers(GetControllerAssemblies());
            AddComponentsTo(_container);
            ServiceLocator.SetLocatorProvider(() => new WindsorServiceLocator(_container));
        }
        protected void AddComponentsTo(IWindsorContainer container)
        {
            CastleWindorComponentRegistrar.AddComponentsTo(container);
        }

        protected Assembly[] GetControllerAssemblies()
        {
            return new Assembly[]{Assembly.LoadFrom(Server.MapPath("~/bin/"+ProjectHierarchy.MvcNS+".dll"))};
        }

        private void InitialiseNHibernateSessions()
        {
            NHibernateSession.ConfigurationCache = new NHibernateConfigurationFileCache();
            NHibernateSessionModified.NamespaceMapToTablePrefix = GetNamespaceMapToTablePrefix();
            //NHibernateSessionModified.AutoMappingOverride = something;
            NHibernateSessionModified.OutputXmlMappingsFile =Server.MapPath("~/NHibernate.mapping.xml");
            NHibernateSessionModified.Init(
                _webSessionStorage,
                GetDomainModelMappingAssemblies(),
                Server.MapPath("~/NHibernate.config"),
                NHCfgProperties, 
                null
            );
        }
        protected string[] GetDomainModelMappingAssemblies()
        {//new string[] { Server.MapPath("~/bin/" + GetDomainModelMappingAssembly().GetName().Name) },
            return new string[] { Server.MapPath("~/bin/"+ProjectHierarchy.DomainNS + ".dll")};
        }

        protected abstract Hashtable GetNamespaceMapToTablePrefix();

        protected  void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
        }

        protected virtual void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

            routes.MapRoute(
                "RootViews",
                "{controller}/{action}", // all views under root,not belong to any area
                new { controller = "Home", action = "" } // Parameter defaults
            );

        }

        //public static void LoadPrecompiledViews(SparkViewFactory factory)
        //{
        //    factory.Engine.LoadBatchCompilation(Assembly.Load("Views.dll"));
        //}

    }

    //public class AreaDescriptorFilter : Spark.Web.Mvc.Descriptors.AreaDescriptorFilter
    //{
    //    public override void ExtraParameters(
    //        ControllerContext context,
    //        IDictionary<string, object> extra)
    //    {
    //        object value;
    //        if (context.RouteData.Values.TryGetValue("area", out value))
    //            extra["area"] = value;
    //        if (context.RouteData.DataTokens["AbsoluteViewName"]!=null)
    //            extra["AbsoluteViewName"] = context.RouteData.DataTokens["AbsoluteViewName"];
    //    }

    //    public override IEnumerable<string> PotentialLocations(
    //        IEnumerable<string> locations,
    //        IDictionary<string, object> extra)
    //    {
    //        string viewname ;
    //        TryGetString(extra, "AbsoluteViewName", out viewname);
    //        if (viewname != null)
    //        {
    //            locations = locations.Concat(new string[] { viewname, viewname + ".spark" });
    //        }
    //        string areaName;
    //        var t= TryGetString(extra, "area", out areaName)
    //                   ? locations.Select(x => Path.Combine(areaName, x)).Concat(locations)
    //                   : locations;
    //        return t;
    //    }
        
    //}
}