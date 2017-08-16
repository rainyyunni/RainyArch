package projectbase.sharparch.hibernate;

import java.util.Hashtable;
import java.util.Map;

import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import projectbase.sharparch.domain.designbycontract.Check;
import projectbase.utils.JavaArchException;

public class HibernateSession {
	// / <summary>
	// / The default factory key used if only one database is being communicated
	// with.
	// / </summary>
	public static final String DefaultFactoryKey = "hibernate.current_session";

	// / <summary>
	// / Maintains a Map of NHibernate session factories, one per database. The
	// key is
	// / the "factory key" used to look up the associated database, and used to
	// decorate respective
	// / repositories. If only one database is being used, this Map contains a
	// single
	// / factory with a key of <see cref = "DefaultFactoryKey" />.
	// / </summary>
	public static final Map<String, SessionFactory> SessionFactories = new Hashtable<String, SessionFactory>();

	private static Interceptor registeredInterceptor;

	private static IHibernateConfigurationCache _configurationCache;

	// / <summary>
	// / Provides access to <see cref = "INHibernateConfigurationCache" />
	// Object.
	// / </summary>
	// / <exception cref = "InvalidOperationException">
	// / Thrown on Set if the Init method has already been called and the
	// / NHibernateSession.Storage property is not null.
	// / </exception>
	public static IHibernateConfigurationCache getConfigurationCache() {
		return _configurationCache;
	}

	public static void setConfigurationCache(IHibernateConfigurationCache value)
			throws JavaArchException {
		if (getStorage() != null) {
			throw new JavaArchException(
					"Cannot set the ConfigurationCache property after calling Init");
		}

		_configurationCache = value;

	}

	// / <summary>
	// / Used to get the current NHibernate session if you're communicating with
	// a single database.
	// / When communicating with multiple databases, invoke <see cref =
	// "CurrentFor" /> instead.
	// / </summary>
	public static Session Current() {

		Check.Require(
				!IsConfiguredForMultipleDatabases(),
				"The NHibernateSession.Current property may "
						+ "only be invoked if you only have one NHibernate session factory; i.e., you're "
						+ "only communicating with one database.  Since you're configured communications "
						+ "with multiple databases, you should instead call CurrentFor(String factoryKey)");

		return CurrentFor(DefaultFactoryKey);

	}

	// / <summary>
	// / An application-specific implementation of ISessionStorage must be setup
	// either thru
	// / <see cref = "InitStorage" /> or one of the <see cref = "Init" />
	// overloads.
	// / </summary>
	public static ISessionStorage _storage;

	public static ISessionStorage getStorage() {
		return _storage;
	}

	public static void setStorage(ISessionStorage value) {
		_storage = value;
	}


	// / <summary>
	// / This method is used by application-specific session storage
	// implementations
	// / and unit tests. Its job is to walk thru existing cached sessions and
	// Close() each one.
	// / </summary>
	public static void CloseAllSessions() {
		if (getStorage() != null) {
			for (Session session : getStorage().GetAllSessions()) {
				if (session.isOpen()) {
					session.close();
				}
			}
		}
	}

	// / <summary>
	// / Used to get the current NHibernate session associated with a factory
	// key; i.e., the key
	// / associated with an NHibernate session factory for a specific database.
	// /
	// / If you're only communicating with one database, you should call <see
	// cref = "Current" /> instead,
	// / although you're certainly welcome to call this if you have the factory
	// key available.
	// / </summary>
	public static Session CurrentFor(String factoryKey) {
		Check.Require(factoryKey != null && !factoryKey.isEmpty(),
				"factoryKey may not be null or empty");
		Check.Require(getStorage() != null,
				"An ISessionStorage has not been configured");
		Check.Require(SessionFactories.containsKey(factoryKey),
				"An ISessionFactory does not exist with a factory key of "
						+ factoryKey);

		Session session = getStorage().GetSessionForKey(factoryKey);

		if (session == null) {
			if (registeredInterceptor != null) {
				session = SessionFactories.get(factoryKey).withOptions()
						.interceptor(registeredInterceptor).openSession();
			} else {
				session = SessionFactories.get(factoryKey).openSession();
			}

			getStorage().SetSessionForKey(factoryKey, session);
		}

		return session;
	}

	// / <summary>
	// / Returns the default ISessionFactory import the DefaultFactoryKey.
	// / </summary>
	public static SessionFactory GetDefaultSessionFactory() {
		return GetSessionFactoryFor(DefaultFactoryKey);
	}

	// / <summary>
	// / Return an ISessionFactory based on the specified factoryKey.
	// / </summary>
	public static SessionFactory GetSessionFactoryFor(String factoryKey) {
		if (!SessionFactories.containsKey(factoryKey)) {
			return null;
		}

		return SessionFactories.get(factoryKey);
	}

	public static void InitStorage(ISessionStorage storage) {
		Check.Require(storage != null,
				"storage mechanism was null but must be provided");
		Check.Require(getStorage() == null,
				"A storage mechanism has already been configured for this application");
		setStorage(storage);
	}

	public static boolean IsConfiguredForMultipleDatabases() {
		return SessionFactories.size() > 1;
	}

	public static void RegisterInterceptor(Interceptor interceptor) {
		Check.Require(interceptor != null, "interceptor may not be null");

		registeredInterceptor = interceptor;
	}

	public static void RemoveSessionFactoryFor(String factoryKey) {
		if (GetSessionFactoryFor(factoryKey) != null) {
			SessionFactories.remove(factoryKey);
		}
	}
}