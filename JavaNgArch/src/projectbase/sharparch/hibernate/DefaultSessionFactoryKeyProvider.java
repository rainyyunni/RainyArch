package projectbase.sharparch.hibernate;

/// <summary>
/// Implementation of <see cref="ISessionFactoryKeyProvider" /> that uses 
/// the <see cref="SessionFactoryAttribute" /> to determine the session
/// factory key.
/// </summary>
public class DefaultSessionFactoryKeyProvider implements
		ISessionFactoryKeyProvider {
	public String GetKey() {
		return HibernateSession.DefaultFactoryKey;
	}

	// / <summary>
	// / Gets the session factory key.
	// / </summary>
	// / <param name="anObject">An Object that may have the <see
	// cref="SessionFactoryAttribute"/> applied.</param>
	// / <returns></returns>
	public String GetKeyFrom(Object anObject) {
		return SessionFactory.AttrMethod.GetKeyFrom(anObject);
	}
}
