package projectbase.sharparch.hibernate.mvc;



import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import projectbase.sharparch.hibernate.HibernateSession;
import projectbase.sharparch.hibernate.ISessionStorage;
import projectbase.sharparch.hibernate.SimpleSessionStorage;
import projectbase.utils.Util;

    public class WebSessionStorage  implements ServletRequestListener,ISessionStorage
    {
        private static final String HttpContextSessionStorageKey = "HttpContextSessionStorageKey";
        
        public WebSessionStorage(){
        	
        }
        public Iterable<Session> GetAllSessions()
        {
        	ISessionStorage storage = GetSimpleSessionStorage();
            return storage.GetAllSessions();
        }

        public Session GetSessionForKey(String factoryKey)
        {
        	SimpleSessionStorage storage = GetSimpleSessionStorage();
            return storage.GetSessionForKey(factoryKey);
        }

        public void SetSessionForKey(String factoryKey, Session session)
        {
        	SimpleSessionStorage storage = GetSimpleSessionStorage();
            storage.SetSessionForKey(factoryKey, session);
        }

        private static SimpleSessionStorage GetSimpleSessionStorage()
        {
            return (SimpleSessionStorage)((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getAttribute(HttpContextSessionStorageKey,RequestAttributes.SCOPE_REQUEST);
        }

		@Override
		public void requestDestroyed(ServletRequestEvent sre) {
			try{
	            HibernateSession.CloseAllSessions();
	
	            HttpServletRequest req = (HttpServletRequest)sre.getServletRequest();
	            req.removeAttribute(HttpContextSessionStorageKey);
			}catch(Exception e){
				Util.AddLog(e.getMessage());
			}
			
		}
		@Override
		public void requestInitialized(ServletRequestEvent sre) {
        	SimpleSessionStorage storage = (SimpleSessionStorage)((HttpServletRequest)sre.getServletRequest()).getAttribute(HttpContextSessionStorageKey);
            if (storage == null)
            {
                storage = new SimpleSessionStorage();
                ((HttpServletRequest)sre.getServletRequest()).setAttribute(HttpContextSessionStorageKey,storage);
            }
		}




    }
