package projectbase.sharparch.hibernate;

import org.hibernate.Session;

public interface ISessionStorage {
	Iterable<Session> GetAllSessions();

	Session GetSessionForKey(String factoryKey);

	void SetSessionForKey(String factoryKey, Session session);
}
