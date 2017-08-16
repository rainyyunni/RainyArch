package projectbase.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import projectbase.data.hibernatemapbycode.convention.NamingConvention;
import projectbase.domain.BaseDomainObjectWithTypedId;
import projectbase.sharparch.domain.designbycontract.Check;
import projectbase.sharparch.hibernate.HibernateSession;
import projectbase.sharparch.hibernate.IHibernateConfigurationCache;
import projectbase.sharparch.hibernate.ISessionStorage;
import projectbase.utils.function.Action;

/// <summary>
/// SharpArch.NHibernate.NhibernateSession only uses FNH to configure and build session factory,we choose to change that to use NHibernate mapping by code instead.
/// </summary>
public final class HibernateSessionModified extends HibernateSession {


	private static boolean _mapAllEnumsToStrings;
	private static Properties _cfgProperties;

	public static boolean isMapAllEnumsToStrings() {
		return _mapAllEnumsToStrings;
	}

	public static void setMapAllEnumsToStrings(boolean mapAllEnumsToStrings) {
		_mapAllEnumsToStrings = mapAllEnumsToStrings;
	}
	public static Properties getCfgProperties() {
		return _cfgProperties;
	}

	public static void setCfgProperties(Properties cfgProperties) {
		_cfgProperties = cfgProperties;
	}
	public static String OutputXmlMappingsFile;

	public static Action AfterInit;

	public static Configuration Init(ISessionStorage storage,
			List<Class<? extends BaseDomainObjectWithTypedId<Integer>>> annotatedEntityClass, String cfgFile,
			Properties cfgProperties, String validatorCfgFile)
			throws IOException {
		HibernateSession.InitStorage(storage);
		try {
			Configuration cfg = AddConfiguration(
					HibernateSession.DefaultFactoryKey, annotatedEntityClass,
					cfgFile, cfgProperties, validatorCfgFile);
			if (AfterInit != null)
				AfterInit.Do();
			return cfg;
		} catch (IOException e) {
			// If this NHibernate config throws an exception, null the Storage
			// reference so
			// the config can be corrected without having to restart the web
			// application.
			HibernateSession.setStorage(null);
			throw e;
		}
	}

	public static Configuration AddConfiguration(String factoryKey,
			List<Class<? extends BaseDomainObjectWithTypedId<Integer>>> annotatedEntityClass, String cfgFile,
			Properties cfgProperties, String validatorCfgFile)
			throws IOException {
		Configuration config;
		IHibernateConfigurationCache configCache = HibernateSession
				.getConfigurationCache();
//		if (configCache != null) {
//			config = configCache.LoadConfiguration(factoryKey, cfgFile,
//					mappingAssemblies);
//			if (config != null) {
//				SessionFactory sessionFactory = config
//						.buildSessionFactory(null);
//				SessionFactories.put(factoryKey, sessionFactory);
//				return HibernateSession.AddConfiguration(factoryKey,
//						sessionFactory, config, validatorCfgFile);
//			}
//		}

		config = AddConfiguration(factoryKey, annotatedEntityClass,
				ConfigureHibernate(cfgFile, cfgProperties), validatorCfgFile);

		if (configCache != null) {
			//TODO configCache.SaveConfiguration(factoryKey, config);
		}

		return config;
	}

	public static Configuration AddConfiguration(String factoryKey,
			List<Class<? extends BaseDomainObjectWithTypedId<Integer>>> annotatedEntityClass, Configuration cfg,
			String validatorCfgFile) {
		SessionFactory sessionFactory = CreateSessionFactoryFor(
				annotatedEntityClass, cfg);
		Check.Require(!SessionFactories.containsKey(factoryKey),
				"A session factory has already been configured with the key of "
						+ factoryKey);
		SessionFactories.put(factoryKey, sessionFactory);

		return cfg;
	}
	private static SessionFactory CreateSessionFactoryFor(
			List<Class<? extends BaseDomainObjectWithTypedId<Integer>>> annotatedEntityClass, Configuration cfg)  {
//		cfg.addAnnotatedClass(BaseDomainObject.class);
//		cfg.addAnnotatedClass(BaseDomainObjectWithTypedId.class);

		annotatedEntityClass.forEach(o->cfg.addAnnotatedClass(o));

		cfg.setNamingStrategyDelegator(NamingConvention.INSTANCE);
		SessionFactory sf=cfg.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build());

		return sf;
	}

	private static Configuration ConfigureHibernate(String cfgFile,
			Properties cfgProperties) {
		Configuration cfg = new Configuration();

		if (cfgProperties != null) {
			cfg.mergeProperties(cfgProperties);
		}

		if (cfgFile != null && !cfgFile.isEmpty()) {
			cfg = cfg.configure(cfgFile);
		} else if (Files.exists(Paths.get("hibernate.cfg.xml"),
				LinkOption.NOFOLLOW_LINKS)) {
			cfg = cfg.configure();
		}
		for (Object key : cfgProperties.keySet()) {
			cfg.getProperties().setProperty((String) key,
					cfgProperties.getProperty((String) key));
		}
		return cfg;
	}


}
