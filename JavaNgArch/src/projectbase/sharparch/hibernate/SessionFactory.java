package projectbase.sharparch.hibernate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// <summary>
///     Provides the ability to decorate repositories with an attribute defining the factory key
///     for the given repository; accordingly, the respective session factory will be used to 
///     communicate with the database.  This allows you to declare different repositories to 
///     communicate with different databases.
/// </summary>
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SessionFactory {
	String FactoryKey();

	//不能写静态方法，就用内部类写吧
	public class AttrMethod {
		// / <summary>
		// / Global method to retrieve the factory key from an Object, as
		// defined in its
		// / SessionFactoryAttribute, if available. Defaults to the
		// DefaultFactoryKey
		// / if not found.
		// / </summary>
		public static String GetKeyFrom(Object target) {
			if (!HibernateSession.IsConfiguredForMultipleDatabases()) {
				return HibernateSession.DefaultFactoryKey;
			}

			Class<?> objectType = target.getClass();

			SessionFactory attr = (SessionFactory) objectType
					.getAnnotation(SessionFactory.class);

			if (attr != null) {
				return attr.FactoryKey();
			}

			return HibernateSession.DefaultFactoryKey;
		}

	}
}