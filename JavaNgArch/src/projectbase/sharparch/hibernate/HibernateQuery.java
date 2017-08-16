package projectbase.sharparch.hibernate;

import org.hibernate.Session;

public abstract class HibernateQuery {
	protected Session getSession() {
		String factoryKey = SessionFactoryKeyHelper.GetKeyFrom(this);
		return HibernateSession.CurrentFor(factoryKey);
	}
}
