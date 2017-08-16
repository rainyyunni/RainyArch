package projectbase.sharparch.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

public class SimpleSessionStorage implements ISessionStorage {
	private final Map<String, Session> _storage = new HashMap<String, Session>();

	// / <summary>
	// / Returns all the values of the internal Map of sessions.
	// / </summary>
	// / <returns></returns>
	public Iterable<Session> GetAllSessions() {
		return _storage.values();
	}

	// / <summary>
	// / Returns the session associated with the specified factoryKey or
	// / null if the specified factoryKey is not found.
	// / </summary>
	// / <param name = "factoryKey"></param>
	// / <returns></returns>
	public Session GetSessionForKey(String factoryKey) {

		if (!_storage.containsKey(factoryKey)) {
			return null;
		}

		return _storage.get(factoryKey);
	}

	// / <summary>
	// / Stores the session into a Map import the specified factoryKey.
	// / If a session already exists by the specified factoryKey,
	// / it gets overwritten by the new session passed in.
	// / </summary>
	// / <param name = "factoryKey"></param>
	// / <param name = "session"></param>
	public void SetSessionForKey(String factoryKey, Session session) {
		_storage.put(factoryKey, session);
	}
}
