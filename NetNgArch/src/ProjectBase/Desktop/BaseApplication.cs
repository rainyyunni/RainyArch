using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using AutoMapper;
using Castle.Windsor;
using CommonServiceLocator.WindsorAdapter;
using Microsoft.Practices.ServiceLocation;
using ProjectBase.BusinessDelegate;
using ProjectBase.Data;
using ProjectBase.Utils;
using SharpArch.NHibernate;
using log4net.Config;
using System.Windows.Forms;

namespace ProjectBase.Desktop
{
    /// <summary>
    /// initialize and configure all components, namely nhibernate,,castle windor,log4net,autoMapper
    /// </summary>
    public abstract class BaseApplication
    {
        private ISessionStorage _sessionStorage;
        private static readonly IApplicationStorage _applicationStorage=  new SimpleApplicationStorage();
        private static readonly IWindsorContainer _container = new WindsorContainer();
        protected static IDictionary<string, string> NHCfgProperties;

        /// <summary>
        /// Provides a globally available access to the <see cref="IWindsorContainer" /> instance.
        /// </summary>
        public static IWindsorContainer WindsorContainer
        {
            get { return _container; }
        }
        public static IApplicationStorage ApplicationStorage
        {
            get { return _applicationStorage; }
        }
        protected BaseApplication()
        {
            Init();
        }

        public void Init()
        {
            _sessionStorage = new SimpleSessionStorage();
            Util.InitStorage(_applicationStorage);
        }

        public virtual void Application_Error(Exception e)
        {
            
            //if(!HttpContext.Current.IsDebuggingEnabled)
            //{
            //    var html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head><body><h2>系统运行错误："+Server.GetLastError().GetBaseException().GetType()+"</h2></body></htm>";
            //    HttpContext.Current.Response.ClearContent();
            //    HttpContext.Current.Response.Write(html);
            //    HttpContext.Current.Response.End();
            //}
            Util.AddLog("Application_Error", e);
        }

        public virtual void Application_Start()
        {
            InitProjectHierarchy();
            InitLog4net();
            InitializeServiceLocator();
            InitAutoMapper();
            InitOther();
            NHibernateInitializer.Instance().InitializeNHibernateOnce(this.InitialiseNHibernateSessions);
        }

        protected abstract void InitProjectHierarchy();

        protected void InitLog4net()
        {
            XmlConfigurator.Configure();
        }

        protected void InitAutoMapper()
        {
            AutoMapperProfile.DomainModelMappingAssemblies = GetDomainModelMappingAssemblies();
            Mapper.Initialize(x => x.AddProfile<AutoMapperProfile>());
        }

        protected void InitOther()
        {
            NHibernateExceptionTranslator.InitStorage(_applicationStorage);
        }

        public virtual void Application_End(Object sender,EventArgs e)
        {
            NHibernateSession.CloseAllSessions();
            _container.Dispose();
        }

        /// <summary>
        /// Instantiate the container and add all Controllers that derive from
        /// WindsorController to the container.  Also associate the Controller
        /// with the WindsorContainer ControllerFactory.
        /// </summary>
        protected virtual void InitializeServiceLocator()
        {
            // ControllerBuilder.Current.SetControllerFactory(new WindsorControllerFactory(_container));
            //  _container.RegisterControllers(GetControllerAssemblies());
            AddComponentsTo(_container);
            ServiceLocator.SetLocatorProvider(() => new WindsorServiceLocator(_container));
        }

        protected void AddComponentsTo(IWindsorContainer container)
        {
            CastleWindorComponentRegistrar.AddComponentsTo(container);
        }

        protected Assembly[] GetControllerAssemblies()
        {
            return new Assembly[]{Assembly.LoadFrom(Application.StartupPath+"/"+ProjectHierarchy.MvcNS+".dll")};
        }

        private void InitialiseNHibernateSessions()
        {
            NHibernateSession.ConfigurationCache = new NHibernateConfigurationFileCache();
            NHibernateSessionModified.NamespaceMapToTablePrefix = GetNamespaceMapToTablePrefix();
            //NHibernateSessionModified.AutoMappingOverride = something;
            NHibernateSessionModified.OutputXmlMappingsFile = Application.StartupPath + "/NHibernate.mapping.xml";
            NHibernateSessionModified.Init(
                _sessionStorage,
                GetDomainModelMappingAssemblies(),
                Application.StartupPath + "/NHibernate.config",
                NHCfgProperties,
                null
                );
        }

        protected string[] GetDomainModelMappingAssemblies()
        {
            return new string[] {Application.StartupPath + "/" + ProjectHierarchy.DomainNS + ".dll"};
        }

        protected abstract Hashtable GetNamespaceMapToTablePrefix();
    }
}