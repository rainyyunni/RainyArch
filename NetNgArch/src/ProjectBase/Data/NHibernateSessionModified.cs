
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using NHibernate;
using NHibernate.Cfg;
using NHibernate.Cfg.MappingSchema;
using NHibernate.Mapping.ByCode;
using ProjectBase.Data.NHibernateMapByCode.Convention;
using ProjectBase.Domain;
using ProjectBase.Domain.NhibernateMapByCode;
using SharpArch.Domain;
using SharpArch.NHibernate;

namespace ProjectBase.Data
{
    /// <summary>
    /// SharpArch.NHibernate.NhibernateSession only uses FNH to configure and build session factory,we choose to change that to use NHibernate mapping by code instead.
    /// </summary>
    public static class NHibernateSessionModified
    {

        #region Properties 

        public static Hashtable NamespaceMapToTablePrefix
        {
            get { return NamingConventions.NamespaceMapToTablePrefix; }
            set { NamingConventions.NamespaceMapToTablePrefix=value; }
        }
        public static Action<ModelMapper> AutoMappingOverride { set; get; }

        public static bool MapAllEnumsToStrings { set; get; }

        public static Type BaseEntityToIgnore { set; get; }

        public static string OutputXmlMappingsFile { set; get; }

        public static Dictionary<string, ISessionFactory> SessionFactories { set; get; }
        public static Action AfterInit { set; get; }

        #endregion Properties

        #region Methods 

        public static Configuration Init(
            ISessionStorage storage, string[] mappingAssemblies, string cfgFile,IDictionary<string, string> cfgProperties, string validatorCfgFile)
        {
            SessionFactories = new Dictionary<string, ISessionFactory>();
            NHibernateSession.InitStorage(storage);
            try
            {
                var cfg=AddConfiguration(NHibernateSession.DefaultFactoryKey, mappingAssemblies, cfgFile, cfgProperties, validatorCfgFile);
                if (AfterInit != null) AfterInit();
                return cfg;
            }
            catch
            {
                // If this NHibernate config throws an exception, null the Storage reference so 
                // the config can be corrected without having to restart the web application.
                NHibernateSession.Storage = null;
                throw;
            }
        }
        public static Configuration AddConfiguration(string factoryKey, string[] mappingAssemblies, string cfgFile, IDictionary<string, string> cfgProperties, string validatorCfgFile)
        {
            Configuration config;
            var configCache = NHibernateSession.ConfigurationCache;
            if (configCache != null)
            {
                config = configCache.LoadConfiguration(factoryKey, cfgFile, mappingAssemblies);
                if (config != null)
                {
                    var sessionFactory = config.BuildSessionFactory();
                    SessionFactories.Add(factoryKey, sessionFactory);
                    return NHibernateSession.AddConfiguration(factoryKey, sessionFactory, config, validatorCfgFile);
                }
            }

            config = AddConfiguration(factoryKey,mappingAssemblies, 
                ConfigureNHibernate(cfgFile, cfgProperties), 
                validatorCfgFile);

            if (configCache != null)
            {
                configCache.SaveConfiguration(factoryKey, config);
            }

            return config;
        }

        public static Configuration AddConfiguration(string factoryKey, string[] mappingAssemblies,Configuration cfg,string validatorCfgFile)
        {
            var sessionFactory = CreateSessionFactoryFor(mappingAssemblies, cfg);
            Check.Require(
                !SessionFactories.ContainsKey(factoryKey),
                    "A session factory has already been configured with the key of " + factoryKey);
            SessionFactories.Add(factoryKey, sessionFactory);

            return NHibernateSession.AddConfiguration(factoryKey, sessionFactory, cfg, validatorCfgFile);
        }
        private static ISessionFactory CreateSessionFactoryFor(string[] mappingsAssemblies, Configuration cfg)
        {
            var assemblies = new List<Assembly>();
            Array.ForEach(mappingsAssemblies,i=>assemblies.Add(Assembly.LoadFrom(MakeLoadReadyAssemblyName(i))));
            var mapping = GetMappings(assemblies);
            cfg.AddDeserializedMapping(mapping, null);
            return cfg.BuildSessionFactory();
        }
        private static Configuration ConfigureNHibernate(string cfgFile, IDictionary<string, string> cfgProperties)
        {
            var cfg = new Configuration();

            if (cfgProperties != null)
            {
                cfg.AddProperties(cfgProperties);
            }

            if (string.IsNullOrEmpty(cfgFile) == false)
            {
                cfg= cfg.Configure(cfgFile);
            }else if (File.Exists("Hibernate.cfg.xml"))
            {
                cfg= cfg.Configure();
            }
            foreach (var cfgProperty in cfgProperties)
            {
                cfg.Properties[cfgProperty.Key] = cfgProperty.Value;
            }
            return cfg;
        }


        private static HbmMapping GetMappings(IEnumerable<Assembly> mappingsAssemblies)
        {
            //Using the built-in auto-mapper
            var mapper = new ConventionModelMapper();
            DefineBaseClass(mapper);
            var allEntities = new List<Type>(); 
            foreach (var mappingsAssembly in mappingsAssemblies)
            {
                allEntities.AddRange(mappingsAssembly.GetTypes().Where(
                        t => BaseEntityToIgnore.IsAssignableFrom(t)
                            && t != BaseEntityToIgnore
                            && !t.IsInterface
                    ).ToList());
            }
            mapper.AddAllManyToManyRelations(allEntities);
            mapper.ApplyNamingConventions();
            if (MapAllEnumsToStrings) mapper.MapAllEnumsToStrings();
            if (AutoMappingOverride != null) AutoMappingOverride(mapper);
            OverrideByClassMapping(mapper,mappingsAssemblies);

            var mapping = mapper.CompileMappingFor(allEntities);
            
            //ShowOutputXmlMappings(mapping);
            return mapping;
        }
        private static void DefineBaseClass(ConventionModelMapper mapper)
        {
            if (BaseEntityToIgnore == null) BaseEntityToIgnore = typeof(BaseDomainObject);
            mapper.IsEntity((type, declared) =>
                BaseEntityToIgnore.IsAssignableFrom(type) &&
                BaseEntityToIgnore != type &&
                !type.IsInterface);
            mapper.IsRootEntity((type, declared) => type.BaseType == BaseEntityToIgnore);
            //mapper.IsTablePerClassHierarchy((type, declared) =>true);
        }
        private static void ShowOutputXmlMappings(HbmMapping mapping)
        {
            var outputXmlMappings = mapping.AsString();
            Console.WriteLine(outputXmlMappings);
            File.WriteAllText(OutputXmlMappingsFile, outputXmlMappings);
        }
        private static string MakeLoadReadyAssemblyName(string assemblyName)
        {
            return (assemblyName.IndexOf(".dll") == -1) ? assemblyName.Trim() + ".dll" : assemblyName.Trim();
        }

        private static void OverrideByClassMapping(ModelMapper mapper, IEnumerable<Assembly>  mappingsAssemblies)
        {
            var allMappingClasses = new List<Type>();
            foreach (var mappingsAssembly in mappingsAssemblies)
            {
                allMappingClasses.AddRange(mappingsAssembly.GetTypes().Where(
                        t => typeof(IClassByClassMapping).IsAssignableFrom(t)
                    ).ToList());
            }
            mapper.AddMappings(allMappingClasses);
        }
        #endregion Methods
    }
}