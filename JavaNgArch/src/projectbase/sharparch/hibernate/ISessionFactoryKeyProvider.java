package projectbase.sharparch.hibernate;

public interface ISessionFactoryKeyProvider {
	// / <summary>
	// / Gets the session factory key.
	// / </summary>
	// / <returns></returns>
	String GetKey();

	// / <summary>
	// / Gets the session factory key.
	// / </summary>
	// / <param name="anObject">An optional Object that may have an attribute
	// used to determine the session factory key.</param>
	// / <returns></returns>
	String GetKeyFrom(Object anObject);
}
